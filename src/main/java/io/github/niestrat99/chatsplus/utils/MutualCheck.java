package io.github.niestrat99.chatsplus.utils;

import io.github.niestrat99.chatsplus.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;

public class MutualCheck {

    public static void checkForMutuals() {
        try {
            HashMap<String, String> pluginConvo = new LinkedHashMap<>();

            Main.log(Level.INFO, "AVENGERS, ASSEMBLE!", null, null);
            pluginConvo.put("ChatPings", "Huh??");
            pluginConvo.put("AdvancedTeleport", "What the?");
            pluginConvo.put("HeadsPlus", "Hey, that's my line!!");
            pluginConvo.put("SimplePets", "Oh.");
            pluginConvo.put("KeepInvIndividual", "CHILDREN YEARN FOR THE MINES!!");

            for (String plugin : pluginConvo.keySet()) {
                Plugin validPlugin = Bukkit.getPluginManager().getPlugin(plugin);
                if (validPlugin != null) {
                    validPlugin.getLogger().info(pluginConvo.get(plugin));
                }
            }
        } catch (NoClassDefFoundError ignored) {}
    }
}
