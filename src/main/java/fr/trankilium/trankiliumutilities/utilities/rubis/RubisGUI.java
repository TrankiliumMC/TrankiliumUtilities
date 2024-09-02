package fr.trankilium.trankiliumutilities.utilities.rubis;

import fr.trankilium.trankiliumutilities.globalUtils.ItemBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

public class RubisGUI implements InventoryHolder {

    private final Inventory inventory;

    public RubisGUI(Player player) {
        this.inventory = Bukkit.createInventory(this, 4*9, text("Grades", NamedTextColor.GOLD, TextDecoration.BOLD));

        ItemStack rubyItem = new ItemBuilder(Material.PLAYER_HEAD)
                .setName("§c§lRubis")
                .setSkullUrl("http://textures.minecraft.net/texture/2530191500c2453624dd937ec125d44f0942cc2b664073e2a366b3fa67a0c897")
                .setLore(text(PlaceholderAPI.setPlaceholders(player, "§eTu as §a%rubis_balance% Rubis§e.")),
                        text("§7Obtient des Rubis grâce au PVE, en"),
                        text("§7votant, ou en en achetant sur la"),
                        text("§7boutique pour soutenir le serveur !"),
                        text("§e[Lien de la Boutique]"))
                .toItemStack();
        this.inventory.setItem(4, rubyItem);

        ItemStack keyItem = new ItemBuilder(Material.TRIPWIRE_HOOK)
                .setName("§6Clés de Looboxs")
                .setLore(text("§7Acheter des Clés pour les Lootboxs."),
                        text("§e[Voir les Clés]"))
                .toItemStack();
        this.inventory.setItem(20, keyItem);

        ItemStack claimsItem = new ItemBuilder(Material.STRUCTURE_VOID)
                .setName("§3Claims")
                .setLore(text(PlaceholderAPI.setPlaceholders(player, "§eTu utilises actuellement §a%scs_player_claims_count%/%scs_player_max_claims% §e claims.")),
                        text("§7Tu peux acheter des claims supplémentaires"),
                        text("§ePrix du prochain claim : §a" + 999 + " Rubis"),
                        text("§e[Clique pour acheter]"))
                .toItemStack();
        this.inventory.setItem(22, claimsItem);

        ItemStack potionItem = new ItemBuilder(Material.POTION)
                .setName("§aPotions Double XP")
                .setLore(text("§7Acheter des potions doublant l'XP"),
                        text("§7des métiers."),
                        text("§e[Voir les Potions]"))
                .setPotionColor(Color.GREEN)
                .toItemStack();
        this.inventory.setItem(24, potionItem);
    }

    public static void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) { return; }
        if (event.getSlot() == 4) {
            player.sendMessage("§6§l[Trankilium] §eRends toi sur la Boutique pour soutenir le serveur : §ahttps://shop.trankilium.fr/");
            player.closeInventory();
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

}
