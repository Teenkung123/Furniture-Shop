package com.develop.devfurniture.Events;

import com.develop.devfurniture.Loader.ShopLoader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionHandler implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        ShopLoader.getPlayerPage().remove(event.getPlayer());
    }

}
