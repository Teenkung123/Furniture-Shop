package com.develop.devfurniture.Events;

import com.develop.devfurniture.DevFurniture;
import com.develop.devfurniture.Loader.ConfigLoader;
import com.develop.devfurniture.Loader.ConfirmationGUILoader;
import com.develop.devfurniture.Utils.ItemBuilder;
import com.develop.devfurniture.Utils.PreviewItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.develop.devfurniture.DevFurniture.colorize;

public class ShopEvent implements Listener {

    @EventHandler
    public void onShopInteract(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(colorize(ConfigLoader.getShopGUIName()))) {
            event.setCancelled(true);
            if (!DevFurniture.getEnabled()) {
                event.getWhoClicked().sendMessage(colorize("&cPlease wait until Plugin is done load data!"));
                return;
            }
            if (event.getSlot() >= 9 && event.getSlot() <= 44 && event.getCurrentItem() != null) {
                ItemBuilder builder = new ItemBuilder(event.getCurrentItem());
                if (builder.getStringNBT("ShopKey") != null) {
                    if (event.getClick().isLeftClick() && event.getClick().isShiftClick()) {
                        Inventory inv = ConfirmationGUILoader.getConfirmationGUI();
                        ItemStack item = new ItemBuilder(ConfigLoader.getFurnitureCustomStack().get(builder.getStringNBT("ShopKey")).getItemStack().clone())
                                .setStringNBT("ShopKey", builder.getStringNBT("ShopKey"))
                                .addLore(DevFurniture.colorizeArray(DevFurniture.replaceArray(ConfigLoader.getConfirmationGUILore(), "<price>", ConfigLoader.getFurniturePrice().get(builder.getStringNBT("ShopKey")).toString())))
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

}
