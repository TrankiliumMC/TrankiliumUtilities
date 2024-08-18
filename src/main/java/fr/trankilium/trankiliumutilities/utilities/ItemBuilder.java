package fr.trankilium.trankiliumutilities.utilities;

import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;

public class ItemBuilder {
    private final ItemStack is;

    public ItemBuilder(Material m) {
      this(m, 1);
    }

    public ItemBuilder(ItemStack is) {
      this.is = is;
    }

    public ItemBuilder(Material m, int amount) {
      this.is = new ItemStack(m, amount);
    }

    public ItemBuilder setName(Component name) {
      ItemMeta im = this.is.getItemMeta();
      im.displayName(name);
      this.is.setItemMeta(im);
      return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta im = this.is.getItemMeta();
        im.displayName(Component.text(name));
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {
      this.is.addUnsafeEnchantment(ench, level);
      return this;
    }

    public ItemBuilder removeEnchantment(Enchantment ench) {
      this.is.removeEnchantment(ench);
      return this;
    }

    public ItemBuilder setSkullOwner(OfflinePlayer owner) {
      try {
        SkullMeta im = (SkullMeta)this.is.getItemMeta();
        im.setOwningPlayer(owner);
        this.is.setItemMeta((ItemMeta)im);
      } catch (ClassCastException classCastException) {}
      return this;
    }

    public ItemBuilder setCustomModelData(int data) {
      ItemMeta im = this.is.getItemMeta();
      im.setCustomModelData(data);
      this.is.setItemMeta(im);
      return this;
    }

    public ItemBuilder addEnchant(Enchantment ench, int level, boolean visible) {
      ItemMeta im = this.is.getItemMeta();
      im.addEnchant(ench, level, visible);
      this.is.setItemMeta(im);
      return this;
    }

    public ItemBuilder setLore(Component... lore) {
      ItemMeta im = this.is.getItemMeta();
      im.lore(Arrays.asList(lore));
      this.is.setItemMeta(im);
      return this;
    }

    public ItemBuilder setLeatherArmorColor(Color color) {
      try {
        LeatherArmorMeta im = (LeatherArmorMeta)this.is.getItemMeta();
        im.setColor(color);
        this.is.setItemMeta((ItemMeta)im);
      } catch (ClassCastException ignored) {}
      return this;
    }

    public ItemBuilder addFlags(ItemFlag itemflag) {
      ItemMeta im = this.is.getItemMeta();
      im.addItemFlags(itemflag);
      this.is.setItemMeta(im);
      return this;
    }

    public ItemBuilder setUnbreakable(boolean b) {
      this.is.getItemMeta().setUnbreakable(b);
      return this;
    }

    public ItemStack toItemStack() {
      return this.is;
    }

}