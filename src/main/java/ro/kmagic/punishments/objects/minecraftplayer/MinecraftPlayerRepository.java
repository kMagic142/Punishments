package ro.kmagic.punishments.objects.minecraftplayer;

import ro.kmagic.punishments.Punishments;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

@SuppressWarnings("all")
public class MinecraftPlayerRepository {

    public static MinecraftPlayer getPlayerByUuid(String uuid) {
        try {
            String sql = "SELECT * FROM " + Punishments.tablePrefix + "players WHERE uuid='" + uuid + "'";
            ResultSet set = Punishments.getPlugin().getDatabase().query(sql);
            while (set.next()) {
                String username = set.getString("username");
                boolean banned = Boolean.parseBoolean(set.getString("banned"));
                Timestamp banexpirydate = set.getTimestamp("banexpirydate");
                boolean muted = Boolean.parseBoolean(set.getString("muted"));
                Timestamp muteexpirydate = set.getTimestamp("muteexpirydate");
                int warns = set.getInt("warns");
                int kicks = set.getInt("kicks");
                boolean blacklisted = Boolean.parseBoolean(set.getString("blacklisted"));
                return new MinecraftPlayer(uuid, username, banned, banexpirydate, muted, muteexpirydate, warns, kicks, blacklisted);
            }
            return null;
        } catch (SQLException ignored) {}
        return null;
    }

    public static MinecraftPlayer getPlayerByUuid(UUID uuid) {
        try {
            String sql = "SELECT * FROM " + Punishments.tablePrefix + "players WHERE uuid='" + uuid.toString() + "'";
            ResultSet set = Punishments.getPlugin().getDatabase().query(sql);
            while (set.next()) {
                String username = set.getString("username");
                boolean banned = Boolean.parseBoolean(set.getString("banned"));
                Timestamp banexpirydate = set.getTimestamp("banexpirydate");
                boolean muted = Boolean.parseBoolean(set.getString("muted"));
                Timestamp muteexpirydate = set.getTimestamp("muteexpirydate");
                int warns = set.getInt("warns");
                int kicks = set.getInt("kicks");
                boolean blacklisted = Boolean.parseBoolean(set.getString("blacklisted"));
                return new MinecraftPlayer(uuid.toString(), username, banned, banexpirydate, muted, muteexpirydate, warns, kicks, blacklisted);
            }
            return null;
        } catch (SQLException ignored) {}
        return null;
    }

    public static MinecraftPlayer getPlayerByUsername(String username) {
        try {
            String sql = "SELECT * FROM " + Punishments.tablePrefix + "players WHERE username='" + username + "'";
            ResultSet set = Punishments.getPlugin().getDatabase().query(sql);
            while (set.next()) {
                String uuid = set.getString("uuid");
                boolean banned = Boolean.parseBoolean(set.getString("banned"));
                Timestamp banexpirydate = set.getTimestamp("banexpirydate");
                boolean muted = Boolean.parseBoolean(set.getString("muted"));
                Timestamp muteexpirydate = set.getTimestamp("muteexpirydate");
                int warns = set.getInt("warns");
                int kicks = set.getInt("kicks");
                boolean blacklisted = Boolean.parseBoolean(set.getString("blacklisted"));
                return new MinecraftPlayer(uuid, username, banned, banexpirydate, muted, muteexpirydate, warns, kicks, blacklisted);
            }
            return null;
        } catch (SQLException ignored) {}
        return null;
    }

    public static MinecraftPlayer getPlayer(Player player) {
        try {
            String sql = "SELECT * FROM " + Punishments.tablePrefix + "players WHERE uuid='" + player.getUniqueId() + "'";
            ResultSet set = Punishments.getPlugin().getDatabase().query(sql);
            while (set.next()) {
                String uuid = set.getString("uuid");
                String username = set.getString("username");
                boolean banned = Boolean.parseBoolean(set.getString("banned"));
                Timestamp banexpirydate = set.getTimestamp("banexpirydate");
                boolean muted = Boolean.parseBoolean(set.getString("muted"));
                Timestamp muteexpirydate = set.getTimestamp("muteexpirydate");
                int warns = set.getInt("warns");
                int kicks = set.getInt("kicks");
                boolean blacklisted = Boolean.parseBoolean(set.getString("blacklisted"));
                return new MinecraftPlayer(uuid, username, banned, banexpirydate, muted, muteexpirydate, warns, kicks, blacklisted);
            }
            return null;
        } catch (SQLException ignored) {}
        return null;
    }

}
