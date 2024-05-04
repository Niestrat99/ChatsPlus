package io.github.niestrat99.chatsplus.utils;

import io.github.thatsmusic99.configurationmaster.api.ConfigSection;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;

public class MessageUtil {
    // Final stuff
    private static final String title = "&bChatsPlus &8>> &r";
    private static final String successIcon = "[&a✔&r] ";
    private static final String errorIcon = "[&c✖&r] ";

    // Message presets
    private static String msgTitle(String message) {
        return ChatColor.translateAlternateColorCodes('&', title + message);
    }
    public static String chatRoomTitle(Player player) {
        ConfigSection chat = Chats.chats.get(Chats.getChat(player));

        String title = chat.getString("title");
        String tag = Objects.requireNonNull(chat.getString("nameTag")).replace("name", "%s");
        String color = chat.getString("messageColor");

        return ChatColor.translateAlternateColorCodes('&', title + " &r" + tag + " &r" + color + "%s");
    }
    public static void msgSuccess(Player player, String message) {
        player.sendMessage(msgTitle(successIcon + "&a" + message));
    }
    public static void msgError(Player player, String message) {
        player.sendMessage(msgTitle(errorIcon + "&c" + message));
    }

    public static void msgInfo(Player player, String message) {
        player.sendMessage(msgTitle(message));
    }

}
