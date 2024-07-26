package fr.trankilium.trankiliumutilities.utilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static net.kyori.adventure.text.Component.text;

public class JoinAndLeaveMessages {
    public static void joinMessage(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.isOp()) {
            return;
        } else if (player.hasPermission("group.vip")) {
            Bukkit.getServer().broadcast(text(String.format("§7[§2+§7] §6✦%s✦ §as'est connecté !", player.getName())));
        } else {
            Bukkit.getServer().broadcast(text(String.format("§7[§2+§7] §a%s", player.getName())));
        }
    }

    public static void quitMessage(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player.isOp()) {
            return;
        } else {
            Bukkit.getServer().broadcast(text(String.format("§7[§4+§7] §c%s", player.getName())));
        }
    }

}
