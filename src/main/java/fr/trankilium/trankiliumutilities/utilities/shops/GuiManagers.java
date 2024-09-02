package fr.trankilium.trankiliumutilities.utilities.shops;

import fr.trankilium.trankiliumutilities.Main;
import fr.trankilium.trankiliumutilities.globalUtils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecation")
public class GuiManagers {

    public GuiManagers() {
        //new ShopGui();
        new CustomGui();
    }

    private static String convertColorsToMinecraftFormat(String text) {
        return text != null ? text.replace("&", "§") : null;
    }

    private static String convertColorsToConfigFormat(String text) {
        return text != null ? text.replace("§", "&") : null;
    }

    public static class ShopGui implements InventoryHolder {

        private Inventory inv;
        private File shopFile;
        private static FileConfiguration shopConfig;

        public ShopGui() {
            createShopConfig();
            loadCustomInventory();
        }

        private void createShopConfig() {
            shopFile = new File(Main.getDatafolder(), "shop.yml");
            if (!shopFile.exists()) {
                setDefaultShopConfig();
            } else {
                shopConfig = YamlConfiguration.loadConfiguration(shopFile);
            }
        }

        private void setDefaultShopConfig() {
            String invname = "§6§lShop";
            int slots = 6 * 9;
            inv = Bukkit.createInventory(this, slots, invname);

            shopConfig = new YamlConfiguration();
            shopConfig.set("inventory.name", convertColorsToConfigFormat(invname));
            shopConfig.set("inventory.size", slots);
            fillEmptySlots();

            ItemStack freeItem = new ItemBuilder(Material.DIRT_PATH).setName("§bObject Gratuit").setLore(
                    Component.text(" "),
                    Component.text("  §7Voici les objets gratuits du jour !"),
                    Component.text(" "),
                    Component.text("  §8§oClique pour ouvrir le menu !")
            ).toItemStack();

            inv.setItem(31, freeItem);

            saveItemToConfig(31, freeItem);
            saveFiles();
        }

        private void fillEmptySlots() {
            if (shopConfig != null && shopConfig.contains("inventory.fillemptyslot")) {
                String materialName = shopConfig.getString("inventory.fillemptyslot.material").toUpperCase();
                String name = convertColorsToMinecraftFormat(shopConfig.getString("inventory.fillemptyslot.name", " "));

                Material material = Material.valueOf(materialName);
                ItemStack itemStack = new ItemBuilder(material).setName(name).toItemStack();

                for (int i = 0; i < inv.getSize(); i++) {
                    if (inv.getItem(i) == null) {
                        inv.setItem(i, itemStack);
                    }
                }
            } else {
                Material defaultMaterial = Material.BLACK_STAINED_GLASS_PANE;
                String defaultName = " ";

                ItemStack defaultItemStack = new ItemBuilder(defaultMaterial).setName(defaultName).toItemStack();

                for (int i = 0; i < inv.getSize(); i++) {
                    if (inv.getItem(i) == null) {
                        inv.setItem(i, defaultItemStack);
                    }
                }

                if (shopConfig != null) {
                    shopConfig.set("inventory.fillemptyslot.material", defaultMaterial.name());
                    shopConfig.set("inventory.fillemptyslot.name", convertColorsToConfigFormat(defaultName));
                    saveFiles();
                }
            }
        }

