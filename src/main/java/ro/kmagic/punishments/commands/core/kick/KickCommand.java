package ro.kmagic.punishments.commands.core.kick;

import ro.kmagic.punishments.objects.command.Description;
import ro.kmagic.punishments.objects.command.SubCommand;
import ro.kmagic.punishments.objects.command.Usage;
import ro.kmagic.punishments.objects.minecraftplayer.MinecraftPlayer;
import ro.kmagic.punishments.objects.minecraftplayer.MinecraftPlayerRepository;
import ro.kmagic.punishments.util.LogManager;
import ro.kmagic.punishments.util.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Usage(usage = "[-s] <player> <reason>")
@Description(description = "Kick a player with a custom message.")
public class KickCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 0) {
            boolean silent = args[0].equals("-s");

            if (args.length >= 2 && !silent) {
                Player target = Bukkit.getPlayer(args[0]);
                String reason = "";
                int i = 0;
                for (String arg : args) {
                    if (i > 0) reason = reason.concat(arg + " ");
                    i++;
                }
                if (reason.length() > 1) reason = reason.substring(0, reason.length()-1);
                if (target == null) {
                    sender.sendMessage(messages().get("unknown-player").replaceAll("%s", args[0]));
                    return;
                }

                if (target.hasPermission("punishments.punishment-immune")) {
                    sender.sendMessage(ChatColor.RED + "You cannot " + name() + " that player!");
                    return;
                }

                MinecraftPlayer mp = MinecraftPlayerRepository.getPlayerByUuid(target.getUniqueId());

                assert mp != null : args[0] + " is somehow not available. Unable to kick them!";
                mp.setKicks(mp.getKicks() + 1);
                target.kickPlayer(Placeholders.setKickPlaceholders(messages().get("kick-message"), reason, sender));

                if (sender instanceof Player) LogManager.addLog((Player) sender, "Kick", reason, target.getName(), "None");

                sender.sendMessage(ChatColor.WHITE + "Successfully kicked " + target.getName());
                for (Player player : Bukkit.getOnlinePlayers()) player.sendMessage(Placeholders.setKickPlaceholders(messages().get("kick-broadcast-message"), reason, sender, args[0]));
            } else if (args.length >= 3) {
                Player target = Bukkit.getPlayer(args[1]);
                String reason = "";
                int i = 0;
                for (String arg : args) {
                    if (i > 1) reason = reason.concat(arg + " ");
                    i++;
                }
                if (reason.length() > 1) reason = reason.substring(0, reason.length()-1);

                if (target == null) {
                    sender.sendMessage(messages().get("unknown-player").replaceAll("%s", args[1]));
                    return;
                }

                if (target.hasPermission("punishments.punishment-immune")) {
                    sender.sendMessage(ChatColor.RED + "You cannot " + name() + " that player!");
                    return;
                }

                MinecraftPlayer mp = MinecraftPlayerRepository.getPlayerByUuid(target.getUniqueId());

                assert mp != null : args[1] + " is somehow not available. Unable to kick them!";
                mp.setKicks(mp.getKicks() + 1);
                target.kickPlayer(Placeholders.setKickPlaceholders(messages().get("kick-message"), reason, sender));

                if (sender instanceof Player) LogManager.addLog((Player) sender, "Kick", reason, target.getName(), "None");

                sender.sendMessage(ChatColor.WHITE + "Successfully kicked " + target.getName());
                for (Player player : Bukkit.getOnlinePlayers()) if (player.hasPermission("punishments.staff-broadcasts")) player.sendMessage(Placeholders.setKickPlaceholders(messages().get("kick-broadcast-message") + ChatColor.WHITE + " [SILENT]", reason, sender, args[1]));
            } else Bukkit.dispatchCommand(sender, "punishments help kick");
        } else Bukkit.dispatchCommand(sender, "punishments help kick");
    }

}