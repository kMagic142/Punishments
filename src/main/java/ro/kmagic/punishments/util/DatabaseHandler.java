package ro.kmagic.punishments.util;

import ro.kmagic.punishments.Punishments;
import ro.kmagic.punishments.enums.BanType;
import ro.kmagic.punishments.enums.MuteType;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.Objects;
import java.util.UUID;

public class DatabaseHandler extends DatabaseManipulator {

    @Getter
    public final String tablePrefix;

    public DatabaseHandler(String database, String host, String port, String username, String password, String tablePrefix) {
        super(database, host, port, username, password, false);

        this.tablePrefix = tablePrefix;
        Punishments.tablePrefix = tablePrefix;
    }

    public void createPlayersTable() {
        sendMessage(" - Creating table \"" + tablePrefix + "players\"...");
        try {
            DatabaseMetaData meta = getConnection().getMetaData();
            ResultSet set = meta.getTables(null, null, tablePrefix + "players", null);
            if (set.next()) sendMessage(" - Table \"" + tablePrefix + "players\" already exists!"); else {
                sendMessage(" - Table \"" + tablePrefix + "players\" does not exist, creating it now!");

                String sql = "CREATE TABLE `" + getDatabase() + "`.`" + tablePrefix + "players` (" +
                        "  `uuid` VARCHAR(100) NOT NULL," +
                        "  `username` VARCHAR(100) NOT NULL," +
                        "  `banned` VARCHAR(100) NOT NULL," +
                        "  `banexpirydate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                        "  `muted` VARCHAR(100) NOT NULL," +
                        "  `muteexpirydate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                        "  `warns` INT NOT NULL," +
                        "  `kicks` INT NOT NULL," +
                        "  `blacklisted` VARCHAR(100) NOT NULL, " +
                        "  PRIMARY KEY (`uuid`, `username`)," +
                        "  UNIQUE INDEX `uuid_UNIQUE` (`uuid` ASC) VISIBLE);";
                execute(sql);
            }
        } catch (SQLException e) {
            sendMessage(ChatColor.RED + " - An error occurred while trying to create table \"" + tablePrefix + "players\"");
        }
    }

    public void createBansTable() {
        sendMessage(" - Creating table \"" + tablePrefix + "bans\"...");
        try {
            DatabaseMetaData meta = getConnection().getMetaData();
            ResultSet set = meta.getTables(null, null, tablePrefix + "bans", null);
            if (set.next()) sendMessage(" - Table \"" + tablePrefix + "bans\" already exists!"); else {
                sendMessage(" - Table \"" + tablePrefix + "bans\" does not exist, creating it now!");

                String sql = "CREATE TABLE `" + getDatabase() + "`.`" + tablePrefix + "bans` (" +
                        "  `uuid` VARCHAR(100) NOT NULL," +
                        "  `username` VARCHAR(100) NOT NULL," +
                        "  `ban-type` VARCHAR(100) NOT NULL," +
                        "  `reason` VARCHAR(100) NOT NULL," +
                        "  `admin` VARCHAR(100) NOT NULL," +
                        "  `id` INT NOT NULL," +
                        "  PRIMARY KEY (`uuid`)," +
                        "  UNIQUE INDEX `uuid_UNIQUE` (`uuid` ASC) VISIBLE," +
                        "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE" +
                        ");";
                execute(sql);
            }
        } catch (SQLException e) {
            sendMessage(ChatColor.RED + " - An error occurred while trying to create table \"" + tablePrefix + "bans\"");
        }
    }

