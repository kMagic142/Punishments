package ro.kmagic.punishments.objects.blacklist;

import ro.kmagic.punishments.Punishments;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class BlacklistRepository {

    public static Blacklist getBlacklistByUuid(UUID uuid) {
        try {
            ResultSet set = Punishments.getPlugin().getDatabase().query("SELECT * FROM " + Punishments.tablePrefix + "blacklists WHERE uuid='" + uuid.toString() + "'");

            if (set.next()) {
                String username = set.getString("username");
                String reason = set.getString("reason");
                String admin = set.getString("admin");
                int id = set.getInt("id");
                return new Blacklist(uuid.toString(), username, reason, admin, id);
            }
        } catch (SQLException ignored) {}
        return null;
    }

    public static Blacklist getBlacklistByUuid(String uuid) {
        try {
            ResultSet set = Punishments.getPlugin().getDatabase().query("SELECT * FROM " + Punishments.tablePrefix + "blacklists WHERE uuid='" + uuid + "'");

            if (set.next()) {
                String username = set.getString("username");
                String reason = set.getString("reason");
                String admin = set.getString("admin");
                int id = set.getInt("id");
                return new Blacklist(uuid, username, reason, admin, id);
            }
        } catch (SQLException ignored) {}
        return null;
    }

    public static Blacklist getBlacklistById(int id) {
        try {
            ResultSet set = Punishments.getPlugin().getDatabase().query("SELECT * FROM " + Punishments.tablePrefix + "blacklists WHERE id='" + id + "'");

            if (set.next()) {
                String uuid = set.getString("uuid");
                String username = set.getString("username");
                String reason = set.getString("reason");
                String admin = set.getString("admin");
                return new Blacklist(uuid, username, reason, admin, id);
            }
        } catch (SQLException ignored) {}
        return null;
    }

    public static List<Blacklist> getAllBlacklists() {
        List<Blacklist> bans = new ArrayList<>();
        try {
            ResultSet set = Punishments.getPlugin().getDatabase().query("SELECT * FROM " + Punishments.tablePrefix + "blacklists");

            while (set.next()) {
                String uuid = set.getString("uuid");
                String username = set.getString("username");
                String reason = set.getString("reason");
                String admin = set.getString("admin");
                int id = set.getInt("id");
                bans.add(new Blacklist(uuid, username, reason, admin, id));
            }
        } catch (SQLException ignored) {}
        return bans;
    }

    public static List<Blacklist> getBlacklistsByAdmin(String admin) {
        List<Blacklist> bans = new ArrayList<>();
        try {
            ResultSet set = Punishments.getPlugin().getDatabase().query("SELECT * FROM " + Punishments.tablePrefix + "blacklists WHERE admin='" + admin + "'");

            while (set.next()) {
                String uuid = set.getString("uuid");
                String username = set.getString("username");
                String reason = set.getString("reason");
                int id = set.getInt("id");
                bans.add(new Blacklist(uuid, username, reason, admin, id));
            }
        } catch (SQLException ignored) {}
        return bans;
    }

    public static List<Blacklist> getBlacklistsByAdmin(UUID admin) {
        List<Blacklist> bans = new ArrayList<>();
        try {
            ResultSet set = Punishments.getPlugin().getDatabase().query("SELECT * FROM " + Punishments.tablePrefix + "blacklists WHERE admin='" + admin.toString() + "'");

            while (set.next()) {
                String uuid = set.getString("uuid");
                String username = set.getString("username");
                String reason = set.getString("reason");
                int id = set.getInt("id");
                bans.add(new Blacklist(uuid, username, reason, admin.toString(), id));
            }
        } catch (SQLException ignored) {}
        return bans;
    }

    public static int getNextId() {
        try {
            ResultSet set = Punishments.getPlugin().getDatabase().query("SELECT id FROM " + Punishments.tablePrefix + "blacklists");

            int id = 0;

            while (set.next()) {
                if (set.getInt("id") > id) id = set.getInt("id");
            }

            return id;
        } catch (SQLException ignored) {}
        return 0;
    }

}
