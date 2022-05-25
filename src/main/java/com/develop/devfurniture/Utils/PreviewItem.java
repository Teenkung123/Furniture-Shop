package com.develop.devfurniture.Utils;

import com.develop.devfurniture.DevFurniture;
import dev.lone.itemsadder.api.CustomFurniture;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import static com.develop.devfurniture.DevFurniture.colorize;

public class PreviewItem {

    public static void addPreviewItem(String NameSpace, Location Location) {
        if (!DevFurniture.getEnabled()) {
            System.out.println(colorize("&cCould not perform task addPreviewItem due to plugin is not complete loading!"));
            return;
        }
        CustomFurniture furniture = CustomFurniture.spawnPreciseNonSolid(NameSpace, Location);
        if (furniture.getArmorstand() != null) {
            furniture.getArmorstand().setInvulnerable(true);
        }
    }

    public static void removePreviewFromLocation(Location Location) {
        if (!DevFurniture.getEnabled()) {
            System.out.println(colorize("&cCould not perform task removePreviewFromLocation due to plugin is not complete loading!"));
            return;
        }
        if (Location.getWorld() != null) {
            for (Entity entity : Location.getWorld().getNearbyEntities(Location, 1, 1, 1)) {
                if (entity.getType().equals(EntityType.ARMOR_STAND)) {
                    if (CustomFurniture.byAlreadySpawned((ArmorStand) entity) != null) {
                        entity.remove();
                    }
                }
            }
        }
    }

}
