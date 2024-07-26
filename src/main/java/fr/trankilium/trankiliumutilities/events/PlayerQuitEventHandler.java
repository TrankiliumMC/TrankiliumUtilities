package fr.trankilium.trankiliumutilities.events;

import fr.trankilium.trankiliumutilities.utilities.JoinAndLeaveMessages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitEventHandler implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        JoinAndLeaveMessages.quitMessage(event);
    }
}
