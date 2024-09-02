package fr.trankilium.trankiliumutilities.events;

import fr.trankilium.trankiliumutilities.globalUtils.ConfirmGUI;
import fr.trankilium.trankiliumutilities.utilities.rubis.RubisGUI;
import fr.trankilium.trankiliumutilities.utilities.shops.GuiManagers;
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
        if (inventory.getHolder() instanceof RanksGUI) {
            RanksGUI.onClick(event);
        }
        if (inventory.getHolder() instanceof ConfirmGUI menu) {
            event.setCancelled(true);
            menu.onClick(event.getSlot(), (Player) event.getWhoClicked());
        }

        if(inventory.getHolder() instanceof GuiManagers.ShopGui) {
            GuiManagers.ShopGui.onClick(event);
        }

        if(inventory.getHolder() instanceof GuiManagers.CustomGui) {
            GuiManagers.CustomGui.onClick(event);
        }

        if(inventory.getHolder() instanceof RubisGUI) {
            RubisGUI.onClick(event);
        }
    }
}
