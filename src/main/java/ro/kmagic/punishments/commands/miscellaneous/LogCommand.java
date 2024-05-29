package ro.kmagic.punishments.commands.miscellaneous;

import ro.kmagic.punishments.objects.command.Description;
import ro.kmagic.punishments.objects.command.SubCommand;
import ro.kmagic.punishments.objects.command.Usage;
import ro.kmagic.punishments.objects.log.Log;
import ro.kmagic.punishments.objects.log.LogRepository;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import ro.kmagic.punishments.util.MojangAPI;

import java.util.List;
import java.util.Objects;

@Usage(usage = "<player>")
@Description(description = "View all logs for a staff member. This will show what commands they've been using.")
public class LogCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            String uuid = MojangAPI.getUuidByName(args[0]);

            if (uuid == null) {
                sender.sendMessage(ChatColor.RED + "Could not find that player!");
                return;
            }
            List<Log> logs = LogRepository.getLogsByUuid(uuid);

            if (Objects.requireNonNull(logs).isEmpty()) {
                sender.sendMessage(ChatColor.RED + "That player does not have any logs yet!");
                return;
            }

            String message = "";

            message = message.concat(args[0] + "'s logs:\n" + ChatColor.RED + " > " + ChatColor.WHITE + "Format: Type, Player, Date\n");

            int ln = 0;

            for (Log log : logs) {
                ln++;
                message = message.concat(ChatColor.RED + "  " + ln + ". " + ChatColor.DARK_RED + log.getType() + " " + ChatColor.WHITE + log.getTarget() + " " + log.getDate() + "\n");
            }

            message = message.concat(ChatColor.WHITE + "END LOG");

            sender.sendMessage(message);
        } else Bukkit.dispatchCommand(sender, "punishments help log");
    }
}
