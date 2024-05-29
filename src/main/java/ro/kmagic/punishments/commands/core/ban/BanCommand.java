package ro.kmagic.punishments.commands.core.ban;

import ro.kmagic.punishments.enums.BanType;
import ro.kmagic.punishments.objects.ban.Ban;
import ro.kmagic.punishments.objects.ban.BanRepository;
import ro.kmagic.punishments.objects.command.Description;
import ro.kmagic.punishments.objects.command.SubCommand;
import ro.kmagic.punishments.objects.command.Usage;
import ro.kmagic.punishments.objects.minecraftplayer.MinecraftPlayer;
import ro.kmagic.punishments.objects.minecraftplayer.MinecraftPlayerRepository;
import ro.kmagic.punishments.util.ChatUtils;
import ro.kmagic.punishments.util.LogManager;
import ro.kmagic.punishments.util.Placeholders;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.Objects;

@Usage(usage = "[-s] <player> <time> <reason>")
@Description(description = "Ban a player.")
public class BanCommand implements SubCommand {

    @Override
    public void execute(CommandSender player, String[] args) {
        boolean silent = false;

        if (args.length != 0) {
            if (args[0].equals("-s")) silent = true;

            if (silent) {
                if (args.length >= 3) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        player.sendMessage(messages().get("unknown-player").replaceAll("%s", args[1]));
                        return;
                    }

                    if (target.hasPermission("punishments.punishment-immune")) {
                        player.sendMessage(ChatColor.RED + "You cannot " + name() + " that player!");
                        return;
                    }

                    String reason = "";
                    if (args.length >= 4) {
                        int i = 0;
                        for (String s : args) {
                            if (i > 2) reason = reason.concat(s + " ");
                            i++;
                        }
                        reason = reason.substring(0, reason.length() - 1);
                    } else reason = "The Ban Hammer has spoken!";

                    String time = args[2];

                    MinecraftPlayer mp = getMinecraftPlayer(target);

                    if (mp == null) {
                        player.sendMessage(ChatColor.RED + "Unexpected error: player somehow null (ERR CODE 404)");
                        return;
                    }

                    ban(player, target, reason, time, mp);

                    for (Player p : Bukkit.getOnlinePlayers()) if (p.hasPermission("punishments.staff-broadcasts")) sendHoverableText(target, p, true, time.equals("-1"), time, BanRepository.getBanByUuid(target.getUniqueId()));
                } else Bukkit.dispatchCommand(player, "punishments help ban");
            } else {
                if (args.length >= 2) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        player.sendMessage(messages().get("unknown-player").replaceAll("%s", args[0]));
                        return;
                    }

                    if (target.hasPermission("punishments.punishment-immune")) {
                        player.sendMessage(ChatColor.RED + "You cannot " + name() + " that player!");
                        return;
                    }

                    String reason = "";
                    if (args.length >= 4) {
                        int i = 0;
                        for (String s : args) {
                            if (i > 1) reason = reason.concat(s + " ");
                            i++;
                        }
                        reason = reason.substring(0, reason.length() - 1);
                    } else reason = "The Ban Hammer has spoken!";

                    String time = args[1];

                    MinecraftPlayer mp = getMinecraftPlayer(target);

                    if (mp == null) {
                        player.sendMessage(ChatColor.RED + "Unexpected error: player somehow null (ERR CODE 404)");
                        return;
                    }

                    ban(player, target, reason, time, mp);

                    for (Player p : Bukkit.getOnlinePlayers()) sendHoverableText(target, p, false, time.equals("-1"), time, Objects.requireNonNull(BanRepository.getBanByUuid(target.getUniqueId())));
                } else Bukkit.dispatchCommand(player, "punishments help ban");
            }
        } else Bukkit.dispatchCommand(player, "punishments help ban");
    }

    private MinecraftPlayer getMinecraftPlayer(Player target) {
        return MinecraftPlayerRepository.getPlayerByUuid(target.getUniqueId());
    }

    private void ban(CommandSender player, Player target, String reason, String time, MinecraftPlayer mp) {
        mp.setBanned(true);
        //if (Settings.BAN_IP_ADDRESSES) database().insertBannedIp(target);
        if (!time.equals("-1")) {
            database().insertNewBan(target.getUniqueId().toString(), target.getName(), BanType.ban, reason, player.getName(), BanRepository.getNextId());
            mp.setBanexpirydate(new Timestamp(System.currentTimeMillis() + (Placeholders.getTime(time))));
            target.kickPlayer(Placeholders.setBanPlaceholders(messages().get("ban-message"), Objects.requireNonNull(BanRepository.getBanByUuid(target.getUniqueId()))));
        } else {
            database().insertNewBan(target.getUniqueId().toString(), target.getName(), BanType.perm, reason, player.getName(), BanRepository.getNextId());
            mp.setBanexpirydate(new Timestamp(System.currentTimeMillis()));
            target.kickPlayer(Placeholders.setBanPlaceholders(messages().get("perm-ban-message"), Objects.requireNonNull(BanRepository.getBanByUuid(target.getUniqueId()))));
        }

        if (player instanceof Player) LogManager.addLog((Player) player, "Ban", reason, target.getName(), time);

        player.sendMessage("Successfully banned " + target.getName() + " for " + time + ".");
    }

    private void sendHoverableText(Player target, Player onlinePlayer, boolean silent, boolean perm, String time, Ban ban) {
        TextComponent text;

        text = silent ? new TextComponent(perm ? Placeholders.setBanPlaceholders(messages().get("perm-ban-broadcast-message") + ChatColor.WHITE + " [SILENT]", BanRepository.getBanByUuid(target.getUniqueId())) : Placeholders.setBanPlaceholders(messages().get("ban-broadcast-message") + ChatColor.WHITE + " [SILENT]", BanRepository.getBanByUuid(target.getUniqueId()))) : new TextComponent(perm ? Placeholders.setBanPlaceholders(messages().get("perm-ban-broadcast-message"), Objects.requireNonNull(BanRepository.getBanByUuid(target.getUniqueId()))) : Placeholders.setBanPlaceholders(messages().get("ban-broadcast-message"), Objects.requireNonNull(BanRepository.getBanByUuid(target.getUniqueId()))));
        time = time.equals("-1") ? "Permanent Ban" : time;
        String slnt = silent ? "Yes" : "No";

        String hover = "";

        hover = hover.concat("Player banned by " + ban.getAdmin() + "\n");
        hover = hover.concat(colorize("&fPlayer  &c» &f" + ban.getUsername() + "\n"));
        hover = hover.concat(colorize("&fReason &c» &f" + ban.getReason() + "\n"));
        hover = hover.concat(colorize("&fTime     &c» &f" + time + "\n"));
        hover = hover.concat(colorize("&fSilent   &c» &f" + slnt + "\n"));
        hover = hover.concat(colorize("&fBan ID  &c» &f" + ban.getId()));

        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hover)));

        onlinePlayer.spigot().sendMessage(text);
    }

    private String colorize(String s) {
        return ChatUtils.colorize(s);
    }

}
