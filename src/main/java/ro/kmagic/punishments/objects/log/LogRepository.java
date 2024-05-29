package ro.kmagic.punishments.objects.log;

import ro.kmagic.punishments.Punishments;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LogRepository {

    public static List<Log> getLogsByUuid(String uuid) {
        try {
            ResultSet set = Punishments.getPlugin().getDatabase().query("SELECT * FROM " + Punishments.tablePrefix + "logs WHERE uuid='" + uuid + "'");

            List<Log> logs = new ArrayList<>();

            while (set.next()) {
                logs.add(new Log(set.getString("uuid"), set.getString("type"), set.getString("reason"), set.getString("target"), set.getString("username"), set.getTimestamp("date"), set.getString("time")));
            }

            return logs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void clearLogsByUuid(String uuid) {
        Punishments.getPlugin().getDatabase().execute("DELETE FROM " + Punishments.tablePrefix + "logs WHERE uuid='" + uuid + "'");
    }

}
