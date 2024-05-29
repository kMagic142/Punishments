package ro.kmagic.punishments.commands.miscellaneous;

import ro.kmagic.punishments.Punishments;
import ro.kmagic.punishments.objects.command.Description;
import ro.kmagic.punishments.objects.command.SubCommand;
import ro.kmagic.punishments.objects.command.Usage;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

@Description(description = "Displays all commands and their usages.")
@Usage(usage = "help OR /punishments help <command>")
public class HelpCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        List<ro.kmagic.punishments.util.subcommands.SubCommand> subcommands = Punishments.getPlugin().getCommandHandler().getSubcommands();
        if (args.length == 0) {
            for (ro.kmagic.punishments.util.subcommands.SubCommand subcommand : subcommands) {
                String[] usages = subcommand.usage().split(" OR ");
                for (String usage : usages) sender.sendMessage(ChatColor.RED + " > " + ChatColor.WHITE + usage);
            }
        } else {
            for (ro.kmagic.punishments.util.subcommands.SubCommand subcommand : subcommands) {
                if (args[0].equals(subcommand.name())) {
                    sender.sendMessage(ChatColor.RED + StringUtils.capitalize(subcommand.name()) + ChatColor.WHITE + " (" + subcommand.permission() + "):");
                    String[] usages = subcommand.usage().split(" OR ");
                    sender.sendMessage(ChatColor.RED + " > " + ChatColor.WHITE + subcommand.description());
                    for (String usage : usages) sender.sendMessage(ChatColor.RED + " > " + ChatColor.WHITE + usage);
                }
            }
        }
    }
}
