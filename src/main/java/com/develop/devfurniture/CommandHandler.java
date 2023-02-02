package com.develop.devfurniture;

import com.develop.devfurniture.Events.BuyConfirmationEvent;
import com.develop.devfurniture.Loader.ConfigLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.develop.devfurniture.DevFurniture.colorize;

public class CommandHandler implements CommandExecutor {
    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("open")) {
                if (checkDataLoad(sender)) return false;
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("FurnitureShop.reload")) { return false; }
                long ms = System.currentTimeMillis();
                sender.sendMessage(colorize("&bReloading Configuration files. . ."));
                DevFurniture.getInstance().reloadConfigFile();
                ConfigLoader.clearConfigData();
                ConfigLoader.loadEverything();
                sender.sendMessage(colorize("&aReload Complete!\n&aTook &e" + (System.currentTimeMillis() - ms) + "&a ms"));
            }
        } else {
            if (checkDataLoad(sender)) return false;
        }
        return false;
    }

    private boolean checkDataLoad(CommandSender sender) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("FurnitureShop.open")) {
                return true;
            }
            if (!DevFurniture.getEnabled()) {
                player.sendMessage(colorize("&cPlease wait until Plugin is done load data!"));
                return true;
            }
            BuyConfirmationEvent.changePageInventory(player);
        }
        return false;
    }
}
