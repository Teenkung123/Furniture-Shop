package com.develop.devfurniture.Events;

import com.develop.devfurniture.DevFurniture;
import com.develop.devfurniture.Loader.ConfigLoader;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static com.develop.devfurniture.DevFurniture.colorize;

public class ItemsAdderEvent implements Listener {

    @EventHandler
    public void onLoad(ItemsAdderLoadDataEvent event) {
        System.out.println(colorize("&aItemsAdder is complete loading items. Furniture Shop is reloading now. . ."));
        long ms = System.currentTimeMillis();
        DevFurniture.getInstance().reloadConfigFile();
        ConfigLoader.clearConfigData();
        ConfigLoader.loadEverything();
        System.out.println(colorize("&aReload Complete! &aTook &e" + (System.currentTimeMillis() - ms) + "&a ms"));
    }

}
