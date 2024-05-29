package ro.kmagic.punishments.commands.core.mute;

import ro.kmagic.punishments.Punishments;
import ro.kmagic.punishments.objects.command.Description;
import ro.kmagic.punishments.objects.command.SubCommand;
import ro.kmagic.punishments.objects.command.Usage;
import ro.kmagic.punishments.objects.minecraftplayer.MinecraftPlayer;
import ro.kmagic.punishments.objects.minecraftplayer.MinecraftPlayerRepository;
import ro.kmagic.punishments.util.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.kmagic.punishments.util.MojangAPI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;

@Usage(usage = "[-s] <player>")
@SuppressWarnings("ConstantConditions")
@Description(description = "Unmute a player.")
public class UnmuteCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 0) {
            boolean silent = args[0].equals("-s");

            if (args.length == 1 && !silent) {
                boolean muted = checkMuted(args[0]);

                if (!muted) {
                    sender.sendMessage(ChatColor.RED + "That player is not currently muted!");
                    return;
                }

                if (Bukkit.getPlayer(args[0]).hasPermission("punishments.punishment-immune")) {
                    sender.sendMessage( ChatColor.RED + "You cannot " + name() + " that player!");
                    return;
                }

                database().execute("DELETE FROM " + Punishments.tablePrefix + "mutes WHERE uuid='" + MojangAPI.getUuidByName(args[0]) + "'");

                MinecraftPlayer mp = MinecraftPlayerRepository.getPlayerByUuid(Objects.requireNonNull(MojangAPI.getUuidByName(args[0])));

                mp.setMuted(false);
                mp.setMuteexpirydate(new Timestamp(System.currentTimeMillis()));

                if (sender instanceof Player) LogManager.addLog((Player) sender, "Unmute", "None", args[0], "None");

                sender.sendMessage(ChatColor.WHITE + "Successfully unmuted " + args[0] + "!");
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(messages().get("unmute-broadcast-message").replaceAll("%admin%", sender.getName()).replaceAll("%player%", args[0]));
                }
            } else if (args.length == 2 && silent) {
                boolean muted = checkMuted(args[1]);

                if (!muted) {
                    sender.sendMessage(ChatColor.RED + "That player is not currently muted!");
                    return;
                }

                if (Bukkit.getPlayer(args[1]).hasPermission("punishments.punishment-immune")) {
                    sender.sendMessage(ChatColor.RED + "You cannot " + name() + " that player!");
                    return;
                }

                database().execute("DELETE FROM " + Punishments.tablePrefix + "mutes WHERE uuid='" + MojangAPI.getUuidByName(args[1]) + "'");

                MinecraftPlayer mp = MinecraftPlayerRepository.getPlayerByUuid(Objects.requireNonNull(MojangAPI.getUuidByName(args[1])));

                mp.setMuted(false);
                mp.setMuteexpirydate(new Timestamp(System.currentTimeMillis()));

                if (sender instanceof Player) LogManager.addLog((Player) sender, "Unmute", "None", args[1], "None");

                sender.sendMessage(ChatColor.WHITE + "Successfully unmuted " + args[1] + "!");
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("punishments.staff-broadcasts")) player.sendMessage((messages().get("unmute-broadcast-message") + ChatColor.WHITE + " [SILENT]").replaceAll("%admin%", sender.getName()).replaceAll("%player%", args[1]));
                }
            } else Bukkit.dispatchCommand(sender, "punishments help unmute");
        } else Bukkit.dispatchCommand(sender, "punishments help unmute");
    }

    public boolean checkMuted(String name) {
        ResultSet set = database().query("SELECT username FROM " + Punishments.tablePrefix + "mutes");

        boolean muted = false;

        try {
            while (set.next()) {
                if (set.getString("username").equalsIgnoreCase(name)) muted = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return muted;
    }

}
