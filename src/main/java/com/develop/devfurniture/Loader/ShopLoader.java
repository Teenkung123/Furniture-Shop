package com.develop.devfurniture.Loader;

import com.develop.devfurniture.Utils.EconomyType;
import com.develop.devfurniture.Utils.ItemBuilder;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;

import static com.develop.devfurniture.DevFurniture.colorize;
import static com.develop.devfurniture.DevFurniture.colorizeArray;

public class ShopLoader {

    private static final ArrayList<Inventory> GUIInventory = new ArrayList<>();
    private static Integer MaxPage;
    public static HashMap<Player, Integer> PlayerPage = new HashMap<>();

    public static ArrayList<Inventory> getGUI() {
        return GUIInventory;
    }
    public static HashMap<Player, Integer> getPlayerPage() { return PlayerPage; }
    public static Integer getMaxPage() {
        return MaxPage;
    }



    public static void clearShop() {
        GUIInventory.clear();
        MaxPage = null;
        PlayerPage.clear();
    }

    public static void loadShop() {
        MaxPage = Double.valueOf(Math.ceil(ConfigLoader.getFurnitureKeyList().size() / 36d)).intValue();
        for (int i = 1; i <= MaxPage; i++) {
            System.out.println(colorize("&aLoading ShopGUI Page &e" + i));
            Inventory inv = Bukkit.createInventory(null, 54, colorize(ConfigLoader.getShopGUIName()));
            for (int ii = 0; ii < 54; ii++) {
                if (ii >= 9 && ii <= 44) {
                    int count = (i * 36 - 36) + ii - 9;
                    if (count >= ConfigLoader.getFurnitureKeyList().size()) {
                        continue;
                    }
                    CustomStack item = ConfigLoader.getFurnitureCustomStack().get(ConfigLoader.getFurnitureKeyList().get(count));
                    String key = ConfigLoader.getFurnitureKeyList().get(count);
                    String tag;
                    if (ConfigLoader.getEconomyType().get(key).equals(EconomyType.BOTH)) {
                        tag = ConfigLoader.getConfig().getString("Language.Both", "")
                                .replace("<vault>", ConfigLoader.getFurniturePrice().get(key) + ConfigLoader.getConfig().getString("Language.Vault"))
                                .replace("<playerpoints>", ConfigLoader.getFurniturePricePlayerPoints().get(key) + ConfigLoader.getConfig().getString("Language.PlayerPoints"));
                    } else if (ConfigLoader.getEconomyType().get(key).equals(EconomyType.PLAYERPOINTS)) {
                        tag = ConfigLoader.getConfig().getString("Language.Single", "")
                                .replace("<price>", ConfigLoader.getFurniturePricePlayerPoints().get(key) + ConfigLoader.getConfig().getString("Language.PlayerPoints"));
                    } else {
                        //System.out.println(ConfigLoader.getConfig().getString("Language.Single", ""));
                        //System.out.println(ConfigLoader.getFurniturePrice().getOrDefault(key, 100000D) + ConfigLoader.getConfig().getString("Language.Vault", ""));
                        tag = ConfigLoader.getConfig().getString("Language.Single", "")
                                .replace("<price>"
                                        , ConfigLoader.getFurniturePrice().getOrDefault(key, 100000D) + ConfigLoader.getConfig().getString("Language.Vault", ""));
                    }
                    ArrayList<String> lores = new ArrayList<>();
                    lores.add(tag);
                    lores.addAll(ConfigLoader.getShopGUILore());
                    inv.setItem(ii, new ItemBuilder(item.getItemStack().clone())
                            .setStringNBT("ShopKey", ConfigLoader.getFurnitureKeyList().get(count))
                            .setIntegerNBT("ShopPage", i)
                            .setIntegerNBT("ItemNumber", count)
                            .addLore(colorizeArray(lores))
                            .build());
                } else if (ii == 53) {
                    inv.setItem(ii, ConfigLoader.getNextStack().setIntegerNBT("Page", i).build());
                } else if (ii == 52) {
                    inv.setItem(ii, ConfigLoader.getPreviousStack().setIntegerNBT("Page", i).build());
                }
            }

            GUIInventory.add(inv);

        }
    }

}
