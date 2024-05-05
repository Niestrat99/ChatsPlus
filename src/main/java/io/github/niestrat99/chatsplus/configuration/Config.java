package io.github.niestrat99.chatsplus.configuration;

import io.github.niestrat99.chatsplus.Main;
import io.github.niestrat99.chatsplus.utils.Chats;
import io.github.niestrat99.chatsplus.utils.Worlds;
import io.github.thatsmusic99.configurationmaster.api.ConfigFile;
import io.github.thatsmusic99.configurationmaster.api.ConfigSection;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class Config extends ConfigFile {
    public static Config configFile;

    private static void validateFile() {
        if (!Main.get().getDataFolder().exists()) {
            boolean createFolderSuccess = Main.get().getDataFolder().mkdir();
            if (!createFolderSuccess) {
                Main.error("Failed to create DataFolder for ChatsPlus. Please tell the developer about this!");
            }
        }
    }

    public Config(@NotNull File file) throws IOException, IllegalAccessException {
        super(file);
    }

    public static void loadConfig() {
        validateFile();
        try {
            Config config = new Config(new File(Main.get().getDataFolder(), "config.yml"));
            configFile = config;
            config.load();
            validateChats();
        } catch (Exception e) {
            Main.error("Something went wrong whilst loading configurations: " + e.getMessage());
        }

    }

    public static void reloadConfig() {
        try {
            configFile.reload();
            validateChats();
            Chats.getChatsList();
            Worlds.getWorldDefaultChats();
        } catch (Exception e) {
            Main.error("Something went wrong whilst reloading configurations: " + e.getMessage());
        }
    }

    @Override
    public void addDefaults() {
        addSection("CHATSPLUS by Niestrat99");

        // Debug
        addSection("DEBUG");
        addDefault("debug-mode", false, "Allows the developer to try and find out any possible bugs.");

        addComment("[]>-----------------------------------------------------------------------------<[]");

        // Chats List
        addSection("CHATS");
        addComment(
                """
                This option allows you to make new chats. Simply add them to the list below.
                Each chat, that you create, will automatically have a permission named 'chatsplus.chat.<chatName>'.
                """
        );
        addComment(
                """
                The chats must have sub-settings set up as well, or else it uses default values which may look ugly.
                - title = The text that appears in the very front of the message.
                - nameTag = The way the player name is displayed. You can make it look like "xXPlayerNameXx" for example.
                - messageColor = The color of what the actual message shall appear in.
                - ignoreDiscord = This prevents the EssentialsX Discord plugin from sending a message out of this chat into the Discord server, if set to "true".
                EXAMPLE:
                chats:
                    vip:
                        title: '&8[&6VIP&8]'
                        nameTag: '&8<&6name&8>'
                        messageColor: '&6'
                        ignoreDiscord: true
                
                NOTE:
                - The color codes (e.g. &6) are optional, but allow you to make your chats look fancier.
                - You MUST have the text "name" included in your nameTag section, or else the player name won't be displayed.
                """
        );

        makeSectionLenient("chats");
        addExample("chats.staff.title", "&b[&fStaffChat&b]");
        addExample("chats.staff.nameTag", "&b<&fname&b>");
        addExample("chats.staff.messageColor", "&r");
        addExample("chats.staff.ignoreDiscord", true);

        addComment("[]>-----------------------------------------------------------------------------<[]");

        // Per World Chat
        addSection("PER WORLD CHAT");
        addComment(
                """
                In this section, you can assign a chat to a certain world.
                If a player joins that world, they'll automatically be assigned to that specific chat.
                Just like the chats section, this section also comes with sub configurations:
                - chat = The chat that shall be assigned to the world
                - isGlobal <true/false> = Whether the chat shall appear in all worlds or not
                - offStandard <true/false> = If the player shall be put into the assigned that when using /chat off/kick
                """
        );
        addComment(
                """
                To assign a chat to a world you do the following:
                per-world-chat:
                    world:
                        chat: vip
                        isGlobal: true
                        offStandard: false
                        
                NOTE:
                - The world must match the world's actual name.
                - All three sub configurations MUST be included! If not, then the plugin will set default values to it.
                - Make sure you're setting a valid chat created in the chats section above, or else the plugin will remove that setting.
                """
        );

        makeSectionLenient("per-world-chat");

        addComment("[]>-----------------------------------------------------------------------------<[]");

        // Assign Chat On Join
        addSection("ASSIGN CHAT ON JOIN");
        addComment(
                """
                Assigns the joining player to the specified chat, if they have the permission 'chatsplus.admin.join'.
                This can be ideal, if you want staff members to be automatically assigned to a staff chat.
                It bypasses the Per-World-Chat settings.
                """
        );
        addDefault("assign-chat-on-join", "");
    }

    private static void validateChats() throws Exception {
        ConfigSection chatsSection = configFile.getConfigSection("chats");
        for (String chat : chatsSection.getKeys(false)) {
            ConfigSection chatSubConfig = chatsSection.getConfigSection(chat);
            String title = chatSubConfig.getString("title");
            String tag = chatSubConfig.getString("nameTag");
            String mg = chatSubConfig.getString("messageColor");
            String id = chatSubConfig.getString("ignoreDiscord");

            if (title == null) {
                Main.warn(chat + " is missing title, setting default.");
                chatSubConfig.set("title", "[" + chat + "]");
            }
            if (tag == null) {
                Main.warn(chat + " is missing nameTag, setting default.");
                chatSubConfig.set("nameTag", "'<name>'");
            }
            if (mg == null) {
                Main.warn(chat + " is missing messageColor, setting default.");
                chatSubConfig.set("messageColor", "&r");
            }
            if (id == null) {
                Main.warn(chat + "is missing ignoreDiscord, setting default.");
                chatSubConfig.set("ignoreDiscord", true);
            }
            configFile.save();
        }
    }
}
