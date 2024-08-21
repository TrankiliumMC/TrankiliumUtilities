package fr.trankilium.trankiliumutilities.shops;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class ShopCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if(player.hasPermission("shoptk.use")) {
                Inventory inv = new GuiManagers.CustomGui().getInventory();
                player.openInventory(inv);
            } else {
                player.sendMessage("§cTu n'as pas la permission d'utilisé le Shop.");
            }
        }
        return false;
    }
}
