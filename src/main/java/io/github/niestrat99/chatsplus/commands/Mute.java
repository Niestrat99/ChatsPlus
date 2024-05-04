package io.github.niestrat99.chatsplus.commands;

import io.github.niestrat99.chatsplus.utils.MessageUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Mute {
    public static HashMap<Player, List<String>> muteList = new HashMap<>();

    public static void muteChatForPlayer(Player player, String chat) {
        if (muteList.containsKey(player) && muteList.get(player).contains(chat)) {
            MessageUtil.msgError(player, "Chat is already muted.");
            return;
        }
        if (!muteList.containsKey(player)) {
            muteList.put(player, new ArrayList<>());
        }
        muteList.get(player).add(chat);
        MessageUtil.msgSuccess(player, "Chat &e" + chat + "&a is now muted!");
    }

    public static void unmuteChatForPlayer(Player player, String chat) {
        if (!muteList.containsKey(player) || !muteList.get(player).contains(chat)) {
            MessageUtil.msgError(player, "Chat is not muted.");
            return;
        }
        muteList.get(player).remove(chat);
        if (muteList.get(player).isEmpty()) {
            muteList.remove(player);
        }
        MessageUtil.msgSuccess(player, "Chat &e" + chat + "&a is now unmuted!");
    }

    public static void clearPlayer(Player player) {
        muteList.remove(player);
    }
}
