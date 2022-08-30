package com.develop.devfurniture.Events;

import com.develop.devfurniture.DevFurniture;
import com.develop.devfurniture.Loader.ConfigLoader;
import com.develop.devfurniture.Loader.ShopLoader;
import com.develop.devfurniture.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import static com.develop.devfurniture.DevFurniture.colorize;

public class BuyConfirmationEvent implements Listener {

    @EventHandler
    public void onConfirmationClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) { return; }
        if (event.getCurrentItem().getType() == Material.AIR) { return; }
        if (event.getClickedInventory() == null) { return; }
        if (event.getView().getTitle().equalsIgnoreCase(colorize(ConfigLoader.getConfirmationGUIName()))) {
            event.setCancelled(true);
            if(event.getClickedInventory().getType() == InventoryType.PLAYER){ return;}
            Player player = (Player) event.getWhoClicked();
            if (!DevFurniture.getEnabled()) {
                event.getWhoClicked().sendMessage(colorize("&cPlease wait until Plugin is done load data!"));
                return;
            }
            if (event.getCurrentItem() == null) {
                return;
            }
            if (new ItemBuilder(event.getCurrentItem()).getStringNBT("IsAccept").equalsIgnoreCase("true")) {
                if (event.getClickedInventory() == null) {
                    return;
                }
                ItemStack item = event.getClickedInventory().getItem(13);
                if (item == null) {
                    return;
                }
                ItemBuilder builder = new ItemBuilder(item);
                if (ConfigLoader.getFurnitureKeyList().contains(builder.getStringNBT("ShopKey"))) {
                    String key = builder.getStringNBT("ShopKey");
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(colorize(ConfigLoader.getConfig().getString("Language.Not-Enough-Inventory")));
                    } else {
                        if (DevFurniture.getEconomy().getBalance(Bukkit.getOfflinePlayer(player.getUniqueId())) >= ConfigLoader.getFurniturePrice().get(key)) {
                            ItemStack toGive = ConfigLoader.getFurnitureCustomStack().get(key).getItemStack().clone();
                            DevFurniture.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), ConfigLoader.getFurniturePrice().get(key));
                            player.getInventory().addItem(toGive);
                            player.sendMessage(colorize(ConfigLoader.getConfig().getString("Language.Succeeded", "Succeeded!").replace("<item>", builder.getDisplayName())));
                        } else {
                          player.sendMessage(colorize(ConfigLoader.getConfig().getString("Language.Not-Enough-Money")));
                      }
                    }
                }
            } else if (new ItemBuilder(event.getCurrentItem()).getStringNBT("IsDeny").equalsIgnoreCase("true")) {
                if (ShopLoader.getPlayerPage().containsKey(player)) {
                    if (ShopLoader.getPlayerPage().get(player) > ShopLoader.getMaxPage() - 1) {
                        player.openInventory(ShopLoader.getGUI().get(ShopLoader.getMaxPage() - 1));
                        ShopLoader.getPlayerPage().replace(player, ShopLoader.getMaxPage() - 1);
                    } else if (ShopLoader.getPlayerPage().get(player) < 0) {
                        player.openInventory(ShopLoader.getGUI().get(0));
                        ShopLoader.getPlayerPage().replace(player, 0);
                    } else {
                        player.openInventory(ShopLoader.getGUI().get(ShopLoader.getPlayerPage().get(player)));
                    }
                } else {
                    player.openInventory(ShopLoader.getGUI().get(0));
                    ShopLoader.getPlayerPage().put(player, 0);
                }
            }
        }
    }
}
