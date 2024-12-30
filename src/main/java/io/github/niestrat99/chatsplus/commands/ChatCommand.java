package io.github.niestrat99.chatsplus.commands;

import io.github.niestrat99.chatsplus.Main;
import io.github.niestrat99.chatsplus.configuration.Config;
import io.github.niestrat99.chatsplus.utils.Chats;
import io.github.niestrat99.chatsplus.utils.MessageUtil;
import io.github.niestrat99.chatsplus.utils.Worlds;
import io.github.thatsmusic99.configurationmaster.api.ConfigSection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (!Main.checkPerms(player, "chatsplus.command.chat")) {return false;}
            if (args.length > 0) {
                switch (args[0]) {
                    case "reload" -> {
                        if (!Main.checkPerms(player, "chatsplus.command.reload")) {return false;}
                        Main.debug("Starting to reload the config files.");
                        MessageUtil.msgInfo(player, "Reloading config file...");

                        Config.reloadConfig();

                        MessageUtil.msgSuccess(player, "Config has been reloaded!");
                        Main.debug("Configs have been reloaded without issues.");
                    }
                    case "off" -> {
                        if (!Main.checkPerms(player, "chatsplus.command.off")) {return false;}
                        World world = player.getWorld();
                        String chat = Chats.getChat(player);
                        ConfigSection worldData = Worlds.getData(world.getName());

                        Main.debug("Player to unassign in: World=" + world.getName() + ", Chat=" + chat);
                        Main.debug("-----------------------------------------------------------------");
                        if (worldData != null) {
                            Main.debug("World data of " + world.getName() + ": Chat=" + worldData.getString("chat") + ", isGlobal=" + worldData.getBoolean("isGlobal") + ", offStandard=" + worldData.getBoolean("offStandard"));
                        }

                        if (chat == null) {
                            MessageUtil.msgError(player, "You are not in any chat.");
                            Main.debug("Player is not in any chat, returning.");
                            return false;
                        } else if (worldData != null && chat.equals(worldData.getString("chat"))) {
                            MessageUtil.msgError(player, "You cannot leave this chat due to world restrictions.");
                            Main.debug("Player is in world standard chat. Returning.");
                            return false;
                        }
                        if (Worlds.worlds.containsKey(player.getWorld().getName()) && Worlds.getData(player.getWorld().getName()).getBoolean("offStandard")) {
                            Chats.assignPlayerToChat(player, Worlds.getWorldChat(player.getWorld().getName()));
                        } else {
                            Chats.unassignPlayer(player);
                        }
                        MessageUtil.msgSuccess(player, "You left the chat room.");
                    }
                    case "help" -> {
                        if (!Main.checkPerms(player, "chatsplus.command.help")) {return false;}
                        Help.helpCommandResponse(player);
                    }
                    case "kick" -> {
                        if (!Main.checkPerms(player, "chatsplus.command.kick")) {return false;}

                        Main.debug("Player who initiated kick command: " + player.getName());

                        if (args.length > 1) {
                            Player target = Bukkit.getPlayerExact(args[1]);
                            Main.debug("Target to kick: " + target);

                            if (target == null) {
                                MessageUtil.msgError(player, "Player '" + args[1] + "' doesn't exist or is offline.");
                                Main.debug("Target is null, returning.");
                                return false;
                            }
                            String targetChat = Chats.getChat(target);

                            if (targetChat == null) {
                                MessageUtil.msgError(player, target.getName() + " is not in any chat.");
                                Main.debug("Target is not in any chat, returning.");
                                return false;
                            } else if (Worlds.worlds.containsKey(target.getWorld().getName()) && Worlds.getData(target.getWorld().getName()).getBoolean("offStandard")) {
                                Chats.assignPlayerToChat(target, Worlds.getWorldChat(target.getWorld().getName()));
                            } else {
                                Chats.unassignPlayer(target);
                            }
                            MessageUtil.msgInfo(target, "You have been kicked from the chat.");
                            MessageUtil.msgSuccess(player, "Successfully kicked " + target.getName() + " from the chat.");
                        }
                    }
                    case "mute" -> {
                        if (!Main.checkPerms(player, "chatsplus.command.mute")) {return false;}
                        if (args.length > 1) {
                            String chatName = args[1];
                            Main.debug("Player " + player.getName() + " initiated mute command to mute chat " + chatName + ".");

                            if (!Chats.chats.containsKey(chatName) || !player.hasPermission("chatsplus.chat." + chatName)) {
                                MessageUtil.msgError(player, "Chat does not exist.");
                                Main.debug("Chat does not exist, returning.");
                                return false;
                            } else {
                                Mute.muteChatForPlayer(player, chatName);
                                Main.debug("Player successfully muted chat.");
                            }
                        } else {
                            MessageUtil.msgError(player, "Too few arguments.");
                            return false;
                        }
                    }
                    case "unmute" -> {
                        if (!Main.checkPerms(player, "chatsplus.command.unmute")) {return false;}
                        if (args.length > 1) {
                            String chatName = args[1];
                            Main.debug("Player " + player.getName() + " initiated mute command to mute chat " + chatName + ".");

                            if (!Chats.chats.containsKey(chatName) || !player.hasPermission("chatsplus.chat." + chatName)) {
                                MessageUtil.msgError(player, "Chat does not exist.");
                                Main.debug("Chat does not exist, returning.");
                                return false;
                            } else {
                                Mute.unmuteChatForPlayer(player, chatName);
                                Main.debug("Player successfully unmuted chat.");
                            }
                        } else {
                            MessageUtil.msgError(player, "Too few arguments.");
                            return false;
                        }
                    }
                    case "assign" -> {
                        if (args.length > 2) {
                            Player target = Bukkit.getPlayerExact(args[1]);
                            String chatName = args[2];

                            Main.debug("Player " + player.getName() + " initiated assign command to assign target " + target + " to chat " + chatName + ".");

                            if (target == null) {
                                MessageUtil.msgError(player, "Player '" + args[1] + "' doesn't exist or is offline.");
                                Main.debug("Target does not exist, returning.");
                                return false;
                            }
                            if (!Chats.chats.containsKey(chatName)) {
                                MessageUtil.msgError(player, "Chat doesn't exist.");
                                Main.debug("Chat does not exist, returning.");
                                return false;
                            }
                            if (Chats.playerIsInChat(target) && Objects.requireNonNull(Chats.getChat(player)).equals(chatName)) {
                                MessageUtil.msgError(player, target.getName() + " is already in that chat.");
                                Main.debug("Target is already assigned to chat " + chatName + ", returning.");
                                return false;
                            }

                            Chats.assignPlayerToChat(target, chatName);
                            MessageUtil.msgSuccess(player, target.getName() + " has been added to chat &e" + chatName + "&a!");
                            MessageUtil.msgInfo(target, "You were moved to chat &e" + chatName + "&r.");
                            Main.debug("Target was successfully assigned to chat.");
                        } else {
                            MessageUtil.msgError(player, "Too few arguments.");
                            return false;
                        }
                    }
                    default -> {
                        String chatName = args[0];

                        Main.debug("Player " + player.getName() + " tries to assign to chat " + chatName + ".");

                        if (!Chats.chats.containsKey(chatName)) {
                            MessageUtil.msgError(player, "'" + chatName + "'" + " is not a valid chat room!");
                            Main.debug("Chat does not exist, returning.");
                            return false;
                        }
                        if (!Main.checkPerms(player, "chatsplus.command.chat." + chatName)) {return false;}

                        if (Chats.playerIsInChat(player)) {
                            if (Chats.chatRoom.get(player).contains(args[0])) {
                                Chats.unassignPlayer(player);
                                MessageUtil.msgSuccess(player, "You left the chat room.");
                                return false;
                            }
                            Main.debug("Player switches chats from " + Chats.getChat(player) + " to " + chatName + ".");
                        }
                        Chats.assignPlayerToChat(player, args[0]);
                        MessageUtil.msgSuccess(player, "You're now chatting in " + args[0] + "!");
                        Main.debug("Player successfully assigned to chat.");
                    }
                }
            } else {
                MessageUtil.msgError(player, "Too few arguments.");
                return false;
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> suggestions = new ArrayList<>();

        for (String value : Chats.chats.keySet()) {
            if (sender.hasPermission("chatsplus.chat." + value)) {
                suggestions.add(value);
            }
        }

        if (args.length == 1) {
            suggestions.add("help");
            suggestions.add("off");
            suggestions.add("mute");
            suggestions.add("unmute");
            if (sender.hasPermission("chatsplus.command.reload")) {
                suggestions.add("reload");
            }
            if (sender.hasPermission("chatsplus.command.kick")) {
                suggestions.add("kick");
            }
            if (sender.hasPermission("chatsplus.command.assign")) {
                suggestions.add("assign");
            }
        }

        if (args.length == 2) {
            if ((args[0].equals("assign") && sender.hasPermission("chatsplus.command.assign")) || (args[0].equals("kick") && sender.hasPermission("chatsplus.command.kick"))) {
                return null;
            }
            for (String chat : Chats.chats.keySet()) {
                if (sender.hasPermission("chatsplus.chat." + chat)) {
                    suggestions.add(chat);
                }
            }
        }
        if (args.length == 3) {
            if (args[0].equals("assign") && sender.hasPermission("chatsplus.command.assign")) {
                for (String chat : Chats.chats.keySet()) {
                    if (sender.hasPermission("chatsplus.chat." + chat)) {
                        suggestions.add(chat);
                    }
                }
            } else {
                suggestions.clear();
            }
        }

        return suggestions;
    }
}
