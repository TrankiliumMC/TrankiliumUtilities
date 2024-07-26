package fr.trankilium.trankiliumutilities.globalUtils;

import me.clip.placeholderapi.libs.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

import static java.awt.SystemColor.text;
import static net.kyori.adventure.text.Component.text;

public class ConfirmGUI implements InventoryHolder {
    private final Inventory inventory;
    private final Consumer<Boolean> action;

    public ConfirmGUI(Player player, Consumer<Boolean> action) {
        this.action = action;
        this.inventory = Bukkit.createInventory(this, 3*9, text("§eEs-tu sûr ?"));
        this.inventory.setItem(12, createItem(Material.LIME_TERRACOTTA, "§aConfirmer"));
        this.inventory.setItem(14, createItem(Material.RED_TERRACOTTA, "§cAnnuler"));
        player.openInventory(this.inventory);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    private ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(text(name));
            item.setItemMeta(meta);
        }
        return item;
    }

    public void onClick(int slot, Player player) {
        if (slot == 12) {
            action.accept(true);
            player.closeInventory();
        } else if (slot == 14) {
            action.accept(false);
            player.closeInventory();
        }
    }
}
