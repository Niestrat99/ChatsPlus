package io.github.niestrat99.chatsplus.commands;

import io.github.niestrat99.chatsplus.Main;
import io.github.niestrat99.chatsplus.utils.Chats;
import io.github.niestrat99.chatsplus.utils.Worlds;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class ConsoleCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            if (args.length > 0) {
                switch (args[0]) {
                    case "listChats" -> {
                        Main.log(Level.INFO, "List of chats:", null, null);
                        for (String chat : Chats.chats.keySet()) {
                            Main.log(Level.INFO, "- " + chat, null, null);
                        }
                    }

                    case "listWorldChats" -> {
                        Main.log(Level.INFO, "List of Per-World-Chats:", null, null);
                        for (String world : Worlds.worlds.keySet()) {
                            String worldChat = Worlds.worlds.get(world).getString("chat");
                            Main.log(Level.INFO, "- World:" + world + " | Chat: " + worldChat, null, null);
                        }
                    }

                    case "getPlayerChats" -> {
                        Main.log(Level.INFO, "List of players in a chat:", null, null);
                        for (Player player : Chats.chatRoom.keySet()) {
                            String chat = Chats.chatRoom.get(player);
                            Main.log(Level.INFO, "- Player: " + player.getName() + " | Chat: " + chat, null, null);
                        }
                    }

                    default -> commandList();

                }
            } else {
                commandList();
            }
        }
        return true;
    }

    private void commandList() {
        Main.log(Level.INFO, "Console commands for ChatsPlus:", null, null);
        Main.log(Level.INFO, "chatsystem listChats - Lists all chats.", null, null);
        Main.log(Level.INFO, "chatsystem listWorldChats - Lists all worlds with assigned chats.", null, null);
        Main.log(Level.INFO, "chatsystem getPlayerChats - Lists all players using a chat.", null, null);
    }
}
