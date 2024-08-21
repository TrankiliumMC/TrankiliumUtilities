package fr.trankilium.trankiliumutilities.utilities;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Enumeration;

import static net.kyori.adventure.text.Component.text;

public class JoinAndLeaveMessages {
    public static void joinMessage(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.joinMessage(Component.text(""));
        if (player.isOp()) return;
        if (player.hasPermission("group.vip")) {
            Bukkit.getServer().broadcast(text(String.format("§7[§2+§7] §6✦ %s ✦ §as'est connecté !", player.getName())));
        } else {
            Bukkit.getServer().broadcast(text(String.format("§7[§2+§7] §a%s", player.getName())));
        }
    }

    public static void quitMessage(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.quitMessage(Component.text(""));
        if (player.isOp()) return;
        if (player.hasPermission("group.vip")) {
            Bukkit.getServer().broadcast(text(String.format("§7[§2+§7] §6✦ %s ✦ §as'est déconnecté !", player.getName())));
        } else {
            Bukkit.getServer().broadcast(text(String.format("§7[§2+§7] §a%s", player.getName())));
        }
    }
}
