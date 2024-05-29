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
@Description(description = "Clears the log of a player.")
public class ClearlogCommand implements SubCommand {

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

            LogRepository.clearLogsByUuid(uuid);

            sender.sendMessage("Successfully cleared " + args[0] + "'s logs!");
        } else Bukkit.dispatchCommand(sender, "punishments help clearlog");
    }
}
