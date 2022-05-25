package com.develop.devfurniture.Utils;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static com.develop.devfurniture.DevFurniture.colorize;

public class ItemBuilder {

    private final ItemStack itemStack;
    private ItemMeta itemMeta;
    private NBTItem itemNBT;
    private Integer amount;

    public ItemBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
        this.itemMeta = this.itemStack.getItemMeta();
        this.itemNBT = new NBTItem(this.itemStack);
        this.amount = amount;
    }

    public ItemBuilder(ItemStack stack) {
        this.itemStack = stack;
        this.itemMeta = this.itemStack.getItemMeta();
        this.itemNBT = new NBTItem(this.itemStack);
        this.amount = stack.getAmount();
    }

    public ItemBuilder setDisplayName(String name) {
        this.itemMeta.setDisplayName(colorize(name));
        return this;
    }

    private void updateItemMeta() {
        this.itemStack.setItemMeta(this.itemMeta);
        this.itemStack.setAmount(this.amount);
        this.itemNBT = new NBTItem(this.itemStack);
    }

    public ItemBuilder setAmount(int amount) {
        if (amount > 64) {
            this.amount = 64;
        } else if (amount <= 0) {
            this.amount = 1;
        } else {
            this.amount = amount;
        }
        return this;
    }

    public ItemBuilder setModelData(Integer model) {
        this.itemMeta.setCustomModelData(model);
        return this;
    }

    public Integer getModelData() {
        if (this.itemMeta.hasCustomModelData()) {
            return this.itemMeta.getCustomModelData();
        }
        return 0;
    }

    public ItemBuilder setLoreByArray(ArrayList<String> lines) {
        this.itemMeta.setLore(lines);
        return this;
    }

    public ArrayList<String> getLore() {
        if (this.itemMeta.getLore() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(this.itemMeta.getLore());
    }

    public String getStringNBT(String key) {
        return this.itemNBT.getString(key);
    }

    public ItemBuilder setStringNBT(String path, String value) {
        updateItemMeta();
        this.itemNBT.setString(path, value);
        this.itemNBT.applyNBT(this.itemStack);
        this.itemMeta = this.itemStack.getItemMeta();
        return this;
    }

    public Integer getIntegerNBT(String key) {
        return this.itemNBT.getInteger(key);
    }

    public ItemBuilder setIntegerNBT(String path, Integer value) {
        updateItemMeta();
        this.itemNBT.setInteger(path, value);
        this.itemNBT.applyNBT(this.itemStack);
        this.itemMeta = this.itemStack.getItemMeta();
        return this;
    }

    public ItemBuilder setGlowing(Boolean glow) {
        if (glow) {
            this.itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
            this.itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            this.itemMeta.removeEnchant(Enchantment.PROTECTION_ENVIRONMENTAL);
            this.itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }

    public ItemStack build() {
        this.updateItemMeta();
        return this.itemStack;
    }

    public String getDisplayName() {
        if (this.itemMeta.hasDisplayName()) {
            return this.itemMeta.getDisplayName();
        } else {
            return this.itemMeta.getLocalizedName();
        }
    }

    public ItemBuilder addLore(ArrayList<String> addedLore) {
        ArrayList<String> lore = new ArrayList<>();
        lore.addAll(this.itemMeta.getLore());
        lore.addAll(addedLore);
        this.itemMeta.setLore(lore);
        return this;
    }
}
