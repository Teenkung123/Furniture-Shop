package com.develop.devfurniture.Utils;

import com.develop.devfurniture.DevFurniture;
import com.develop.devfurniture.Loader.ConfigLoader;
import com.develop.devfurniture.Loader.WorldGuardLoader;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TaskRunner {

    public static void RunRandomFurnitureTask() {
        Bukkit.getScheduler().runTaskTimer(DevFurniture.getInstance(), () -> {
            if (DevFurniture.getEnabled()) {
                ProtectedRegion pr = WorldGuardLoader.getRegionManager(ConfigLoader.getPreviewLocation().getWorld()).getRegion(ConfigLoader.getWorldGuardArea());
                if (pr != null) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (pr.contains(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ())) {
                            return;
                        }
                    }
                }
                PreviewItem.removePreviewFromLocation(ConfigLoader.getPreviewLocation());
                PreviewItem.addPreviewItem(ConfigLoader.getRandomFurniture(), ConfigLoader.getPreviewLocation());
            }
        }, 40, 40);
    }

}