        private void saveItemToConfig(int slot, ItemStack item) {
            String defaultMaterialConfig = shopConfig.getString("inventory.fillemptyslot.material");
            Material defaultMaterial = null;

            if (defaultMaterialConfig != null) {
                defaultMaterial = Material.getMaterial(defaultMaterialConfig.toUpperCase());
            }

            // Si l'item n'est pas nul et n'est pas égal au matériel par défaut (si défini)
            if (item != null && (defaultMaterial == null || item.getType() != defaultMaterial)) {
                shopConfig.set("inventory.items." + slot + ".slot", slot);
                shopConfig.set("inventory.items." + slot + ".material", item.getType().name().toUpperCase());
                shopConfig.set("inventory.items." + slot + ".amount", item.getAmount());
                shopConfig.set("inventory.items." + slot + ".permission", "default");

                // On récupère le nom du matériau pour les actions
                String materialName = item.getType().name().toUpperCase();
                shopConfig.set("inventory.items." + slot + ".action", "open freeitems");

                // Gestion des métadonnées de l'item
                if (item.hasItemMeta()) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta.hasDisplayName()) {
                        String displayName = convertColorsToConfigFormat(GsonComponentSerializer.gson().serialize(Objects.requireNonNull(meta.displayName())));
                        shopConfig.set("inventory.items." + slot + ".name", displayName);
                    }
                    if (meta.hasLore()) {
                        List<Component> lore = meta.lore();
                        if (lore != null) {
                            String[] loreConfig = lore.stream()
                                    .map(component -> convertColorsToConfigFormat(GsonComponentSerializer.gson().serialize(component)))
                                    .toArray(String[]::new);
                            shopConfig.set("inventory.items." + slot + ".lore", loreConfig);
                        }
                    }
                }
            }
        }

        private void loadCustomInventory() {
            inv = Bukkit.createInventory(this, shopConfig.getInt("inventory.size"), Objects.requireNonNull(convertColorsToMinecraftFormat(shopConfig.getString("inventory.name"))));
            if (shopConfig == null) createShopConfig();
            fillEmptySlots();

            if (shopConfig.contains("inventory.items")) {
                Objects.requireNonNull(shopConfig.getConfigurationSection("inventory.items")).getKeys(false).forEach(key -> {
                    String path = "inventory.items." + key;
                    Material material = Material.valueOf(shopConfig.getString(path + ".material").toUpperCase());
                    int amount = shopConfig.getInt(path + ".amount");
                    int slot = shopConfig.getInt(path + ".slot");

                    ItemBuilder itemBuilder = new ItemBuilder(material, amount);

                    if (shopConfig.contains(path + ".name")) {
                        String nameJson = shopConfig.getString(path + ".name");
                        Component name = GsonComponentSerializer.gson().deserialize(convertColorsToMinecraftFormat(nameJson));
                        itemBuilder.setName(name);
                    }

                    if (shopConfig.contains(path + ".lore")) {
                        List<String> loreJsonList = shopConfig.getStringList(path + ".lore");
                        List<Component> lore = loreJsonList.stream().map(loreJson -> GsonComponentSerializer.gson().deserialize(convertColorsToMinecraftFormat(loreJson))).toList();
                        itemBuilder.setLore(lore.toArray(new Component[0]));
                    }

                    ItemStack itemStack = itemBuilder.toItemStack();
                    inv.setItem(slot, itemStack);
                });
            }
        }

        private void saveFiles() {
            try {
                shopConfig.save(shopFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void onClick(InventoryClickEvent e) {
            e.setCancelled(true);

            Player p = (Player) e.getWhoClicked();
            int slot = e.getSlot();

            if (shopConfig.contains("inventory.items." + slot + ".action")) {
                String action = shopConfig.getString("inventory.items." + slot + ".action");

                if (action.startsWith("open")) {
                    String guiName = action.substring(5).trim(); // extrait le nom du GUI après "open"

                    if ("freeitems".equalsIgnoreCase(guiName)) {
                        CustomGui customGui = new CustomGui();
                        p.openInventory(customGui.getInventory());
                    } else {
                        Main.getInstance().getLogger().warning("Erreur action non reconnue: " + action);
                    }
                } else if (action.startsWith("close")) {
                    p.closeInventory();
                } else {
                    Main.getInstance().getLogger().warning("Erreur action non reconnue: " + action);
                }
            }
        }

        @Override
        public @NotNull Inventory getInventory() {
            return inv;
        }
    }

    public static class CustomGui implements InventoryHolder {

        private static Inventory inv;
        private File freeItemFile;
        private static FileConfiguration freeItemConfig;

        public CustomGui() {
            createShopConfig();
            loadCustomInventory();
        }

        private void createShopConfig() {
            freeItemFile = new File(Main.getDatafolder(), "freeitems.yml");
            if (!freeItemFile.exists()) {
                setDefaultShopConfig();
            } else {
                freeItemConfig = YamlConfiguration.loadConfiguration(freeItemFile);
            }
        }

        private void loadCustomInventory() {
            inv = Bukkit.createInventory(this, freeItemConfig.getInt("inventory.size"), Objects.requireNonNull(convertColorsToMinecraftFormat(freeItemConfig.getString("inventory.name"))));

            if (freeItemConfig == null) {
                createShopConfig();
            }

            if (freeItemConfig.contains("inventory.items")) {
                Objects.requireNonNull(freeItemConfig.getConfigurationSection("inventory.items")).getKeys(false).forEach(key -> {
                    String path = "inventory.items." + key;
                    Material material = Material.valueOf(freeItemConfig.getString(path + ".material").toUpperCase());
                    int amount = freeItemConfig.getInt(path + ".amount");
                    int slot = freeItemConfig.getInt(path + ".slot");

                    ItemBuilder itemBuilder = new ItemBuilder(material, amount);

                    if (freeItemConfig.contains(path + ".name")) {
                        String nameJson = freeItemConfig.getString(path + ".name");
                        Component name = GsonComponentSerializer.gson().deserialize(convertColorsToMinecraftFormat(nameJson));
                        itemBuilder.setName(name);
                    }

                    if (freeItemConfig.contains(path + ".lore")) {
                        List<String> loreJsonList = freeItemConfig.getStringList(path + ".lore");
                        List<Component> lore = loreJsonList.stream().map(loreJson -> GsonComponentSerializer.gson().deserialize(convertColorsToMinecraftFormat(loreJson))).toList();
                        itemBuilder.setLore(lore.toArray(new Component[0]));
                    }

                    ItemStack itemStack = itemBuilder.toItemStack();
                    inv.setItem(slot, itemStack);
                });
            }
        }

        private void setDefaultShopConfig() {
            String invname = "§6§lItems Gratuits";
            int slots = 9;
            inv = Bukkit.createInventory(this, slots, invname);

            freeItemConfig = new YamlConfiguration();
            freeItemConfig.set("inventory.name", convertColorsToConfigFormat(invname));
            freeItemConfig.set("inventory.size", slots);

            ItemStack dirt = new ItemBuilder(Material.DIRT).setName("§eTerre").setLore(
                    Component.text("  §7Clic gauche pour recevoir 1 terre"),
                    Component.text("  §7Clic droit pour recevoir 64 terres"),
                    Component.text(" ")
            ).toItemStack();

            ItemStack cobble = new ItemBuilder(Material.COBBLESTONE).setName("§ePierre").setLore(
                    Component.text("  §7Clic gauche pour recevoir 1 pierre"),
                    Component.text("  §7Clic droit pour recevoir 64 pierres"),
                    Component.text(" ")
            ).toItemStack();

            ItemStack sand = new ItemBuilder(Material.SAND).setName("§eSable").setLore(
                    Component.text("  §7Clic gauche pour recevoir 1 sable"),
                    Component.text("  §7Clic droit pour recevoir 64 sables"),
                    Component.text(" ")
            ).toItemStack();

            ItemStack echaf = new ItemBuilder(Material.SCAFFOLDING).setName("§eÉchaffaudage").setLore(
                    Component.text("  §7Clic gauche pour recevoir 1 échaffaudage"),
                    Component.text("  §7Clic droit pour recevoir 64 échaffaudages"),
                    Component.text(" ")
            ).toItemStack();

            ItemStack torche = new ItemBuilder(Material.TORCH).setName("§eTorche").setLore(
                    Component.text("  §8§7Clic gauche §7pour recevoir 1 torche"),
                    Component.text("  §8§7Clic droit §7pour recevoir 64 torches"),
                    Component.text(" ")
            ).toItemStack();

            fillEmptySlots();

            inv.setItem(2, dirt);
            inv.setItem(3, cobble);
            inv.setItem(4, sand);
            inv.setItem(5, echaf);
            inv.setItem(6, torche);

            saveItemToConfig(2, dirt);
            saveItemToConfig(3, cobble);
            saveItemToConfig(4, sand);
            saveItemToConfig(5, echaf);
            saveItemToConfig(6, torche);
            saveFiles();
        }

        private void saveItemToConfig(int slot, ItemStack item) {
            String defaultMaterialConfig = freeItemConfig.getString("inventory.fillemptyslot.material");
            Material defaultMaterial = null;

            if (defaultMaterialConfig != null) {
                defaultMaterial = Material.getMaterial(defaultMaterialConfig.toUpperCase());
            }

            // Si l'item n'est pas nul et n'est pas égal au matériel par défaut (si défini)
            if (item != null && (defaultMaterial == null || item.getType() != defaultMaterial)) {
                freeItemConfig.set("inventory.items." + slot + ".slot", slot);
                freeItemConfig.set("inventory.items." + slot + ".material", item.getType().name().toUpperCase());
                freeItemConfig.set("inventory.items." + slot + ".amount", item.getAmount());
                freeItemConfig.set("inventory.items." + slot + ".permission", "default");

                // On récupère le nom du matériau pour les actions
                String materialName = item.getType().name().toUpperCase();
                freeItemConfig.set("inventory.items." + slot + ".action-left", "give " + materialName + " 1");
                freeItemConfig.set("inventory.items." + slot + ".action-right", "give " + materialName + " 64");

                // Gestion des métadonnées de l'item
                if (item.hasItemMeta()) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta.hasDisplayName()) {
                        String displayName = convertColorsToConfigFormat(GsonComponentSerializer.gson().serialize(Objects.requireNonNull(meta.displayName())));
                        freeItemConfig.set("inventory.items." + slot + ".name", displayName);
                    }
                    if (meta.hasLore()) {
                        List<Component> lore = meta.lore();
                        if (lore != null) {
                            String[] loreConfig = lore.stream()
                                    .map(component -> convertColorsToConfigFormat(GsonComponentSerializer.gson().serialize(component)))
                                    .toArray(String[]::new);
                            freeItemConfig.set("inventory.items." + slot + ".lore", loreConfig);
                        }
                    }
                }
            }
        }

        private void saveFiles() {
            try {
                freeItemConfig.save(freeItemFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public InventoryHolder getHolder() {
            return this;
        }

        @Override
        public @NotNull Inventory getInventory() {
            return inv;
        }

        public static void onClick(InventoryClickEvent e) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            int slot = e.getSlot();
            ItemStack clickedItem = e.getCurrentItem();

            // Assurez-vous que l'item cliqué n'est pas nul
            if (clickedItem == null || !clickedItem.hasItemMeta()) {
                return;
            }

            ItemMeta meta = clickedItem.getItemMeta();
            String action = null;

            if (e.getAction() == InventoryAction.PICKUP_ALL) {
                if (freeItemConfig.contains("inventory.items." + slot + ".action-left")) {
                    action = freeItemConfig.getString("inventory.items." + slot + ".action-left");
                }
            } else if (e.getAction() == InventoryAction.PICKUP_HALF) {
                if (freeItemConfig.contains("inventory.items." + slot + ".action-right")) {
                    action = freeItemConfig.getString("inventory.items." + slot + ".action-right");
                }
            } else {
                return;
            }

            if (action != null) {
                String[] actionParts = action.split(" ");
                String command = actionParts[0];
                String materialName = actionParts.length > 1 ? actionParts[1] : "";
                int amount = actionParts.length > 2 ? Integer.parseInt(actionParts[2]) : 1; // Amount is optional, default to 1

                switch (command.toLowerCase()) {
                    case "open":
                        // Ouvrir un autre GUI
                        if ("freeitems".equalsIgnoreCase(materialName)) {
                            CustomGui customGui = new CustomGui();
                            p.openInventory(customGui.getInventory());
                        } else {
                            Main.getInstance().getLogger().warning("Erreur action non reconnue pour ouvrir: " + action);
                        }
                        break;

                    case "close":
                        p.closeInventory();
                        break;

                    case "give":
                        // Vérifier les permissions
                        if (meta.hasLore()) {
                            List<Component> lore = meta.lore();
                            String requiredPermission = "default"; // Valeur par défaut

                            for (Component component : lore) {
                                String loreText = GsonComponentSerializer.gson().serialize(component);
                                if (loreText.startsWith("Permission: ")) {
                                    requiredPermission = loreText.substring("Permission: ".length()).trim();
                                    break;
                                }
                            }

                            if (p.hasPermission(requiredPermission)) {
                                Material material = Material.getMaterial(materialName.toUpperCase());

                                if (material != null) {
                                    ItemStack itemStack = new ItemStack(material, amount);
                                    HashMap<Integer, ItemStack> excessItems = p.getInventory().addItem(itemStack);

                                    String formattedName = formatMaterialName(material.name());

                                    if (excessItems.isEmpty()) {
                                        p.sendMessage("§6§l[Shop] §ax" + amount + " §a" + formattedName);
                                    } else {
                                        p.sendMessage("§6§l[Shop] §6Ton inventaire est plein.");
                                    }
                                } else {
                                    Main.getInstance().getLogger().warning("Matériel invalide: " + materialName);
                                }
                            } else {
                                p.sendMessage("§6§l[Shop] §cTu n'as pas la permission de recevoir cet item.");
                            }
                        }
                        break;
                    default:
                        Main.getInstance().getLogger().warning("Erreur action non reconnue: " + action);
                        break;
                }
            }
        }

        private void fillEmptySlots() {
            if (freeItemConfig != null && freeItemConfig.contains("inventory.fillemptyslot")) {
                String materialName = freeItemConfig.getString("inventory.fillemptyslot.material").toUpperCase();
                String name = convertColorsToMinecraftFormat(freeItemConfig.getString("inventory.fillemptyslot.name", " "));

                Material material = Material.valueOf(materialName);
                ItemStack itemStack = new ItemBuilder(material).setName(name).toItemStack();

                for (int i = 0; i < inv.getSize(); i++) {
                    if (inv.getItem(i) == null) {
                        inv.setItem(i, itemStack);
                    }
                }
            } else {
                Material defaultMaterial = null;
                String defaultName = " ";

                if (defaultMaterial == null) return;

                ItemStack defaultItemStack = new ItemBuilder(defaultMaterial).setName(defaultName).toItemStack();

                for (int i = 0; i < inv.getSize(); i++) {
                    if (inv.getItem(i) == null) {
                        inv.setItem(i, defaultItemStack);
                    }
                }

                if (freeItemConfig != null) {
                    freeItemConfig.set("inventory.fillemptyslot.material", defaultMaterial.name());
                    freeItemConfig.set("inventory.fillemptyslot.name", convertColorsToConfigFormat(defaultName));
                    saveFiles();
                }
            }
        }
    }

    private static String formatMaterialName(String materialName) {
        String[] words = materialName.split("_");
        StringBuilder formattedName = new StringBuilder();
        for (String word : words) {
            if (!formattedName.isEmpty()) {
                formattedName.append(" ");
            }
            formattedName.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase());
        }
        return formattedName.toString();
    }
}