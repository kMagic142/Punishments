package ro.kmagic.punishments.handlers;


import net.md_5.bungee.api.ChatColor;
import ro.kmagic.punishments.Punishments;
import ro.kmagic.punishments.commands.core.ban.BanCommand;
import ro.kmagic.punishments.commands.core.ban.UnbanCommand;
import ro.kmagic.punishments.commands.core.blacklist.BlacklistCommand;
import ro.kmagic.punishments.commands.core.blacklist.UnblacklistCommand;
import ro.kmagic.punishments.commands.core.kick.KickCommand;
import ro.kmagic.punishments.commands.core.mute.MuteCommand;
import ro.kmagic.punishments.commands.core.mute.UnmuteCommand;
import ro.kmagic.punishments.commands.miscellaneous.*;
import ro.kmagic.punishments.util.subcommands.SubCommand;
import ro.kmagic.punishments.util.subcommands.SubCommandHandler;

import java.util.List;

public class CommandHandler extends SubCommandHandler {

    public CommandHandler() {
        super(null, null, null, null);
        setOnlyPlayers(() -> getSender().sendMessage(Punishments.getPlugin().getMessages().get("only-players")));
        setNoArguments(() -> {
            String message = "";
            boolean permission = false;
            for (SubCommand subcommand : new CommandHandler().getSubcommands()) if (getSender().hasPermission(subcommand.permission())) permission = true;
            if (permission) message = message.concat(ChatColor.RED + " > " + ChatColor.WHITE + "Type /punishments help for more info."); else message = message.concat(ChatColor.RED + " > " + ChatColor.WHITE + "You do not have permission to use any sub-commands.");
            getSender().sendMessage(message);
        });
        setNoPermission(() -> getSender().sendMessage(Punishments.getPlugin().getMessages().get("no-permission")));
        setSubComandNotFound(() -> getSender().sendMessage(Punishments.getPlugin().getMessages().get("unknown-command")));

        List<SubCommand> subCommands = getSubcommands();

        subCommands.add(new ClearlogCommand());
        subCommands.add(new ReloadCommand());
        subCommands.add(new UnmuteCommand());
        subCommands.add(new UnbanCommand());
        subCommands.add(new KickCommand());
        subCommands.add(new MuteCommand());
        subCommands.add(new HelpCommand());
        subCommands.add(new BanCommand());
        subCommands.add(new LogCommand());
        subCommands.add(new BlacklistCommand());
        subCommands.add(new UnblacklistCommand());

        setSubcommands(subCommands);
    }

}
