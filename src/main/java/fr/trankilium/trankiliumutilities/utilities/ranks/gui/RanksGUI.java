package fr.trankilium.trankiliumutilities.utilities.ranks.gui;


import fr.trankilium.trankiliumutilities.globalUtils.ConfirmGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.Component.text;

public class RanksGUI implements InventoryHolder {

    private final Inventory inventory;

    public RanksGUI(Player player) {
        this.inventory = Bukkit.createInventory(this, 3*9, text("Grades", NamedTextColor.GOLD, TextDecoration.BOLD));
        this.inventory.setItem(10, getRankItem(Material.YELLOW_TERRACOTTA,
                "§e§lDivin",
                false,
                new RankFile().getInt("ranks.gui.price.divin"),
                player));
        this.inventory.setItem(12, getRankItem(Material.BLUE_TERRACOTTA,
                "§9§lCeleste",
                false,
                new RankFile().getInt("ranks.gui.price.celeste"),
                player));

        this.inventory.setItem(14, getRankItem(Material.RED_TERRACOTTA,
                "§c§lTranscendant",
                false,
                new RankFile().getInt("ranks.gui.price.transcendant"),
                player));
        this.inventory.setItem(16, getRankItem(Material.GOLD_BLOCK,
                "§6§lVIP",
                true,
                new RankFile().getInt("ranks.gui.price.vip"),
                player));
    }
    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    private ItemStack getRankItem(Material material, String name, boolean isPaidRank, int price, Player player) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.displayName(text(name));
        List<Component> lore = new ArrayList<>();

        if (!isPaidRank) {
            lore.add(text("§7Ce grade est achetable avec").decoration(TextDecoration.ITALIC, false));
            lore.add(text("§7l'argent de l'économie du").decoration(TextDecoration.ITALIC, false));
            lore.add(text("§7serveur. Tu trouveras plus").decoration(TextDecoration.ITALIC, false));
            lore.add(text("§7d'infos sur ce qu'il apporte").decoration(TextDecoration.ITALIC, false));
            lore.add(text("§7juste à gauche d'ici.").decoration(TextDecoration.ITALIC, false));
            lore.add(text(" ").decoration(TextDecoration.ITALIC, false));
            if (player.hasPermission("group.vip")) {
                int discountedPrice = (int) (price * (1 - new RankFile().getDouble("ranks.gui.vip-discount") / 100));
                lore.add(text(String.format("§7Prix : §c§m%s ⛃§r §a%s ⛃", price, discountedPrice)).decoration(TextDecoration.ITALIC, false));
                    lore.add(text(String.format("§8-%d%% avec le grade VIP", new RankFile().getInt("ranks.gui.vip-discount"))).decoration(TextDecoration.ITALIC, false));
            } else {
                lore.add(text(String.format("§7Prix : §a%s ⛃", price)).decoration(TextDecoration.ITALIC, false));
            }
            lore.add(text("§e[Acheter]").decoration(TextDecoration.ITALIC, false));
        } else {
            lore.add(text("§7Ce grade est achetable avec").decoration(TextDecoration.ITALIC, false));
            lore.add(text("§7de l'§nargent réel§r§7. L'acheter est").decoration(TextDecoration.ITALIC, false));
            lore.add(text("§7une manière de soutenir le serveur").decoration(TextDecoration.ITALIC, false));
            lore.add(text("§7et d'assurer sa pérénité.").decoration(TextDecoration.ITALIC, false));
            lore.add(text(" ").decoration(TextDecoration.ITALIC, false));
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            lore.add(text(String.format("§7Prix : §a%s€", decimalFormat.format(new RankFile().getDouble("ranks.gui.price.vip")))).decoration(TextDecoration.ITALIC, false));
            lore.add(text("§8Tu as déjà le grade VIP. Merci !").decoration(TextDecoration.ITALIC, false));
            lore.add(text(" ").decoration(TextDecoration.ITALIC, false));
            lore.add(text("§e[Lien de la Boutique]").decoration(TextDecoration.ITALIC, false));
        }

        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        String rank = switch (event.getSlot()) {
            case 10 -> "divin";
            case 12 -> "celeste";
            case 14 -> "transcendant";
            default -> null;
        };

        if (rank != null) {

            // Check l'argent du joueur ici (pas fait)
            // ...

            new ConfirmGUI(player, confirmed -> {
                if (confirmed) {
                    player.sendMessage("§aAchat effectué !");
                    // Ajouter la logique d'achat de grade ici : groupe Luckperms + retrait argent si assez argent
                } else {
                    player.sendMessage("§cAchat annulé.");
                }
            });
        }
    }
}
