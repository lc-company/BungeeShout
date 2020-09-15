package cn.lcofficial.bungeeshout.manager;

import cn.lcofficial.bungeeshout.config.Message;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    // 存储玩家冷却时间信息
    public static Map<ProxiedPlayer, Long> memoryCoolDown = new HashMap<>();

    public static boolean isPassed(ProxiedPlayer player) {

        long timestamp = new Date().getTime();
        if (memoryCoolDown.containsKey(player) && !player.hasPermission("bungeeshout.admin")) {
            if (player.hasPermission("bungeeshout.cooldown.60s") && (timestamp - memoryCoolDown.get(player) < (60 * 1000))) {
                player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', Message.CONSOLE_PREFIX) + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', Message.COOLDOWN_MESSAGE.replaceAll("%cooldown%", String.valueOf(timestamp - memoryCoolDown.get(player))))));
                return false;
            } else if (!player.hasPermission("bungeeshout.cooldown.bypass") && (timestamp - memoryCoolDown.get(player) < (180 * 1000))) {
                player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', Message.CONSOLE_PREFIX + ChatColor.RESET) + ChatColor.translateAlternateColorCodes('&', Message.COOLDOWN_MESSAGE.replaceAll("%cooldown%", String.valueOf((timestamp - memoryCoolDown.get(player)) / 1000)))));
                return false;
            } else if (player.hasPermission("bungeeshout.admin") || player.hasPermission("bungeeshout.cooldown.bypass")) removePlayer(player);
            else removePlayer(player);
        } else {
            registerPlayer(player, timestamp);
        }
        return true;
    }

    public static void registerPlayer(ProxiedPlayer player, long timestamp) {
        memoryCoolDown.put(player, timestamp);
    }

    public static void removePlayer(ProxiedPlayer player) {
        memoryCoolDown.remove(player);
    }

}
