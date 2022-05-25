package com.develop.devfurniture;

import com.develop.devfurniture.Loader.ConfigLoader;
import com.develop.devfurniture.Loader.ShopLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;

import static com.develop.devfurniture.DevFurniture.colorize;

public class CommandHandler implements CommandExecutor {
    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("open")) {
                if (sender instanceof Player player) {
                    if (!player.hasPermission("FurnitureShop.open")) { return false; }
                    if (!DevFurniture.getEnabled()) {
                        player.sendMessage(colorize("&cPlease wait until Plugin is done load data!"));
                        return false;
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
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("FurnitureShop.reload")) { return false; }
                long ms = System.currentTimeMillis();
                sender.sendMessage(colorize("&bReloading Configuration files. . ."));
                DevFurniture.getInstance().reloadConfigFile();
                ConfigLoader.clearConfigData();
                ConfigLoader.loadEverything();
                sender.sendMessage(colorize("&aReload Complete!\n&aTook &e" + (System.currentTimeMillis() - ms) + "&a ms"));
            }
        }
        return false;
    }
}
