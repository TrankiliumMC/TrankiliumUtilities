package fr.trankilium.trankiliumutilities.events;

import fr.trankilium.trankiliumutilities.utilities.JoinAndLeaveMessages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventHandler implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        JoinAndLeaveMessages.joinMessage(event);
    }
}
