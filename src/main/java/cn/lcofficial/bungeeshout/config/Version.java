package cn.lcofficial.bungeeshout.config;

import cn.lcofficial.bungeeshout.BungeeShout;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.packet.Chat;

import javax.swing.*;
import java.lang.management.PlatformLoggingMXBean;

public class Version {

    public static final String NAME = BungeeShout.getInstance().getDescription().getName();
    public static final String AUTHOR = BungeeShout.getInstance().getDescription().getAuthor();
    public static final String VERSION = BungeeShout.getInstance().getDescription().getVersion();

    public static void showVersion(ProxiedPlayer player) {
        player.sendMessage(new TextComponent(ChatColor.GOLD + "==============================="));
        player.sendMessage(new TextComponent(ChatColor.AQUA + "     Name: " + NAME));
        player.sendMessage(new TextComponent(ChatColor.AQUA + "     Version: " + VERSION));
        player.sendMessage(new TextComponent(ChatColor.AQUA + "     Authors: " + AUTHOR));
        player.sendMessage(new TextComponent(ChatColor.RED +  "     Copyright LC 2020."));
        player.sendMessage(new TextComponent(ChatColor.GOLD + "==============================="));
    }

}
