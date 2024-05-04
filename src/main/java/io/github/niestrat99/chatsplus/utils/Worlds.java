package io.github.niestrat99.chatsplus.utils;

import io.github.niestrat99.chatsplus.Main;
import io.github.niestrat99.chatsplus.configuration.Config;
import io.github.thatsmusic99.configurationmaster.api.ConfigSection;

import java.util.HashMap;

public class Worlds {
    public static HashMap<String, ConfigSection> worlds = new HashMap<>();
    public static void getWorldDefaultChats() {
        ConfigSection perWorldChat = Config.configFile.getConfigSection("per-world-chat");
        worlds.clear();
        if (perWorldChat != null) {
            for (String worldName : perWorldChat.getKeys(false)) {
                ConfigSection worldConfig = perWorldChat.getConfigSection(worldName);
                if (worldConfig.getString("chat") == null || !worldConfig.getBoolean("isGlobal") || !worldConfig.getBoolean("offStandard")) {
                    Main.error("Missing sub configurations for world "+ worldName + " and will be ignored. Please correct it in the config file and reload to fix.");
                    continue;
                }
                worlds.put(worldName, Config.configFile.getConfigSection("per-world-chat." + worldName));
            }
        }

    }
    public static ConfigSection getData(String world) {
        return worlds.get(world);
    }
    public static String getWorldChat(String world) {
        return getData(world).getString("chat");
    }
}
