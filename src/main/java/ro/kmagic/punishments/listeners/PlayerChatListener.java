package ro.kmagic.punishments.listeners;

import ro.kmagic.punishments.Punishments;
import ro.kmagic.punishments.enums.MuteType;
import ro.kmagic.punishments.objects.minecraftplayer.MinecraftPlayer;
import ro.kmagic.punishments.objects.minecraftplayer.MinecraftPlayerRepository;
import ro.kmagic.punishments.objects.mute.Mute;
import ro.kmagic.punishments.objects.mute.MuteRepository;
import ro.kmagic.punishments.util.Placeholders;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Objects;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        MinecraftPlayer mp = Objects.requireNonNull(MinecraftPlayerRepository.getPlayerByUuid(e.getPlayer().getUniqueId()));
        if (mp.getMuted()) {
            Mute mute = Objects.requireNonNull(MuteRepository.getMuteByUuid(e.getPlayer().getUniqueId()));
            if (mute.getType() == MuteType.mute) {
                if (mp.getMuteexpirydate().getTime() < System.currentTimeMillis()) {
                    mp.setMuted(false);
                    Punishments.getPlugin().getDatabase().execute("DELETE FROM " + Punishments.tablePrefix + "mutes WHERE uuid='" + e.getPlayer().getUniqueId() + "'");
                    return;
                }
                e.getPlayer().sendMessage(Placeholders.setMutePlaceholders(Punishments.getPlugin().getMessages().get("mute-message"), mute));
                e.setCancelled(true);
            } else if (mute.getType() == MuteType.perm) {
                e.getPlayer().sendMessage(Placeholders.setMutePlaceholders(Punishments.getPlugin().getMessages().get("perm-mute-message"), mute));
                e.setCancelled(true);
            }
        }
    }

}
