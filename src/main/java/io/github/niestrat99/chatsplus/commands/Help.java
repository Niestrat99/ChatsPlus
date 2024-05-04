package io.github.niestrat99.chatsplus.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Help {
    private static final List<String> colors = new ArrayList<>(Arrays.asList("6", "7", "a", "b", "c", "d", "e"));

    public static void helpCommandResponse(Player player) {
        Random rnd = new Random();
        String chosenColor = colors.get(rnd.nextInt(colors.size()));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8-------<&b[&fChats &" + chosenColor + "Plus&b]&8>-------"));
        player.sendMessage(helpMessage("/chat help", "Shows you this help page."));
        player.sendMessage(helpMessage("/chat <chatName>", "Assigns you to a chat room."));
        player.sendMessage(helpMessage("/chat off", "Removes you from the current chat."));
        if (player.hasPermission("chatsplus.admin")) {
            player.sendMessage(helpMessage("/chat reload", "Reloads the config file to apply changes."));
        }
    }

    private static String helpMessage(String command, String description) {
        return ChatColor.translateAlternateColorCodes('&', "&b" + command + " &8>> &7" + description);
    }
}
