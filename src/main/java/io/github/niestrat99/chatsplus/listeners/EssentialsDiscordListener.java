package io.github.niestrat99.chatsplus.listeners;

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
            assert chat != null;
            ConfigSection chatSubConfigs = Config.configFile.getConfigSection("chats").getConfigSection(chat);
            boolean ignoreDiscord = chatSubConfigs.getBoolean("ignoreDiscord");

            if (ignoreDiscord) {
                e.setCancelled(true);
            }
        }
    }
}
