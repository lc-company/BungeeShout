package cn.lcofficial.bungeeshout;

import cn.lcofficial.bungeeshout.config.Config;
import cn.lcofficial.bungeeshout.config.Filter;
import cn.lcofficial.bungeeshout.config.Message;

import cn.lcofficial.bungeeshout.manager.CommandManager;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;

public final class BungeeShout extends Plugin {

    private static BungeeShout instance;

    @Override
    public void onEnable() {
        setInstance(this);

        try {
            Config.loadConfig();
            Message.loadMessages();
            Filter.reloadDict();
            CommandManager.registerShoutCommand();
            CommandManager.registerConnectCommand();
            CommandManager.registerPluginCommand();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BungeeShout getInstance() {
        return instance;
    }

    private static void setInstance(BungeeShout instance) {
        BungeeShout.instance = instance;
    }
}