    public void createBlacklistsTable() {
        sendMessage(" - Creating table \"" + tablePrefix + "blacklists\"...");
        try {
            DatabaseMetaData meta = getConnection().getMetaData();
            ResultSet set = meta.getTables(null, null, tablePrefix + "blacklists", null);
            if (set.next()) sendMessage(" - Table \"" + tablePrefix + "blacklists\" already exists!"); else {
                sendMessage(" - Table \"" + tablePrefix + "blacklists\" does not exist, creating it now!");

                String sql = "CREATE TABLE `" + getDatabase() + "`.`" + tablePrefix + "blacklists` (" +
                        "  `uuid` VARCHAR(100) NOT NULL," +
                        "  `username` VARCHAR(100) NOT NULL," +
                        "  `reason` VARCHAR(100) NOT NULL," +
                        "  `admin` VARCHAR(100) NOT NULL," +
                        "  `id` INT NOT NULL," +
                        "  PRIMARY KEY (`uuid`)," +
                        "  UNIQUE INDEX `uuid_UNIQUE` (`uuid` ASC) VISIBLE," +
                        "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE" +
                        ");";
                execute(sql);
            }
        } catch (SQLException e) {
            sendMessage(ChatColor.RED + " - An error occurred while trying to create table \"" + tablePrefix + "blacklists\"");
        }
    }

    public void createMutesTable() {
        sendMessage(" - Creating table \"" + tablePrefix + "mutes\"...");
        try {
            DatabaseMetaData meta = getConnection().getMetaData();
            ResultSet set = meta.getTables(null, null, tablePrefix + "mutes", null);
            if (set.next()) sendMessage(" - Table \"" + tablePrefix + "mutes\" already exists!"); else {
                sendMessage(" - Table \"" + tablePrefix + "mutes\" does not exist, creating it now!");

                String sql = "CREATE TABLE `" + getDatabase() + "`.`" + tablePrefix + "mutes` (" +
                        "  `uuid` VARCHAR(100) NOT NULL," +
                        "  `username` VARCHAR(100) NOT NULL," +
                        "  `mute-type` VARCHAR(100) NOT NULL," +
                        "  `reason` VARCHAR(100) NOT NULL," +
                        "  `admin` VARCHAR(45) NOT NULL," +
                        "  `id` INT NOT NULL," +
                        "  PRIMARY KEY (`uuid`))";
                execute(sql);
            }
        } catch (SQLException e) {
            sendMessage(ChatColor.RED + " - An error occurred while trying to create table \"" + tablePrefix + "\"");
        }
    }

    public void createBannedIpsTable() {
        sendMessage(" - Creating table \"" + tablePrefix + "banned_ips\"");
        try {
            DatabaseMetaData  meta = getConnection().getMetaData();
            ResultSet set = meta.getTables(null, null, tablePrefix + "banned_ips", null);
            if (set.next()) sendMessage(" - Table \"" + tablePrefix + "banned_ips\" already exists!"); else {
                sendMessage(" - Table \"" + tablePrefix + "banned_ips\" does not exist, creating it now!");

                String sql = "CREATE TABLE `" + getDatabase() + "`.`" + tablePrefix + "banned_ips` (" +
                        "  `uuid` VARCHAR(100) NOT NULL," +
                        "  `ip` VARCHAR(100) NOT NULL)";

                execute(sql);
            }
        } catch (SQLException e) {
            sendMessage(ChatColor.RED + " - An error occurred while trying to create table \"" + tablePrefix + "banned_ips\"");
        }
    }

