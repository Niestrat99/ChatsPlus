package io.github.niestrat99.chatsplus.listeners;

import io.github.niestrat99.chatsplus.Main;
import io.github.niestrat99.chatsplus.configuration.Config;
import io.github.niestrat99.chatsplus.utils.Chats;
import io.github.thatsmusic99.configurationmaster.api.ConfigSection;
import net.essentialsx.api.v2.events.discord.DiscordChatMessageEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EssentialsDiscordListener implements Listener {
    @EventHandler
    public void essentialsDiscord(DiscordChatMessageEvent e) {
        Player player = e.getPlayer();

        if (Chats.playerIsInChat(player)) {
            String chat = Chats.getChat(player);
            if (chat == null) {return;}
            ConfigSection chatSubConfigs = Config.configFile.getConfigSection("chats").getConfigSection(chat);
            boolean ignoreDiscord = chatSubConfigs.getBoolean("ignoreDiscord");

            if (ignoreDiscord) {
                Main.debug("Chat " + chat + " | ignoreDiscord = false. Not broadcasting to Discord.");
                e.setCancelled(true);
            }
            Main.debug("Broadcasting to Discord.");
            Main.debug("Chat: " + chat + " | ignoreDiscord = " + ignoreDiscord);
            Main.debug("Player: " + player.getName());
            Main.debug("Message: " + e.getMessage());
        }
    }
}
