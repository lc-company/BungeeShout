package cn.lcofficial.bungeeshout.config;

import cn.lcofficial.bungeeshout.BungeeShout;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Config{

    private static final ConfigurationProvider provider = YamlConfiguration.getProvider(YamlConfiguration.class);
    private static final File configFile = new File(BungeeShout.getInstance().getDataFolder(), "config.yml");

    public static List<String> commands = new ArrayList<>();

    public static boolean urlAccess;

    public static void loadConfig() throws IOException {
        if (!BungeeShout.getInstance().getDataFolder().exists()) BungeeShout.getInstance().getDataFolder().mkdir();
        if (!configFile.exists()) {
            InputStream in = BungeeShout.getInstance().getResourceAsStream("config.yml");
            FileOutputStream out = new FileOutputStream(configFile);
            ByteStreams.copy(in, out);
        }

        Configuration config = provider.load(configFile);
        commands = config.getStringList("commands");
        urlAccess = config.getBoolean("urlAccess", true);
    }

}
