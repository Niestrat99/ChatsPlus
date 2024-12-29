package io.github.niestrat99.chatsplus.listeners;

import io.github.niestrat99.chatsplus.Main;
import io.github.niestrat99.chatsplus.commands.Mute;
import io.github.niestrat99.chatsplus.configuration.Config;
import io.github.niestrat99.chatsplus.utils.Chats;
import io.github.niestrat99.chatsplus.utils.MessageUtil;
import io.github.niestrat99.chatsplus.utils.Worlds;
import io.github.thatsmusic99.configurationmaster.api.ConfigSection;
import net.essentialsx.api.v2.events.discord.DiscordChatMessageEvent;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.Objects;

public class ChatListener implements Listener {

    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        Main.debug("Player chatted: " + player.getName());

        if (Chats.playerIsInChat(player)) {
            World world = player.getWorld();
            String chat = Chats.getChat(player);
            ConfigSection worldData = Worlds.getData(world.getName());
            ConfigSection chatData = Chats.chats.get(chat);

            Main.debug("Player chatting in: World=" + world.getName() + ", Chat=" + chat);
            Main.debug("-----------------------------------------------------------------");
            if (worldData != null) {
                Main.debug("World data of " + world.getName() + ": Chat=" + worldData.getString("chat") + ", isGlobal=" + worldData.getBoolean("isGlobal") + ", offStandard=" + worldData.getBoolean("offStandard"));
                Main.debug("-----------------------------------------------------------------");
            }
            if (chatData != null) {
                Main.debug("Chat data of " + chat + ": title=" + chatData.getString("title") + ", nameTag=" + chatData.getString("nameTag") + ", messageColor=" + chatData.getString("messageColor"));
                Main.debug("-----------------------------------------------------------------");
            }
            Main.debug("Recipients receiving message:");

            for (Player recipient : Main.get().getServer().getOnlinePlayers()) {
                List<String> mutedChat = Mute.muteList.get(recipient);
                if (mutedChat != null) {
                    if (mutedChat.contains(chat)) {
                        e.getRecipients().remove(recipient);
                    }
                }

                if (!recipient.hasPermission("chatsplus.chat." + chat) && !Chats.playerIsInChat(recipient)) {
                    e.getRecipients().remove(recipient);
                    continue;
                }

                if (worldData != null && worldData.getBoolean("isGlobal")) {
                    if (!recipient.getWorld().equals(player.getWorld())
                            || !recipient.hasPermission("chatsplus.admin.bypass")
                            || !recipient.hasPermission("chatsplus.chat.read" + chat)) {
                        e.getRecipients().remove(recipient);
                    }

                    e.setFormat(MessageUtil.chatRoomTitle(player));
                    continue;
                }
                Main.debug(recipient.getName());
                e.setFormat(MessageUtil.chatRoomTitle(player));
            }
        }
    }

    @EventHandler
    public void leaveEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (Chats.playerIsInChat(player)) {
            Main.debug("Unassigning leaving player " + player.getName() + " from chats.");
            Chats.unassignPlayer(player);
        }
    }

    @EventHandler
    public void joinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String chat = Config.configFile.getString("assign-chat-on-join");
        ConfigSection worldData = Worlds.getData(player.getWorld().getName());

        if (chat != null) {
            if (player.hasPermission("chatsplus.admin.join") && !chat.isBlank()) {
                Chats.assignPlayerToChat(player, chat);
                MessageUtil.msgInfo(player, "You have been assigned to chat &b" + chat + "&r.");
                Main.debug("Player" + player.getName() + "has permission to admin chat.");

            } else if (worldData != null && worldData.getBoolean("offStandard")) {
                String worldChat = worldData.getString("chat");
                Chats.assignPlayerToChat(player, worldData.getString("chat"));
                MessageUtil.msgInfo(player, "You have been assigned to chat &b" + worldChat + "&r.");
                Main.debug("Assigning player " + player.getName() + "to standard chat of world '" + player.getWorld() + "'.");
            }
        }
    }

    @EventHandler
    public void enterEvent(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();
        Main.debug("Player " + player.getName() + " is switching from world '" + e.getFrom() + "' to world '" + player.getWorld() + "'.");
        ConfigSection worldData = Worlds.getData(player.getWorld().getName());
        if (worldData != null) {
            String worldChat = worldData.getString("chat");
            if (!Objects.requireNonNull(Chats.getChat(player)).equals(worldChat)) {
                Chats.assignPlayerToChat(player, worldChat);
                Main.debug("Assigning player " + player.getName() + "to standard chat of world '" + player.getWorld() + "'.");
                MessageUtil.msgInfo(player, "You have been assigned to chat &b" + worldChat + "&r.");
            }
        }
    }
}
