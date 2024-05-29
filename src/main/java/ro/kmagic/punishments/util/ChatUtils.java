package ro.kmagic.punishments.util;

import org.bukkit.ChatColor;

import java.text.DecimalFormat;


public class ChatUtils {

    public static String colorize(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String format(int i) {
        DecimalFormat format = new DecimalFormat("#,###");
        format.setGroupingUsed(true);
        return format.format(i);
    }

}
