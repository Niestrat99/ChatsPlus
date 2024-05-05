package io.github.niestrat99.chatsplus.utils;

import io.github.niestrat99.chatsplus.Main;
import io.github.niestrat99.chatsplus.configuration.Config;
import io.github.thatsmusic99.configurationmaster.api.ConfigSection;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Chats {
    public static HashMap<Player, String> chatRoom = new HashMap<>();
    public static HashMap<String, ConfigSection> chats = new HashMap<>();

    public static String getChat(Player player) {
        if (!playerIsInChat(player)) {
            return null;
        }
        return chatRoom.get(player);
    }
    public static Boolean playerIsInChat(Player player) {
        return chatRoom.containsKey(player);
    }
    public static void assignPlayerToChat(Player player, String chat) {
        Main.debug("Assigning player " + player.getName() + " to chat: " + chat);
        chatRoom.put(player, chat);
    }
    public static void unassignPlayer(Player player) {
        Main.debug("Unassigning player " + player.getName() + " from any chat.");
        chatRoom.remove(player);
    }
    public static void getChatsList() {
        ConfigSection chatsSection = Config.configFile.getConfigSection("chats");
        chats.clear();
        if (chatsSection != null) {
            for (String chatName : chatsSection.getKeys(false)) {
                chats.put(chatName, Config.configFile.getConfigSection("chats." + chatName));
            }
        }
    }
}