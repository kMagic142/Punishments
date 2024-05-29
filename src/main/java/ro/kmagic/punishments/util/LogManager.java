package ro.kmagic.punishments.util;

import ro.kmagic.punishments.Punishments;
import org.bukkit.entity.Player;

import java.sql.Timestamp;

public class LogManager {

    public static void addLog(Player player, String type, String reason, String target, String time) {
        String sql = "INSERT INTO " + Punishments.getPlugin().getDatabase().getDatabase() + ".`" + Punishments.tablePrefix + "logs`" +
                "(`uuid`," +
                "`type`," +
                "`reason`," +
                "`target`," +
                "`username`," +
                "`date`," +
                "`time`)" +
                "VALUES(" +
                "'" + player.getUniqueId() + "'," +
                "'" + type + "'," +
                "'" + reason + "'," +
                "'" + target + "'," +
                "'" + player.getName() + "'," +
                "'" + new Timestamp(System.currentTimeMillis()) + "'," +
                "'" + time + "');";
        Punishments.getPlugin().getDatabase().execute(sql);
    }

}
