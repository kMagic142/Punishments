package ro.kmagic.punishments.commands.core.ban;

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
import java.util.Objects;
import java.util.UUID;

@Usage(usage = "[-s] <player>")
@SuppressWarnings("ConstantConditions")
@Description(description = "Unban a player.")
public class UnbanCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (unban(sender, args, false)) return;

            for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage(messages().get("unban-broadcast-message").replaceAll("%admin%", sender.getName()).replaceAll("%player%", args[0]));
        } else if (args.length == 2) {
            if (!args[0].equals("-s")) { Bukkit.dispatchCommand(sender, "punishments help unban"); return; }

            if (unban(sender, args, true)) return;
            for (Player p : Bukkit.getOnlinePlayers()) if (p.hasPermission("punishments.staff-broadcasts")) p.sendMessage((messages().get("unban-broadcast-message") + ChatColor.WHITE + " [SILENT]").replaceAll("%admin%", sender.getName()).replaceAll("%player%", args[1]));
        } else { Bukkit.dispatchCommand(sender, "punishments help unban"); }
    }

    private boolean unban(CommandSender sender, String[] args, boolean silent) {
        boolean banned;

        if (silent) banned = checkBanned(args[1]); else banned = checkBanned(args[0]);

        if (!banned) {
            sender.sendMessage(ChatColor.RED + "That player is not currently banned!");
            return true;
        }

        MinecraftPlayer mp;

        if (silent) mp = MinecraftPlayerRepository.getPlayerByUuid(Objects.requireNonNull(MojangAPI.getUuidByName(args[1]))); else mp = MinecraftPlayerRepository.getPlayerByUuid(Objects.requireNonNull(MojangAPI.getUuidByName(args[0])));

        if (silent) {
            database().execute("DELETE FROM " + Punishments.tablePrefix + "bans where uuid='" + UUID.fromString(MojangAPI.getUuidByName(args[1])) + "'");
            database().execute("DELETE FROM " + Punishments.tablePrefix + "players where uuid='" + UUID.fromString(MojangAPI.getUuidByName(args[1])) + "'");
            database().insertPlayer(mp.getUuid(), mp.getUsername(), false, mp.getBanexpirydate(), mp.getMuted(), mp.getMuteexpirydate(), mp.getWarns(), mp.getKicks(), mp.getBlacklisted());
            database().execute("DELETE FROM " + Punishments.tablePrefix + "banned_ips where uuid='" + UUID.fromString(MojangAPI.getUuidByName(args[1])) + "'");
            if (sender instanceof Player) LogManager.addLog((Player) sender, "Unban", "None", args[1], "None");
        } else {
            database().execute("DELETE FROM " + Punishments.tablePrefix + "bans where uuid='" + UUID.fromString(MojangAPI.getUuidByName(args[0])) + "'");
            database().execute("DELETE FROM " + Punishments.tablePrefix + "players where uuid='" + UUID.fromString(MojangAPI.getUuidByName(args[0])) + "'");
            database().insertPlayer(mp.getUuid(), mp.getUsername(), false, mp.getBanexpirydate(), mp.getMuted(), mp.getMuteexpirydate(), mp.getWarns(), mp.getKicks(), mp.getBlacklisted());
            database().execute("DELETE FROM " + Punishments.tablePrefix + "banned_ips where uuid='" + UUID.fromString(MojangAPI.getUuidByName(args[0])) + "'");
            if (sender instanceof Player) LogManager.addLog((Player) sender, "Unban", "None", args[0], "None");
        }

        if (!silent) sender.sendMessage(ChatColor.WHITE + "Successfully unbanned " + args[0] + "!"); else sender.sendMessage(ChatColor.WHITE + "Successfully unbanned " + args[1] + "!");
        return false;
    }

    public boolean checkBanned(String name) {
        ResultSet set = database().query("SELECT username FROM " + Punishments.tablePrefix + "bans");

        boolean isBanned = false;

        try {
            while (set.next()) {
                if (set.getString("username").equalsIgnoreCase(name)) isBanned = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isBanned;
    }

}
