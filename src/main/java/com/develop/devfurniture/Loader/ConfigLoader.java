package com.develop.devfurniture.Loader;

import com.develop.devfurniture.DevFurniture;
import com.develop.devfurniture.Utils.EconomyType;
import com.develop.devfurniture.Utils.ItemBuilder;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import static com.develop.devfurniture.DevFurniture.colorize;
import static com.develop.devfurniture.DevFurniture.colorizeArray;

public class ConfigLoader {

    private static final ArrayList<String> FurnitureKeyList = new ArrayList<>();
    private static final HashMap<String, CustomStack> FurnitureCustomStack = new HashMap<>();
    private static final HashMap<String, Double> FurniturePrice = new HashMap<>();
    private static final HashMap<String, Integer> FurniturePricePlayerPoints = new HashMap<>();
    private static final HashMap<String, EconomyType> economyType = new HashMap<>();

    private static String ShopGUIName;
    private static ArrayList<String> ShopGUILore;

    private static String ConfirmationGUIName;
    private static ArrayList<String> ConfirmationGUILore;

    private static ItemBuilder PreviousStack;
    private static ItemBuilder NextStack;
    private static ItemBuilder AcceptStack;
    private static ItemBuilder DenyStack;
    private static ItemBuilder BuyWithPointStack;
    private static ItemBuilder BuyWithVaultStack;

    private static Location PreviewLocation;
    private static String WorldGuardArea;

    private static Integer CheckDistance;

    //====================================================================================

    public static ArrayList<String> getFurnitureKeyList() { return FurnitureKeyList; }
    public static HashMap<String, CustomStack> getFurnitureCustomStack() { return FurnitureCustomStack; }
    public static HashMap<String, Double> getFurniturePrice() { return FurniturePrice; }
    public static HashMap<String, Integer> getFurniturePricePlayerPoints() { return FurniturePricePlayerPoints; }
    public static HashMap<String, EconomyType> getEconomyType() { return economyType; }
    public static String getShopGUIName() { return ShopGUIName; }
    public static ArrayList<String> getShopGUILore() { return ShopGUILore; }
    public static String getConfirmationGUIName() { return ConfirmationGUIName; }
    public static ArrayList<String> getConfirmationGUILore() { return ConfirmationGUILore; }
    public static ItemBuilder getPreviousStack() { return PreviousStack; }
    public static ItemBuilder getNextStack() { return NextStack; }
    public static ItemBuilder getAcceptStack() { return AcceptStack; }
    public static ItemBuilder getDenyStack() { return DenyStack; }
    public static ItemBuilder getBuyWithPointStack() { return BuyWithPointStack; }
    public static ItemBuilder getBuyWithVaultStack() { return BuyWithVaultStack; }
    public static FileConfiguration getConfig() { return DevFurniture.getInstance().getConfig(); }
    public static Location getPreviewLocation() { return PreviewLocation; }
    public static String getWorldGuardArea() { return WorldGuardArea; }

    public static Integer getCheckDistance() { return CheckDistance; }

    //====================================================================================

    public static void loadEverything() {
        WorldGuardLoader.loadWorldGuard();
        ConfigLoader.loadConfigData();
        ShopLoader.loadShop();
        DevFurniture.setEnabled(true);
    }

    public static void clearConfigData() {

        DevFurniture.setEnabled(false);

        FurnitureKeyList.clear();
        FurnitureCustomStack.clear();
        FurniturePrice.clear();
        FurniturePricePlayerPoints.clear();
        economyType.clear();

        ShopGUIName = null;
        ShopGUILore = null;

        ConfirmationGUIName = null;
        ConfirmationGUILore = null;

        PreviousStack = null;
        NextStack = null;
        AcceptStack = null;
        DenyStack = null;
        BuyWithPointStack = null;
        BuyWithVaultStack = null;

        PreviewLocation = null;
        WorldGuardArea = null;

        CheckDistance = null;

        ShopLoader.clearShop();
        WorldGuardLoader.clearWorldGuard();

    }

