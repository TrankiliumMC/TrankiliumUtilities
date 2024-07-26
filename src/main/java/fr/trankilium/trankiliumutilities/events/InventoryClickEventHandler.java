package fr.trankilium.trankiliumutilities.events;

import fr.trankilium.trankiliumutilities.globalUtils.ConfirmGUI;
import fr.trankilium.trankiliumutilities.utilities.ranks.gui.RanksGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryClickEventHandler implements Listener {
    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getHolder(false) instanceof RanksGUI) {
            RanksGUI.onClick(event);
        } else if (inventory.getHolder(false) instanceof ConfirmGUI) {
            event.setCancelled(true);
            ConfirmGUI menu = (ConfirmGUI) inventory.getHolder();
            if (menu != null) {
                menu.onClick(event.getSlot(), (Player) event.getWhoClicked());
            }
        }
    }
}