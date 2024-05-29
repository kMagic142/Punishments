package ro.kmagic.punishments;

import org.bukkit.plugin.java.JavaPlugin;
import ro.kmagic.punishments.handlers.CommandHandler;
import ro.kmagic.punishments.handlers.MainCommandHandler;
import ro.kmagic.punishments.handlers.TabCompleteHandler;
import ro.kmagic.punishments.listeners.PlayerChatListener;
import ro.kmagic.punishments.listeners.PlayerJoinListener;
import ro.kmagic.punishments.listeners.PlayerQuitListener;
import ro.kmagic.punishments.util.ChatUtils;
import ro.kmagic.punishments.util.Configuration;
import ro.kmagic.punishments.util.DatabaseHandler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import ro.kmagic.punishments.util.subcommands.SubCommandHandler;

import java.sql.SQLException;
import java.util.*;

@SuppressWarnings({"UnusedAssignment", "ConstantConditions"})
public final class Punishments extends JavaPlugin {

    @Getter
    private static Punishments plugin;

    @Getter
    private SubCommandHandler commandHandler;
    @Getter @Setter
    private Configuration configuration;
    @Getter
    private DatabaseHandler database;

    @Getter
    private HashMap<String, String> messages;

    @Getter
    private HashMap<String, String> databaseInformation;

    public static String tablePrefix;

    public static List<String> mainCommands;

    @Override
    public void onEnable() {
        boolean loaded = false;

        plugin = this;

        registerListeners();
        registerCommands();

        if (!loadConfiguration()) return; else loaded = true;
        if (!loadMessages()) return; else loaded = true;
        if (!loadStorage()) return; else loaded = true;
        if (!loadData()) {
        } else loaded = true;
    }

    public void registerListeners() {
        sendMessage("Registering listeners...");

        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
    }

    public void registerCommands() {
        sendMessage("Registering commands...");

        // Register main command
        PluginCommand punishments = getCommand("punishments");
        if (punishments == null) {
            sendMessage(ChatColor.RED + " - Could not get main command /punishments registered with the server!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        commandHandler = new CommandHandler();
        punishments.setExecutor(commandHandler);
        punishments.setTabCompleter(new TabCompleteHandler());

        // Register non-subcommands

        mainCommands = new ArrayList<>();

        mainCommands.add("ban");
        mainCommands.add("unban");
        mainCommands.add("mute");
        mainCommands.add("unmute");
        mainCommands.add("kick");
        mainCommands.add("blacklist");
        mainCommands.add("unblacklist");

        getCommand("ban").setExecutor(new MainCommandHandler());
        getCommand("ban").setTabCompleter(new TabCompleteHandler());
        getCommand("unban").setExecutor(new MainCommandHandler());
        getCommand("unban").setTabCompleter(new TabCompleteHandler());

        getCommand("mute").setExecutor(new MainCommandHandler());
        getCommand("mute").setTabCompleter(new TabCompleteHandler());
        getCommand("unmute").setExecutor(new MainCommandHandler());
        getCommand("unmute").setTabCompleter(new TabCompleteHandler());

        getCommand("kick").setExecutor(new MainCommandHandler());
        getCommand("kick").setTabCompleter(new TabCompleteHandler());

        getCommand("blacklist").setExecutor(new MainCommandHandler());
        getCommand("blacklist").setTabCompleter(new TabCompleteHandler());
        getCommand("unblacklist").setExecutor(new MainCommandHandler());
        getCommand("unblacklist").setTabCompleter(new TabCompleteHandler());
    }

    private boolean loadConfiguration() {
        sendMessage("Loading configuration...");
        saveDefaultConfig();

        configuration = new Configuration(this, "config.yml");
        return true;
    }

    private boolean loadMessages() {
        sendMessage("Loading messages...");

        messages = new HashMap<>();

        addMessage("ban-message");
        addMessage("perm-ban-message");

        addMessage("ban-broadcast-message");
        addMessage("perm-ban-broadcast-message");
        addMessage("unban-broadcast-message");

        addMessage("blacklist-message");
        addMessage("blacklist-broadcast-message");
        addMessage("unblacklist-broadcast-message");

        addMessage("mute-message");
        addMessage("perm-mute-message");

        addMessage("mute-broadcast-message");
        addMessage("perm-mute-broadcast-message");
        addMessage("unmute-broadcast-message");

        addMessage("kick-message");

        addMessage("only-players");
        addMessage("no-permission");
        addMessage("unknown-command");
        addMessage("unknown-player");

        return true;
    }

    private void addMessage(String message) {
        messages.put(message, ChatUtils.colorize(configuration.getString("messages.punishments." + message)));
    }

    private boolean loadStorage() {
        sendMessage("Loading storage provider...");

        loadDatabaseInformation();

        database = new DatabaseHandler(databaseInformation.get("database"), databaseInformation.get("host"), databaseInformation.get("port"), databaseInformation.get("username"), databaseInformation.get("password"), databaseInformation.get("table-prefix"));

        sendMessage("Creating connection to database...");
        try {
            database.createConnection();
            sendMessage(" - Table prefix: " + databaseInformation.get("table-prefix"));
            sendMessage(" - Successfully connected to database!");
        } catch (SQLException e) {
            e.printStackTrace();
            sendMessage(" - An error occurred while trying to connect to the database! Check the config.yml and restart your server.");
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }

        return true;
    }

    private void loadDatabaseInformation() {
        databaseInformation = new HashMap<>();

        databaseInformation.put("database", configuration.getString("database.database"));
        databaseInformation.put("host", configuration.getString("database.host"));
        databaseInformation.put("port", configuration.getString("database.port"));

        databaseInformation.put("username", configuration.getString("database.username"));
        databaseInformation.put("password", configuration.getString("database.password"));

        databaseInformation.put("table-prefix", configuration.getString("database.table-prefix"));
    }

    private boolean loadData() {
        sendMessage( "Performing initial data load...");

        database.createBannedIpsTable();
        database.createPlayersTable();
        database.createMutesTable();
        database.createBansTable();
        database.createLogsTable();
        database.createBlacklistsTable();

        return true;
    }

    @Override
    public void onDisable() {}

    public void reload() {
        onDisable();
        onEnable();
    }

    public void sendMessage(String msg) {
        getServer().getConsoleSender().sendMessage(msg);
    }

}
