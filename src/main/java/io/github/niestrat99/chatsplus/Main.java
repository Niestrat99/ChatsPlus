package io.github.niestrat99.chatsplus;

import io.github.niestrat99.chatsplus.commands.ChatCommand;
import io.github.niestrat99.chatsplus.commands.ConsoleCommands;
import io.github.niestrat99.chatsplus.configuration.Config;
import io.github.niestrat99.chatsplus.listeners.ChatListener;
import io.github.niestrat99.chatsplus.listeners.EssentialsDiscordListener;
import io.github.niestrat99.chatsplus.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Level;

public class Main extends JavaPlugin {
    public static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        log(Level.INFO, "ChatsPlus is starting up!", null, null);

        // Registering listeners
        log(Level.INFO, "Registering events...", null, null);
        regEvent(new ChatListener());
        if (Bukkit.getPluginManager().getPlugin("EssentialsDiscord") != null) {
            regEvent(new EssentialsDiscordListener());
        }

        // Registering commands
        log(Level.INFO, "Registering commands...", null, null);
        regCmd("chat", new ChatCommand());
        regCmd("chatsystem", new ConsoleCommands());

        // Set up config
        log(Level.INFO, "Loading configuration...", null, null);
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
        log(Level.INFO, "ChatsPlus has successfully loaded!", null, null);
    }

    public static Main get() {
        return instance;
    }

    // For debugging
    public static void debug(String message) {
        if (!Config.configFile.getBoolean("debug-mode")) {return;}
        Main.get().getLogger().info("[DEBUG] > " + message);
    }

    // Console Logging
    public static void log(Level level, String msg, Class<?> className, Exception stacktrace) {
        String message = msg;
        if (className != null) {
            message = message.concat("\n(" + className.getName() + ")");
        }
        if (stacktrace != null) {
            message = message.concat("\nStacktrace:\n" + stacktrace);
        }
        instance.getLogger().log(level, message);
    }

    // Permission Check
    public static Boolean checkPerms(Player player, String permission) {
        debug("Player " + player.getName() + " has permission to node " + permission + ": " + player.hasPermission(permission));
        if (!player.hasPermission(permission)) {
            MessageUtil.msgError(player, "You do not have permission for this!");
            return false;
        }
        return true;
    }

    // Functions to register commands and events. Just to personally write things shorter.
    private static void regCmd(String commandName, CommandExecutor commandClass) {
        Objects.requireNonNull(instance.getCommand(commandName)).setExecutor(commandClass);
    }
    private static void regEvent(Listener eventClass) {
        instance.getServer().getPluginManager().registerEvents(eventClass, instance);
    }
}
