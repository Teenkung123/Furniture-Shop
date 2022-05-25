package com.develop.devfurniture.Loader;

import com.develop.devfurniture.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import static com.develop.devfurniture.DevFurniture.colorize;

public class ConfirmationGUILoader {

    public static Inventory getConfirmationGUI() {
        Inventory GUI = Bukkit.createInventory(null, 27, colorize(ConfigLoader.getConfirmationGUIName()));
        for (int i = 0 ; i < 27 ; i++) {
            if (i >= 9 && i <= 17) {
                if (i == 9 || i == 10) {
                    GUI.setItem(i, ConfigLoader.getDenyStack().build());
                } else if (i == 16 || i == 17) {
                    GUI.setItem(i , ConfigLoader.getAcceptStack().build());
                } else {
                    GUI.setItem(i, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE, 1).setDisplayName(" ").build());
                }
            } else {
                GUI.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName(" ").build());
            }
        }
        return GUI;
    }

}
