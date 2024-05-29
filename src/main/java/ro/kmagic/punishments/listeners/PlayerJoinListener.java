package ro.kmagic.punishments.listeners;

import ro.kmagic.punishments.Punishments;
import ro.kmagic.punishments.enums.BanType;
import ro.kmagic.punishments.objects.ban.Ban;
import ro.kmagic.punishments.objects.ban.BanRepository;
import ro.kmagic.punishments.objects.blacklist.Blacklist;
import ro.kmagic.punishments.objects.blacklist.BlacklistRepository;
import ro.kmagic.punishments.objects.minecraftplayer.MinecraftPlayer;
import ro.kmagic.punishments.objects.minecraftplayer.MinecraftPlayerRepository;
import ro.kmagic.punishments.util.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("ConstantConditions")
public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        ResultSet set = Punishments.getPlugin().getDatabase().query("SELECT * FROM " + Punishments.tablePrefix + "players WHERE uuid='" + player.getUniqueId() + "'");

        MinecraftPlayer p = MinecraftPlayerRepository.getPlayerByUuid(player.getUniqueId());

        ResultSet ips = Punishments.getPlugin().getDatabase().query("SELECT * FROM " + Punishments.tablePrefix + "banned_ips");

        try {
            while (ips.next()) {
                if (ips.getString("ip").equals(player.getAddress().getHostName())) {
                    try {
                        if (!p.getBanned()) {
                            Ban ban = BanRepository.getBanByUuid(ips.getString("uuid"));
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "punishments ban " + player.getName() + " -1 " + ban.getReason());
                        }
                    } catch (Exception ignored) {}

                    try {
                        if (!p.getBlacklisted()) {
                            Blacklist blacklist = BlacklistRepository.getBlacklistByUuid(ips.getString("uuid"));
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "punishments blacklist " + player.getName() + blacklist.getReason());
                        }
                    } catch (Exception ignored) {}
                }
            }
            if (!set.next()) {
                Punishments.getPlugin().getDatabase().insertNewPlayer(player.getUniqueId(), player.getName());
            } else {
                if (p.getBanned()) {
                    Ban ban = BanRepository.getBanByUuid(player.getUniqueId());
                    if (ban.getType() == BanType.ban) {
                        if (p.getBanexpirydate().getTime() < System.currentTimeMillis()) {
                            p.setBanned(false);
                            Punishments.getPlugin().getDatabase().execute("DELETE FROM " + Punishments.tablePrefix + "bans WHERE uuid='" + player.getUniqueId() + "'");
                            return;
                        }
                        player.kickPlayer(Placeholders.setBanPlaceholders(Punishments.getPlugin().getMessages().get("ban-message"), ban));
                        e.setJoinMessage(null);
                    } else if (ban.getType() == BanType.perm) {
                        player.kickPlayer(Placeholders.setBanPlaceholders(Punishments.getPlugin().getMessages().get("perm-ban-message"), ban));
                        e.setJoinMessage(null);
                    }
                }

                if (p.getBlacklisted()) {
                    Blacklist blacklist = BlacklistRepository.getBlacklistByUuid(player.getUniqueId());

                    player.kickPlayer(Placeholders.setBlacklistPlaceholders(Punishments.getPlugin().getMessages().get("blacklist-message"), blacklist));
                    e.setJoinMessage(null);

                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
}
