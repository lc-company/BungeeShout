package cn.lcofficial.bungeeshout.config;

import cn.lcofficial.bungeeshout.BungeeShout;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Message {

    private static final ConfigurationProvider provider = YamlConfiguration.getProvider(YamlConfiguration.class);
    private static final File messageFile = new File(BungeeShout.getInstance().getDataFolder(), "messages.yml");

    public static String
            prefix,
            connectText,
            playerColor,
            messageColor,
            FILTER_CHECK_MESSAGE,
            URL_NOT_ALLOW_MESSAGE,
            COOLDOWN_MESSAGE,
            START_RELOADING_MESSAGE,
            RELOADED_MESSAGE,
            NONE_ERROR_MESSAGE,
            CONSOLE_PREFIX,
            NO_PERM_MESSAGE;

    public static void loadMessages() throws IOException {

        if (!messageFile.exists()) {
            InputStream in = BungeeShout.getInstance().getResourceAsStream("messages.yml");
            FileOutputStream out = new FileOutputStream(messageFile);
            ByteStreams.copy(in, out);
        }

        Configuration messageConfig = provider.load(messageFile);
        
        prefix = messageConfig.getString("messagePrefix");
        connectText = messageConfig.getString("connectText");
        playerColor = messageConfig.getString("playerColor");
        messageColor = messageConfig.getString("messageColor");
        FILTER_CHECK_MESSAGE = messageConfig.getString("filterCheckMessage");
        URL_NOT_ALLOW_MESSAGE = messageConfig.getString("urlNotAccessMessage");
        COOLDOWN_MESSAGE = messageConfig.getString("cooldownMessage");
        START_RELOADING_MESSAGE = messageConfig.getString("startReloadingMessage");
        NONE_ERROR_MESSAGE = messageConfig.getString("noneErrorMessage");
        CONSOLE_PREFIX = messageConfig.getString("consolePrefix");
        NO_PERM_MESSAGE = messageConfig.getString("noPermissionMessage");

    }
    
}
