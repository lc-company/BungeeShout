package cn.lcofficial.bungeeshout.config;

import cn.lcofficial.bungeeshout.BungeeShout;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Filter {

    private static final ConfigurationProvider provider = YamlConfiguration.getProvider(YamlConfiguration.class);
    private static final File filterFile = new File(BungeeShout.getInstance().getDataFolder(), "filter.yml");

    private static List<String> dict = new ArrayList<>();

    public static boolean check(String message) {
        for (String word : dict) {
            if (message.contains(word)) return false;
        }
        return true;
    }

    public static void initDict() throws IOException{
        FilenameFilter fFilter = (dir, name) -> name.endsWith(".dict");

        for (File file : Objects.requireNonNull(BungeeShout.getInstance().getDataFolder().listFiles(fFilter))) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine()) != null) {
                dict.add(line);
            }
        }

        if (!filterFile.exists()) {
            InputStream in = BungeeShout.getInstance().getResourceAsStream("filter.yml");
            FileOutputStream out = new FileOutputStream(filterFile);
            ByteStreams.copy(in, out);
        }

        Configuration filterConfig = provider.load(filterFile);
        dict.addAll(filterConfig.getStringList("words"));
    }

    public static String get(String message) {
        for (String word : dict ){
            if (message.contains(word)) return word;
        }
        return "";
    }

    public static void reloadDict() {
        dict = new ArrayList<>();
        try {
            initDict();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
