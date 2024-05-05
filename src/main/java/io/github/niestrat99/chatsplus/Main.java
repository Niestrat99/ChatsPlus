package io.github.niestrat99.chatsplus;

import io.github.niestrat99.chatsplus.commands.ChatCommand;
import io.github.niestrat99.chatsplus.commands.ConsoleCommands;
import io.github.niestrat99.chatsplus.configuration.Config;
import io.github.niestrat99.chatsplus.listeners.ChatListener;
import io.github.niestrat99.chatsplus.utils.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin {
    public static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        log("ChatsPlus is starting up!");

        // Registering listeners
        log("Registering events...");
        getServer().getPluginManager().registerEvents(new ChatListener(), this);

        // Registering commands
        log("Registering commands...");
        Objects.requireNonNull(getCommand("chat")).setExecutor(new ChatCommand());
        Objects.requireNonNull(getCommand("chatsystem")).setExecutor(new ConsoleCommands());

        // Set up config
        log("Loading config...");
        try {
            Config.loadConfig();
            Chats.getChatsList();
            Worlds.getWorldDefaultChats();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Fun extra
        MutualCheck.checkForMutuals();

        // Done
        log("ChatsPlus is ready!");
    }

    public static Main get() {
        return instance;
    }

    // For debugging
    public static void debug(String message) {
        if (!Config.configFile.getBoolean("debug-mode")) {return;}
        Main.get().getLogger().info("[DEBUG] > " + message);
    }

    public static void log(String message) {
        Main.get().getLogger().info(message);
    }
    public static void warn(String message) {
        Main.get().getLogger().warning(message);
    }

    public static void error(String message) {
        Main.get().getLogger().severe(ErrorHandler.errorSplash() + " - " + message);
    }

    public static Boolean checkPerms(Player player, String permission) {
        debug("Player " + player.getName() + " has permission to node " + permission + ": " + player.hasPermission(permission));
        if (!player.hasPermission(permission)) {
            MessageUtil.msgError(player, "You do not have permission for this!");
            return false;
        }
        return true;
    }
}