    public static void loadConfigData() {
        System.out.println(colorize("&aLoading Config Data"));
        long ms = System.currentTimeMillis();
        for (String key : Objects.requireNonNull(DevFurniture.getInstance().getConfig().getConfigurationSection("Shop")).getKeys(false)) {
            CustomStack stack = CustomStack.getInstance(DevFurniture.getInstance().getConfig().getString("Shop." + key + ".ID"));
            if (stack != null) {
                EconomyType type = EconomyType.fromString(DevFurniture.getInstance().getConfig().getString("Shop." + key + ".Currency", "Vault"));
                FurnitureCustomStack.put(key, stack);
                FurniturePrice.put(key, DevFurniture.getInstance().getConfig().getDouble("Shop." + key + ".Vault", 1000000));
                FurniturePricePlayerPoints.put(key, DevFurniture.getInstance().getConfig().getInt("Shop."+key+".PlayerPoints", 1000000));
                economyType.put(key, type);
                FurnitureKeyList.add(key);
            } else {
                System.out.println(colorize("&cCould not load Shop With ID " + key + " Because that NameSpace Does not Exist"));
            }
        }
        System.out.println(colorize("&aComplete loaded total of " + FurnitureKeyList.size() + " Furniture!"));

        NextStack = new ItemBuilder(Material.getMaterial(DevFurniture.getInstance().getConfig().getString("Items.NextPage.Item", "STONE")), 1)
                .setDisplayName(DevFurniture.getInstance().getConfig().getString("Items.NextPage.Name", "&aNext Page"))
                .setModelData(DevFurniture.getInstance().getConfig().getInt("Items.NextPage.ModelData", 0))
                .setStringNBT("IsNext", "true")
                .setLoreByArray(colorizeArray(new ArrayList<>(DevFurniture.getInstance().getConfig().getStringList("Items.NextPage.Lore"))));

        PreviousStack = new ItemBuilder(Material.getMaterial(DevFurniture.getInstance().getConfig().getString("Items.PreviousPage.Item", "STONE")), 1)
                .setDisplayName(DevFurniture.getInstance().getConfig().getString("Items.PreviousPage.Name", "&cPrevious Page"))
                .setModelData(DevFurniture.getInstance().getConfig().getInt("Items.PreviousPage.ModelData", 0))
                .setStringNBT("IsPrevious", "true")
                .setLoreByArray(colorizeArray(new ArrayList<>(DevFurniture.getInstance().getConfig().getStringList("Items.PreviousPage.Lore"))));

        AcceptStack = new ItemBuilder(Material.getMaterial(DevFurniture.getInstance().getConfig().getString("Items.Accept.Item", "STONE")), 1)
                .setDisplayName(DevFurniture.getInstance().getConfig().getString("Items.Accept.Name", "&aNext Page"))
                .setModelData(DevFurniture.getInstance().getConfig().getInt("Items.Accept.ModelData", 0))
                .setStringNBT("IsAccept", "true")
                .setLoreByArray(colorizeArray(new ArrayList<>(DevFurniture.getInstance().getConfig().getStringList("Items.Accept.Lore"))));

        DenyStack = new ItemBuilder(Material.getMaterial(DevFurniture.getInstance().getConfig().getString("Items.Deny.Item", "STONE")), 1)
                .setDisplayName(DevFurniture.getInstance().getConfig().getString("Items.Deny.Name", "&cPrevious Page"))
                .setModelData(DevFurniture.getInstance().getConfig().getInt("Items.Deny.ModelData", 0))
                .setStringNBT("IsDeny", "true")
                .setLoreByArray(colorizeArray(new ArrayList<>(DevFurniture.getInstance().getConfig().getStringList("Items.Deny.Lore"))));

        BuyWithPointStack = new ItemBuilder(Material.getMaterial(DevFurniture.getInstance().getConfig().getString("Items.BuyWithPoints.Item", "STONE")), 1)
                .setDisplayName(DevFurniture.getInstance().getConfig().getString("Items.BuyWithPoints.Name", "&cPrevious Page"))
                .setModelData(DevFurniture.getInstance().getConfig().getInt("Items.BuyWithPoints.ModelData", 0))
                .setStringNBT("IsAccept", "Point")
                .setLoreByArray(colorizeArray(new ArrayList<>(DevFurniture.getInstance().getConfig().getStringList("Items.BuyWithPoints.Lore"))));

        BuyWithVaultStack = new ItemBuilder(Material.getMaterial(DevFurniture.getInstance().getConfig().getString("Items.BuyWithVault.Item", "STONE")), 1)
                .setDisplayName(DevFurniture.getInstance().getConfig().getString("Items.BuyWithVault.Name", "&cPrevious Page"))
                .setModelData(DevFurniture.getInstance().getConfig().getInt("Items.BuyWithVault.ModelData", 0))
                .setStringNBT("IsAccept", "Vault")
                .setLoreByArray(colorizeArray(new ArrayList<>(DevFurniture.getInstance().getConfig().getStringList("Items.BuyWithVault.Lore"))));

        ShopGUIName = DevFurniture.getInstance().getConfig().getString("Language.Shop-GUI-Name", "&1Furniture Shop");
        ConfirmationGUIName = DevFurniture.getInstance().getConfig().getString("Language.Confirmation-GUI-Name", "&1Confirmation for Buying");

        ShopGUILore = new ArrayList<>(DevFurniture.getInstance().getConfig().getStringList("Language.Shop-Lore"));
        ConfirmationGUILore = new ArrayList<>(DevFurniture.getInstance().getConfig().getStringList("Language.Confirmation-Lore"));

        WorldGuardArea = getConfig().getString("Location.Region");
        PreviewLocation = new Location(Bukkit.getWorld(getConfig().getString("Location.Preview.World", "spawn")), getConfig().getDouble("Location.Preview.X"), getConfig().getDouble("Location.Preview.Y"), getConfig().getDouble("Location.Preview.Z"), 0, 0);

        CheckDistance = getConfig().getInt("Location.CheckDistance", 25);


        System.out.println(colorize("&aLoaded Complete! &aTook &e" + (System.currentTimeMillis() - ms) + "&a ms"));

    }

    public static String getRandomFurniture() {
        int rnd = new Random().nextInt(FurnitureKeyList.size());
        return FurnitureCustomStack.get(FurnitureKeyList.get(rnd)).getNamespacedID();
    }

}
