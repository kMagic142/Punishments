package ro.kmagic.punishments.util;

import ro.kmagic.punishments.enums.BanType;
import ro.kmagic.punishments.enums.MuteType;
import ro.kmagic.punishments.objects.ban.Ban;
import ro.kmagic.punishments.objects.blacklist.Blacklist;
import ro.kmagic.punishments.objects.minecraftplayer.MinecraftPlayerRepository;
import ro.kmagic.punishments.objects.mute.Mute;
import org.bukkit.command.CommandSender;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Placeholders {

    public static String setKickPlaceholders(String s, String reason, CommandSender sender) {
        String admin = sender.getName();
        return s.replaceAll("%reason%", reason).replaceAll("%admin%", admin);
    }

    public static String setKickPlaceholders(String s, String reason, CommandSender sender, String player) {
        String admin = sender.getName();
        return s.replaceAll("%reason%", reason).replaceAll("%admin%", admin).replaceAll("%player%", player);
    }

    public static String setBlacklistPlaceholders(String s, Blacklist blacklist) {
        return s.replaceAll("%reason%", blacklist.getReason()).replaceAll("%admin%", blacklist.getAdmin()).replaceAll("%id%", Integer.toString(blacklist.getId())).replaceAll("%player%", blacklist.getUsername());
    }

    public static String setMutePlaceholders(String s, Mute mute) {
        if (mute.getType() == MuteType.mute) return s.replaceAll("%reason%", mute.getReason()).replaceAll("%admin%", mute.getAdmin()).replaceAll("%time%", findTime(MinecraftPlayerRepository.getPlayerByUuid(mute.getUuid()).getMuteexpirydate())).replaceAll("%id%", Integer.toString(mute.getId())).replaceAll("%player%", mute.getUsername());
        else return s.replaceAll("%reason%", mute.getReason()).replaceAll("%admin%", mute.getAdmin()).replaceAll("%time%", "Never").replaceAll("%id%", Integer.toString(mute.getId()));
    }

    public static String setBanPlaceholders(String s, Ban ban) {
        if (ban.getType() == BanType.ban) return s.replaceAll("%reason%", ban.getReason()).replaceAll("%admin%", ban.getAdmin()).replaceAll("%time%", findTime(MinecraftPlayerRepository.getPlayerByUuid(ban.getUuid()).getBanexpirydate())).replaceAll("%id%", Integer.toString(ban.getId())).replaceAll("%player%", ban.getUsername());
        else return s.replaceAll("%reason%", ban.getReason()).replaceAll("%admin%", ban.getAdmin()).replaceAll("%time%", "Never").replaceAll("%id%", Integer.toString(ban.getId())).replaceAll("%player%", ban.getUsername());
    }

    public static String findTime(Timestamp timestamp) {
        String diff = "";

        Date date = new Date(System.currentTimeMillis());
        Date date1 = new Date(timestamp.getTime());

        final long ms = date1.getTime() - date.getTime();

        final long years = TimeUnit.MILLISECONDS.toDays(ms) / 365;
        final long days = TimeUnit.MILLISECONDS.toDays(ms) %  365;
        final long hours = TimeUnit.MILLISECONDS.toHours(ms) % 24;
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(ms) % 60;
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60;

        if (years > 0) diff = diff.concat(years + "y ");
        if (days > 0) diff = diff.concat(days + "d ");
        if (hours > 0) diff = diff.concat(hours + "h ");
        if (minutes > 0) diff = diff.concat(minutes + "m ");
        if (seconds > 0) diff = diff.concat(seconds + "s ");

        if (!(diff.length() < 1)) diff = diff.substring(0, diff.length()-1);
        if (diff.isEmpty()) diff = diff.concat("0s");

        return diff;
    }

    public static long getTime(String s) {
        long l = 0L;
        if (s.contains("s")) {
            s = s.replaceFirst("s", "");
            l =  (Long.parseLong(s) * 1000L);
        } else if (s.contains("m")) {
            s = s.replaceFirst("m", "");
            l =  (Long.parseLong(s) * 60000L);
        } else if (s.contains("h")) {
            s = s.replaceFirst("h", "");
            l =  (Long.parseLong(s) * 3600000L);
        } else if (s.contains("d")) {
            s = s.replaceFirst("d", "");
            l =  (Long.parseLong(s) * 86400000L);
        } else if (s.contains("y")) {
            s = s.replaceFirst("y", "");
            l =  (Long.parseLong(s) * 31556952000L);
        } else if (s.equals("-1")) {
            l =  -1;
        }
        return l;
    }

}
