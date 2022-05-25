package com.develop.devfurniture.Events;

import com.develop.devfurniture.DevFurniture;
import com.develop.devfurniture.Loader.ConfigLoader;
import com.develop.devfurniture.Loader.ShopLoader;
import com.develop.devfurniture.Loader.WorldGuardLoader;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dev.lone.itemsadder.api.CustomFurniture;
import org.bukkit.GameMode;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import static com.develop.devfurniture.DevFurniture.colorize;

public class OpenShopEvent implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!DevFurniture.getEnabled()) {
            return;
        }
        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            ProtectedRegion pr = WorldGuardLoader.getRegionManager(ConfigLoader.getPreviewLocation().getWorld()).getRegion(ConfigLoader.getWorldGuardArea());
            if (pr != null) {
                Player player = event.getPlayer();
                if (pr.contains(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ())) {
                    if (!DevFurniture.getEnabled()) {
                        event.getPlayer().sendMessage(colorize("&cPlease wait until Plugin is done load data!"));
                        return;
                    }
                    if (ShopLoader.getPlayerPage().containsKey(event.getPlayer())) {
                        if (ShopLoader.getPlayerPage().get(event.getPlayer()) > ShopLoader.getMaxPage() - 1) {
                            event.getPlayer().openInventory(ShopLoader.getGUI().get(ShopLoader.getMaxPage() - 1));
                            ShopLoader.getPlayerPage().replace(event.getPlayer(), ShopLoader.getMaxPage() - 1);
                        } else if (ShopLoader.getPlayerPage().get(event.getPlayer()) < 0) {
                            event.getPlayer().openInventory(ShopLoader.getGUI().get(0));
                            ShopLoader.getPlayerPage().replace(event.getPlayer(), 0);
                        } else {
                            event.getPlayer().openInventory(ShopLoader.getGUI().get(ShopLoader.getPlayerPage().get(event.getPlayer())));
                        }
                    } else {
                        event.getPlayer().openInventory(ShopLoader.getGUI().get(0));
                        ShopLoader.getPlayerPage().put(event.getPlayer(), 0);
                    }
                }
            }
        }
    }

    @EventHandler
    public void damage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (!DevFurniture.getEnabled()) { return; }
            if (event.getEntity().getType().equals(EntityType.ARMOR_STAND)) {
                if (CustomFurniture.byAlreadySpawned((ArmorStand) event.getEntity()) != null) {
                    ProtectedRegion pr = WorldGuardLoader.getRegionManager(ConfigLoader.getPreviewLocation().getWorld()).getRegion(ConfigLoader.getWorldGuardArea());
                    if (pr != null) {
                        Player player = (Player) event.getDamager();
                        if (player.getGameMode().equals(GameMode.CREATIVE)) {
                            player.sendMessage(colorize("&aBypassed Entity Damage Event Because GameMode Creative"));
                            return;
                        }
                        event.setCancelled(true);
                        if (pr.contains(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ())) {
                            if (!DevFurniture.getEnabled()) {
                                player.sendMessage(colorize("&cPlease wait until Plugin is done load data!"));
                                return;
                            }
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
        }
    }
}
