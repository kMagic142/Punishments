package ro.kmagic.punishments.handlers;

import ro.kmagic.punishments.objects.blacklist.Blacklist;
import ro.kmagic.punishments.objects.blacklist.BlacklistRepository;
import ro.kmagic.punishments.util.subcommands.SubCommand;
import org.jetbrains.annotations.NotNull;
import ro.kmagic.punishments.objects.ban.Ban;
import ro.kmagic.punishments.objects.ban.BanRepository;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

public class TabCompleteHandler implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String a, @NotNull String[] args) {
        List<SubCommand> subcommands = new CommandHandler().getSubcommands();
        List<String> tab = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("punishments")) {
            switch (args.length) {
                case 1:
                    boolean perms = false;
                    for (SubCommand subcommand : new CommandHandler().getSubcommands())
                        if (sender.hasPermission(subcommand.permission())) perms = true;
                    if (perms) for (SubCommand subcommand : subcommands) tab.add(subcommand.name());
                    break;
                case 2:
                    switch (args[0]) {
                        case "help":
                            for (SubCommand subcommand : subcommands) tab.add(subcommand.name());
                            break;
                        case "reload":
                        case "unban":
                            if (BanRepository.getAllBans().isEmpty()) {
                                tab.add("No one is currently banned!");
                                break;
                            }
                            for (Ban ban : BanRepository.getAllBans()) {
                                OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(ban.getUuid()));
                                tab.add(target.getName());
                            }
                            break;
                        case "unblacklist":
                            if (BlacklistRepository.getAllBlacklists().isEmpty()) {
                                tab.add("No one is currently blacklisted!");
                                break;
                            }
                            for (Blacklist blacklist : BlacklistRepository.getAllBlacklists()) {
                                OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(blacklist.getUuid()));
                                tab.add(target.getName());
                            }
                            break;
                        case "ban":
                        case "blacklist":
                        case "mute":
                        case "kick":
                            if (args[1].isEmpty())
                                for (Player player : Bukkit.getOnlinePlayers()) tab.add(player.getName());
                            else for (Player player : Bukkit.getOnlinePlayers())
                                if (player.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                                    tab.add(player.getName());
                            tab.add("-s");
                            break;
                        default:
                            for (SubCommand subcommand : subcommands)
                                if (subcommand.tabcompletion() != null)
                                    if (subcommand.name().equals(args[0])) tab.addAll(subcommand.tabcompletion());
                                    else
                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                            tab.add(player.getName());
                                        }
                            break;
                    }
                    break;
                case 3:
                    if (args[0].equals("ban") || args[0].equals("mute")) {
                        if (args[1].equals("-s")) {
                            if (args[2].isEmpty())
                                for (Player player : Bukkit.getOnlinePlayers()) tab.add(player.getName());
                            else for (Player player : Bukkit.getOnlinePlayers())
                                if (player.getName().toLowerCase().startsWith(args[2].toLowerCase()))
                                    tab.add(player.getName());
                            break;
                        }
                        if (args[2].isEmpty()) break;
                        if (args[2].contains("s") || args[2].contains("m") || args[2].contains("h") || args[2].contains("d") || args[2].contains("y"))
                            break;
                        tab.add(args[2] + "s");
                        tab.add(args[2] + "m");
                        tab.add(args[2] + "h");
                        tab.add(args[2] + "d");
                        tab.add(args[2] + "y");
                        break;
                    } else if (args[0].equals("kick") && args[1].equals("-s")) {
                        if (args[2].isEmpty())
                            for (Player player : Bukkit.getOnlinePlayers()) tab.add(player.getName());
                        else for (Player player : Bukkit.getOnlinePlayers())
                            if (player.getName().toLowerCase().startsWith(args[2].toLowerCase()))
                                tab.add(player.getName());
                        break;
                    } else if(args[0].equals("blacklist")) {
                        if (args[1].equals("-s")) {
                            if (args[2].isEmpty())
                                for (Player player : Bukkit.getOnlinePlayers()) tab.add(player.getName());
                            else for (Player player : Bukkit.getOnlinePlayers())
                                if (player.getName().toLowerCase().startsWith(args[2].toLowerCase()))
                                    tab.add(player.getName());
                            break;
                        }
                        if (args[2].isEmpty()) break;
                        break;
                    }
                    break;
                case 4:
                    if (args[0].equals("ban") || args[0].equals("mute")) {
                        if (!args[1].equals("-s")) break;
                        if (args[3].isEmpty()) break;
                        if (args[3].contains("s") || args[3].contains("m") || args[3].contains("h") || args[3].contains("d") || args[3].contains("y"))
                            break;
                        tab.add(args[3] + "s");
                        tab.add(args[3] + "m");
                        tab.add(args[3] + "h");
                        tab.add(args[3] + "d");
                        tab.add(args[3] + "y");
                    } else if (args[0].equals("blacklist")) {
                        if (!args[1].equals("-s")) break;
                        if (args[2].isEmpty()) break;
                    }
                    break;
            }
        } else {
            switch (args.length) {
                case 1:
                    switch (cmd.getName()) {
                        case "help":
                            for (SubCommand subcommand : subcommands) tab.add(subcommand.name());
                            break;
                        case "reload":
                        case "unban":
                            if (BanRepository.getAllBans().isEmpty()) {
                                tab.add("No one is currently banned!");
                                break;
                            }
                            for (Ban ban : BanRepository.getAllBans()) {
                                OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(ban.getUuid()));
                                tab.add(target.getName());
                            }
                            break;
                        case "unblacklist":
                            if (BlacklistRepository.getAllBlacklists().isEmpty()) {
                                tab.add("No one is currently blacklisted!");
                                break;
                            }
                            for (Blacklist blacklist : BlacklistRepository.getAllBlacklists()) {
                                OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(blacklist.getUuid()));
                                tab.add(target.getName());
                            }
                            break;
                        case "ban":
                        case "blacklist":
                        case "mute":
                        case "kick":
                            if (args[0].isEmpty())
                                for (Player player : Bukkit.getOnlinePlayers()) tab.add(player.getName());
                            else for (Player player : Bukkit.getOnlinePlayers())
                                if (player.getName().toLowerCase().startsWith(cmd.getName().toLowerCase()))
                                    tab.add(player.getName());
                            tab.add("-s");
                            break;
                        default:
                            for (SubCommand subcommand : subcommands)
                                if (subcommand.tabcompletion() != null)
                                    if (subcommand.name().equals(cmd.getName())) tab.addAll(subcommand.tabcompletion());
                                    else
                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                            tab.add(player.getName());
                                        }
                            break;
                    }
                    break;
                case 2:
                    if (cmd.getName().equals("ban") || cmd.getName().equals("mute")) {
                        if (args[0].equals("-s")) {
                            if (args[1].isEmpty())
                                for (Player player : Bukkit.getOnlinePlayers()) tab.add(player.getName());
                            else for (Player player : Bukkit.getOnlinePlayers())
                                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                                    tab.add(player.getName());
                            break;
                        }
                        if (args[1].isEmpty()) break;
                        if (args[1].contains("s") || args[1].contains("m") || args[1].contains("h") || args[1].contains("d") || args[1].contains("y"))
                            break;
                        tab.add(args[1] + "s");
                        tab.add(args[1] + "m");
                        tab.add(args[1] + "h");
                        tab.add(args[1] + "d");
                        tab.add(args[1] + "y");
                        break;
                    } else if (cmd.getName().equals("kick") && args[0].equals("-s")) {
                        if (args[1].isEmpty())
                            for (Player player : Bukkit.getOnlinePlayers()) tab.add(player.getName());
                        else for (Player player : Bukkit.getOnlinePlayers())
                            if (player.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                                tab.add(player.getName());
                        break;
                    } else if(cmd.getName().equals("blacklist")) {
                        if (args[0].equals("-s")) {
                            if (args[1].isEmpty())
                                for (Player player : Bukkit.getOnlinePlayers()) tab.add(player.getName());
                            else for (Player player : Bukkit.getOnlinePlayers())
                                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                                    tab.add(player.getName());
                            break;
                        }
                        if (args[1].isEmpty()) break;
                        break;
                    }
                    break;
                case 4:
                    if (cmd.getName().equals("ban") || cmd.getName().equals("mute")) {
                        if (!args[0].equals("-s")) break;
                        if (args[2].isEmpty()) break;
                        if (args[2].contains("s") || args[2].contains("m") || args[2].contains("h") || args[2].contains("d") || args[2].contains("y"))
                            break;
                        tab.add(args[2] + "s");
                        tab.add(args[2] + "m");
                        tab.add(args[2] + "h");
                        tab.add(args[2] + "d");
                        tab.add(args[2] + "y");
                    } else if(cmd.getName().equals("blacklist")) {
                        if (!args[0].equals("-s")) break;
                        if (args[2].isEmpty()) break;
                    }
                    break;
            }
        }
        return tab;
    }

}
