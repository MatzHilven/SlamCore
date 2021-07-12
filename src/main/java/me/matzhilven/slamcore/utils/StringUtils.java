package me.matzhilven.slamcore.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class StringUtils {

    public static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> colorize(List<String> s) {
        return s.stream().map(StringUtils::colorize).collect(Collectors.toList());
    }

    public static String removeColor(String s) {
        return ChatColor.stripColor(colorize(s));
    }

    public static String decolorize(String s) {
        return ChatColor.stripColor(s);
    }

    public static List<String> decolorize(List<String> s) {
        return s.stream().map(ChatColor::stripColor).collect(Collectors.toList());
    }

    public static void sendMessage(CommandSender sender, String m) {
        sender.sendMessage(colorize(m));
    }

    public static void sendMessage(CommandSender sender, List<String> m) {
        m.forEach(msg -> sendMessage(sender,msg));
    }

    public static String format(int c) {
        return NumberFormat.getNumberInstance(Locale.US).format(c);
    }

    public static String format(long c) {
        return NumberFormat.getNumberInstance(Locale.US).format(c);
    }
}
