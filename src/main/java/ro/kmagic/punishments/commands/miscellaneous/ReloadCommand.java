package ro.kmagic.punishments.commands.miscellaneous;

import ro.kmagic.punishments.Punishments;
import ro.kmagic.punishments.objects.command.Description;
import ro.kmagic.punishments.objects.command.SubCommand;
import ro.kmagic.punishments.objects.command.Usage;
import org.bukkit.command.CommandSender;

import java.util.Timer;
import java.util.TimerTask;

@Usage(usage = "reload")
@Description(description = "Reloads the punishments plugin.")
public class ReloadCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        Punishments.getPlugin().reload();
        sender.sendMessage("Successfully reloaded Punishments.");
    }
}
