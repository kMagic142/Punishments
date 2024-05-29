package ro.kmagic.punishments.commands.core.blacklist;

import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.kmagic.punishments.objects.blacklist.Blacklist;
import ro.kmagic.punishments.objects.blacklist.BlacklistRepository;
import ro.kmagic.punishments.objects.command.Description;
import ro.kmagic.punishments.objects.command.SubCommand;
import ro.kmagic.punishments.objects.command.Usage;
import ro.kmagic.punishments.objects.minecraftplayer.MinecraftPlayer;
import ro.kmagic.punishments.objects.minecraftplayer.MinecraftPlayerRepository;
import ro.kmagic.punishments.util.ChatUtils;
import ro.kmagic.punishments.util.LogManager;
import ro.kmagic.punishments.util.Placeholders;

import java.util.Objects;

@Usage(usage = "[-s] <player> <reason>")
@Description(description = "Blacklist a player")
public class BlacklistCommand implements SubCommand {

    @Override
    public void execute(CommandSender player, String[] args) {
        boolean silent = false;

        if (args.length != 0) {
            if (args[0].equals("-s")) silent = true;

            if (silent) {
                if (args.length >= 2) {
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
                    if (args.length >= 3) {
                        int i = 0;
                        for (String s : args) {
                            if (i > 1) reason = reason.concat(s + " ");
                            i++;
                        }
                        reason = reason.substring(0, reason.length() - 1);
                    } else reason = "The Ban Hammer has spoken!";

                    MinecraftPlayer mp = getMinecraftPlayer(target);

                    if (mp == null) {
                        player.sendMessage(ChatColor.RED + "Unexpected error: player somehow null (ERR CODE 404)");
                        return;
                    }

                    blacklist(player, target, reason, mp);

                    for (Player p : Bukkit.getOnlinePlayers()) if (p.hasPermission("punishments.staff-broadcasts")) sendHoverableText(target, p, true, BlacklistRepository.getBlacklistByUuid(target.getUniqueId()));
                } else Bukkit.dispatchCommand(player, "punishments help blacklist");
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
                    if (args.length >= 3) {
                        int i = 0;
                        for (String s : args) {
                            if (i > 1) reason = reason.concat(s + " ");
                            i++;
                        }
                        reason = reason.substring(0, reason.length() - 1);
                    } else reason = "The Ban Hammer has spoken!";

                    MinecraftPlayer mp = getMinecraftPlayer(target);

                    if (mp == null) {
                        player.sendMessage(ChatColor.RED + "Unexpected error: player somehow null (ERR CODE 404)");
                        return;
                    }

                    blacklist(player, target, reason, mp);

                    for (Player p : Bukkit.getOnlinePlayers()) sendHoverableText(target, p, false, Objects.requireNonNull(BlacklistRepository.getBlacklistByUuid(target.getUniqueId())));
                } else Bukkit.dispatchCommand(player, "punishments help blacklist");
            }
        } else Bukkit.dispatchCommand(player, "punishments help blacklist");
    }

    private MinecraftPlayer getMinecraftPlayer(Player target) {
        return MinecraftPlayerRepository.getPlayerByUuid(target.getUniqueId());
    }

    private void blacklist(CommandSender player, Player target, String reason, MinecraftPlayer mp) {
        mp.setBlacklisted(true);
        database().insertBannedIp(target);

        database().insertNewBlacklist(target.getUniqueId().toString(), target.getName(), reason, player.getName(), BlacklistRepository.getNextId());
        target.kickPlayer(Placeholders.setBlacklistPlaceholders(messages().get("blacklist-message"), Objects.requireNonNull(BlacklistRepository.getBlacklistByUuid(target.getUniqueId()))));

        if (player instanceof Player) LogManager.addLog((Player) player, "Blacklist", reason, target.getName(), "Permanent");

        player.sendMessage("Successfully blacklisted " + target.getName() + ".");
    }

    private void sendHoverableText(Player target, Player onlinePlayer, boolean silent, Blacklist ban) {
        TextComponent text;

        text = silent ? new TextComponent(Placeholders.setBlacklistPlaceholders(messages().get("ban-broadcast-message") + ChatColor.WHITE + " [SILENT]", BlacklistRepository.getBlacklistByUuid(target.getUniqueId()))) : new TextComponent(Placeholders.setBlacklistPlaceholders(messages().get("ban-broadcast-message"), Objects.requireNonNull(BlacklistRepository.getBlacklistByUuid(target.getUniqueId()))));
        String slnt = silent ? "Yes" : "No";

        String hover = "";

        hover = hover.concat("Player blacklisted by " + ban.getAdmin() + "\n");
        hover = hover.concat(colorize("&fPlayer  &c» &f" + ban.getUsername() + "\n"));
        hover = hover.concat(colorize("&fReason &c» &f" + ban.getReason() + "\n"));
        hover = hover.concat(colorize("&fSilent   &c» &f" + slnt + "\n"));
        hover = hover.concat(colorize("&fBlacklist ID  &c» &f" + ban.getId()));

        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hover)));

        onlinePlayer.spigot().sendMessage(text);
    }

    private String colorize(String s) {
        return ChatUtils.colorize(s);
    }

}
