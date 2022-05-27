package com.develop.devfurniture.Events;

import com.develop.devfurniture.DevFurniture;
import com.develop.devfurniture.Loader.ConfigLoader;
import com.develop.devfurniture.Loader.ShopLoader;
import com.develop.devfurniture.Utils.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import static com.develop.devfurniture.DevFurniture.colorize;

public class PageChange implements Listener {

    @EventHandler
    public void PageChangeEvent(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(colorize(ConfigLoader.getShopGUIName()))) {
            event.setCancelled(true);
            if (!DevFurniture.getEnabled()) {
                event.getWhoClicked().sendMessage(colorize("&cPlease wait until Plugin is done load data!"));
                return;
            }
            if (event.getCurrentItem() != null) {
                ItemBuilder builder = new ItemBuilder(event.getCurrentItem());
                if (builder.getStringNBT("IsNext").equalsIgnoreCase("true")) {
                    int Page = builder.getIntegerNBT("Page") + 1;
                    if (Page <= ShopLoader.getMaxPage()) {
                        event.getWhoClicked().openInventory(ShopLoader.getGUI().get(Page-1));
                        ShopLoader.getPlayerPage().replace((Player) event.getWhoClicked(), Page-1);
                    }
                }
                if (builder.getStringNBT("IsPrevious").equalsIgnoreCase("true")) {
                    int Page = builder.getIntegerNBT("Page") - 1;
                    if (Page > 0) {
                        event.getWhoClicked().openInventory(ShopLoader.getGUI().get(Page-1));
                        ShopLoader.getPlayerPage().replace((Player) event.getWhoClicked(), Page-1);
                    }
                }
            }
        }
    }

}
