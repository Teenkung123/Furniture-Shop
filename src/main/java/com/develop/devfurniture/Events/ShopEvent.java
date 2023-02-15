package com.develop.devfurniture.Events;

import com.develop.devfurniture.DevFurniture;
import com.develop.devfurniture.Loader.ConfigLoader;
import com.develop.devfurniture.Loader.ConfirmationGUILoader;
import com.develop.devfurniture.Utils.EconomyType;
import com.develop.devfurniture.Utils.ItemBuilder;
import com.develop.devfurniture.Utils.PreviewItem;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static com.develop.devfurniture.DevFurniture.colorize;
import static com.develop.devfurniture.DevFurniture.colorizeArray;

public class ShopEvent implements Listener {

    @EventHandler
    public void onShopInteract(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) { return; }
        if (event.getCurrentItem().getType() == Material.AIR) { return; }
        if (event.getView().getTitle().equalsIgnoreCase(colorize(ConfigLoader.getShopGUIName()))) {
            if (cancelEvent(event)) return;
            if (event.getSlot() >= 9 && event.getSlot() <= 44 && event.getCurrentItem() != null) {
                ItemBuilder builder = new ItemBuilder(event.getCurrentItem());
                if (builder.getStringNBT("ShopKey") != null) {
                    if (event.getClick().isLeftClick() && event.getClick().isShiftClick()) {
                        Inventory inv = ConfirmationGUILoader.getConfirmationGUI();
                        String key = builder.getStringNBT("ShopKey");
                        String tag;
                        if (ConfigLoader.getEconomyType().get(key).equals(EconomyType.BOTH)) {
                            tag = ConfigLoader.getConfig().getString("Language.Both", "")
                                    .replace("<vault>", ConfigLoader.getFurniturePrice().get(key) + ConfigLoader.getConfig().getString("Language.Vault"))
                                    .replace("<playerpoints>", ConfigLoader.getFurniturePricePlayerPoints().get(key) + ConfigLoader.getConfig().getString("Language.PlayerPoints"));
                            inv.setItem(16, ConfigLoader.getBuyWithVaultStack().setStringNBT("payMethod", "Vault").build());
                            inv.setItem(17, ConfigLoader.getBuyWithPointStack().setStringNBT("payMethod", "Point").build());
                        } else if (ConfigLoader.getEconomyType().get(key).equals(EconomyType.PLAYERPOINTS)) {
                            tag = ConfigLoader.getConfig().getString("Language.Single", "")
                                    .replace("<price>", ConfigLoader.getFurniturePricePlayerPoints().get(key) + ConfigLoader.getConfig().getString("Language.PlayerPoints"));
                            inv.setItem(16, ConfigLoader.getAcceptStack().build());
                            inv.setItem(17, ConfigLoader.getAcceptStack().build());
                        } else {
                            tag = ConfigLoader.getConfig().getString("Language.Single", "")
                                    .replace("<price>", ConfigLoader.getFurniturePrice().getOrDefault(key, 100000D) + ConfigLoader.getConfig().getString("Language.Vault", ""));
                            inv.setItem(16, ConfigLoader.getAcceptStack().build());
                            inv.setItem(17, ConfigLoader.getAcceptStack().build());
                        }
                        ArrayList<String> lores = new ArrayList<>();
                        lores.add(tag);
                        lores.addAll(ConfigLoader.getConfirmationGUILore());
                        ItemStack item = new ItemBuilder(ConfigLoader.getFurnitureCustomStack().get(builder.getStringNBT("ShopKey")).getItemStack().clone())
                                .setStringNBT("ShopKey", builder.getStringNBT("ShopKey"))
                                .addLore(colorizeArray(lores))
                                .build();
                        inv.setItem(13, item);
                        event.getWhoClicked().openInventory(inv);
                    } else if (event.getClick().isLeftClick()) {
                        event.getWhoClicked().closeInventory();
                        event.getWhoClicked().sendMessage(colorize(ConfigLoader.getConfig().getString("Language.Preview-Message", "&fYou have started preview furniture of <item>").replace("<item>", colorize(new ItemBuilder(ConfigLoader.getFurnitureCustomStack().get(builder.getStringNBT("ShopKey")).getItemStack()).getDisplayName()))));
                        PreviewItem.removePreviewFromLocation(ConfigLoader.getPreviewLocation());
                        PreviewItem.addPreviewItem(ConfigLoader.getFurnitureCustomStack().get(builder.getStringNBT("ShopKey")).getNamespacedID(), ConfigLoader.getPreviewLocation());
                    }
                }
            }
        }
    }

    static boolean cancelEvent(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getClickedInventory() == null) {
            return true;
        }
        if(event.getClickedInventory().getType() == InventoryType.PLAYER){
            return true;
        }
        if (!DevFurniture.getEnabled()) {
            event.getWhoClicked().sendMessage(colorize("&cPlease wait until Plugin is done load data!"));
            return true;
        }
        return false;
    }

}
