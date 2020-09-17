package cn.lcofficial.bungeeshout.manager;

import cn.lcofficial.bungeeshout.BungeeShout;
import cn.lcofficial.bungeeshout.config.Config;
import cn.lcofficial.bungeeshout.config.Filter;
import cn.lcofficial.bungeeshout.config.Message;
import cn.lcofficial.bungeeshout.config.Version;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.protocol.packet.Chat;

import java.io.IOException;

public class CommandManager {

    public static void registerShoutCommand() {
        for (String commandName : Config.commands) {
            BungeeShout.
                    getInstance().
                    getProxy().
                    getPluginManager().
                    registerCommand(BungeeShout.getInstance(), new Command(commandName) {
                @Override
                public void execute(CommandSender sender, String[] args) {
                    if (sender instanceof ProxiedPlayer) {
                        if (sender.hasPermission("bungeeshout.shout")) {
                            if (args.length > 0) {
                                String content = ChatColor.translateAlternateColorCodes('&', Message.prefix)
                                        + ChatColor.RESET
                                        + ChatColor.translateAlternateColorCodes('&', Message.playerColor)
                                        + sender.getName()
                                        + ": "
                                        + ChatColor.translateAlternateColorCodes('&', Message.messageColor);
                                StringBuilder message = new StringBuilder();
                                for (String arg : args) message.append(arg);
                                content += message;

                                if (!Filter.check(message.toString()) && !sender.hasPermission("bungeeshout.bypass.filter")) {
                                    sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', Message.CONSOLE_PREFIX) + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', Message.FILTER_CHECK_MESSAGE.
                                            replaceAll("%word%", Filter.get(message.toString())))));
                                    return;
                                }

                                if (!Config.urlAccess && !sender.hasPermission("bungeeshout.bypass.url")) {
                                    if (message.toString().contains("http://") || message.toString().contains("https://")) {
                                        sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', Message.CONSOLE_PREFIX) + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', Message.URL_NOT_ALLOW_MESSAGE)));
                                        return;
                                    }
                                }

                                if (!PlayerManager.isPassed((ProxiedPlayer) sender))
                                    return;

                                TextComponent shoutContent = new TextComponent(content);

                                TextComponent connectContent = new TextComponent(ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', Message.connectText));
                                connectContent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/connect:" + ((ProxiedPlayer) sender).getServer().getInfo().getName()));
                                TextComponent[] hoverText = new TextComponent[1];
                                hoverText[0] = new TextComponent(((ProxiedPlayer) sender).getServer().getInfo().getName());
                                connectContent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));

                                for (ProxiedPlayer player : BungeeShout.getInstance().getProxy().getPlayers()) {
                                    player.sendMessage(shoutContent, new TextComponent("    "), connectContent);
                                }
                                BungeeShout.getInstance().getLogger().info(content);
                            } else {
                                sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', Message.CONSOLE_PREFIX) + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', Message.NONE_ERROR_MESSAGE)));
                            }
                        } else {
                            sender.sendMessage(new TextComponent(ChatColor.RED + "Only player can do this!"));
                        }
                    } else {
                        sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', Message.NO_PERM_MESSAGE)));
                    }
                }
            });
        }
    }

    public static void registerPluginCommand() {

        BungeeShout.getInstance().getProxy().getPluginManager().registerCommand(BungeeShout.getInstance(), new Command("bungeeshout") {
            @Override
            public void execute(CommandSender sender, String[] args) {
                try {
                    if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                        BungeeShout.getInstance().getLogger().info(ChatColor.translateAlternateColorCodes('&', Message.CONSOLE_PREFIX) + ChatColor.translateAlternateColorCodes('&', Message.START_RELOADING_MESSAGE));
                        sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', Message.CONSOLE_PREFIX) + ChatColor.translateAlternateColorCodes('&', Message.START_RELOADING_MESSAGE)));

                        Filter.reloadDict();
                        Config.loadConfig();
                        registerShoutCommand();

                        sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', Message.CONSOLE_PREFIX) + ChatColor.translateAlternateColorCodes('&', Message.RELOADED_MESSAGE)));
                        BungeeShout.getInstance().getLogger().info(ChatColor.translateAlternateColorCodes('&', Message.CONSOLE_PREFIX) + ChatColor.translateAlternateColorCodes('&', Message.RELOADED_MESSAGE));
                    } else {
                        Version.showVersion((ProxiedPlayer) sender);
                    }
                } catch (IOException e) {
                    BungeeShout.getInstance().getLogger().info(e.getLocalizedMessage());
                }
            }
        });
    }

    public static void registerConnectCommand() {
        for (String target : BungeeShout.getInstance().getProxy().getServers().keySet()) {
            BungeeShout.getInstance().getProxy().getPluginManager().registerCommand(BungeeShout.getInstance(), new Command("connect:" + target) {
                @Override
                public void execute(CommandSender sender, String[] args) {
                    ServerInfo server = BungeeShout.getInstance().getProxy().getServers().get(target);
                    if (sender instanceof  ProxiedPlayer) {
                        if (server.canAccess(sender)) {
                            ((ProxiedPlayer) sender).connect(server);
                            BungeeShout.getInstance().getLogger().info("Player " + ((ProxiedPlayer) sender).getDisplayName() + " has connect to server: " + target);
                        } else {
                            TextComponent text = new TextComponent(ChatColor.translateAlternateColorCodes('&', Message.CONNECT_ERROR));
                            sender.sendMessage(text);
                        }
                    } else sender.sendMessage(new TextComponent(ChatColor.RED + "Only player can do this!"));
                }
            });
        }
    }
}