    public void createLogsTable() {
        sendMessage(" - Creating table \"" + tablePrefix + "logs\"");
        try {
            DatabaseMetaData  meta = getConnection().getMetaData();
            ResultSet set = meta.getTables(null, null, tablePrefix + "logs", null);
            if (set.next()) sendMessage(" - Table \"" + tablePrefix + "logs\" already exists!"); else {
                sendMessage(" - Table \"" + tablePrefix + "logs\" does not exist, creating it now!");

                String sql = "CREATE TABLE `" + getDatabase() + "`.`" + tablePrefix + "logs` (" +
                        "  `uuid` VARCHAR(100) NOT NULL," +
                        "  `type` VARCHAR(45) NOT NULL," +
                        "  `reason` VARCHAR(300) NOT NULL," +
                        "  `target` VARCHAR(45) NOT NULL," +
                        "  `username` VARCHAR(45) NOT NULL," +
                        "  `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                        "  `time` VARCHAR(45) NOT NULL);";

                execute(sql);
            }
        } catch (SQLException e) {
            sendMessage( ChatColor.RED + " - An error occurred while trying to create table \"" + tablePrefix + "logs\"");
        }
    }

    public void insertNewPlayer(UUID uuid, String username) {
        String sql = "INSERT INTO" +
                " `" + getDatabase() + "`.`" + tablePrefix + "players`(" +
                "`uuid`," +
                "`username`," +
                "`banned`," +
                "`banexpirydate`," +
                "`muted`," +
                "`muteexpirydate`," +
                "`warns`," +
                "`kicks`, " +
                "`blacklisted`" +
                ")" +
                "VALUES(" +
                "'" + uuid.toString() + "'," +
                "'" + username + "'," +
                "'false','" +
                new Timestamp(System.currentTimeMillis()) + "'," +
                "'false','" +
                new Timestamp(System.currentTimeMillis()) + "'," +
                "'0'," +
                "'0'," +
                "'false'" +
                ");";
        execute(sql);
    }

    public void insertPlayer(String uuid, String username, boolean banned, Timestamp banexpirydate, boolean muted, Timestamp muteexpirydate, int warns, int kicks, boolean blacklisted) {
        String sql = "INSERT INTO" +
                " `" + getDatabase() + "`.`" + tablePrefix + "players`(" +
                "`uuid`," +
                "`username`," +
                "`banned`," +
                "`banexpirydate`," +
                "`muted`," +
                "`muteexpirydate`," +
                "`warns`," +
                "`kicks`, " +
                "`blacklisted`" +
                ")" +
                "VALUES(" +
                "'" + uuid + "'," +
                "'" + username + "'," +
                "'" + banned + "','" +
                banexpirydate + "'," +
                "'" + muted + "','" +
                muteexpirydate + "'," +
                "'" + warns + "'," +
                "'" + kicks + "'," +
                "'" + blacklisted + "'" +
                ");";
        execute(sql);
    }

    public void insertNewBan(String uuid, String username, BanType type, String reason, String admin, int id) {
        String sql = "INSERT INTO `" + getDatabase() + "`.`" + tablePrefix + "bans`" +
                "(`uuid`," +
                "`username`," +
                "`ban-type`," +
                "`reason`," +
                "`admin`," +
                "`id`)" +
                "VALUES(" +
                "'" + uuid + "'," +
                "'" + username + "'," +
                "'" + type.getType() + "'," +
                "'" + reason + "'," +
                "'" + admin + "'," +
                "'" + id + "'" +
                ");";
        execute(sql);
    }

    public void insertNewBlacklist(String uuid, String username, String reason, String admin, int id) {
        String sql = "INSERT INTO `" + getDatabase() + "`.`" + tablePrefix + "blacklists`" +
                "(`uuid`," +
                "`username`," +
                "`reason`," +
                "`admin`," +
                "`id`)" +
                "VALUES(" +
                "'" + uuid + "'," +
                "'" + username + "'," +
                "'" + reason + "'," +
                "'" + admin + "'," +
                "'" + id + "'" +
                ");";
        execute(sql);
    }

    public void insertNewMute(String uuid, String username, MuteType type, String reason, String admin, int id) {
        String sql = "INSERT INTO `" + getDatabase() + "`.`" + tablePrefix + "mutes`" +
                "(`uuid`," +
                "`username`," +
                "`mute-type`," +
                "`reason`," +
                "`admin`," +
                "`id`)" +
                "VALUES(" +
                "'" + uuid + "'," +
                "'" + username + "'," +
                "'" + type.getType() + "'," +
                "'" + reason + "'," +
                "'" + admin + "'," +
                "'" + id + "'" +
                ");";
        execute(sql);
    }

    public void insertBannedIp(Player player) {
        String sql = "INSERT INTO `" + getDatabase() + "`.`" + tablePrefix + "banned_ips`" +
                "(`uuid`," +
                "`ip`)" +
                "VALUES(" +
                "'" + player.getUniqueId() + "'," +
                "'" + Objects.requireNonNull(player.getAddress()).getHostName() + "'" +
                ");";
        execute(sql);
    }

    private void sendMessage(String s) {
        Punishments.getPlugin().sendMessage(s);
    }

}
