package io.github.niestrat99.chatsplus.utils.expansions;

import io.github.niestrat99.chatsplus.utils.Chats;
import io.github.thatsmusic99.configurationmaster.api.ConfigSection;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PAPIExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "chatsplus";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Niestrat99";
    }

    @Override
    public @NotNull String getVersion() {
        return "2.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        Player onlinePlayer = player.getPlayer();

        if (identifier.equals("chat")) {
            ConfigSection chat = Chats.chats.get(Chats.getChat(onlinePlayer));
            String title = chat.getString("title");
            return title;
        }
        return null;
    }
}
