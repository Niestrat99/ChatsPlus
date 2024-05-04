package io.github.niestrat99.chatsplus.commands;

import io.github.niestrat99.chatsplus.Main;
import io.github.niestrat99.chatsplus.utils.Chats;
import io.github.niestrat99.chatsplus.utils.Worlds;
import io.github.thatsmusic99.configurationmaster.api.ConfigSection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConsoleCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            if (args.length > 0) {
                switch (args[0]) {
                    case "listChats" -> {
                        Main.log("List of chats:");
                        for (String chat : Chats.chats.keySet()) {
                            Main.log("- " + chat);
                        }
                    }

                    case "listWorldChats" -> {
                        Main.log("List of Per-World-Chats:");
                        for (String world : Worlds.worlds.keySet()) {
                            String worldChat = Worlds.worlds.get(world).getString("chat");
                            Main.log("- World:" + world + " | Chat: " + worldChat);
                        }
                    }

                    case "getPlayerChats" -> {
                        Main.log("List of players in a chat:");
                        for (Player player : Chats.chatRoom.keySet()) {
                            String chat = Chats.chatRoom.get(player);
                            Main.log("- Player: " + player.getName() + " | Chat: " + chat);
                        }
                    }

                    default -> {
                        Main.log("Console commands for ChatsPlus:");
                        Main.log("chatsystem listChats - Lists all chats.");
                        Main.log("chatsystem listWorldChats - Lists all worlds with assigned chats.");
                        Main.log("chatsystem getPlayerChats - Lists all players using a chat.");
                    }
                }
            }
        }
        return true;
    }
}
