package it.unixdevelopment.eegame.utils.color;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class ChatUtil {

    public static String color(String old) {
        return ChatColor.translateAlternateColorCodes('&', old);
    }

    public static String papiColor(Player player, String old) {
        return PlaceholderAPI.setPlaceholders(player, color(old));
    }

    public static List<String> color(List<String> old) {
        old.replaceAll(ChatUtil::color);
        return old;
    }

    public static List<String> papiColor(Player player, List<String> old) {
        old.replaceAll(s -> papiColor(player, s));
        return old;
    }

}
