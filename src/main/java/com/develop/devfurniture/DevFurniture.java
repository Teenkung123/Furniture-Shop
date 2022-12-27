package com.develop.devfurniture;

import com.develop.devfurniture.Events.*;
import com.develop.devfurniture.Loader.ConfigLoader;
import com.develop.devfurniture.Utils.TaskRunner;
import dev.lone.itemsadder.api.ItemsAdder;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DevFurniture extends JavaPlugin {

    private static DevFurniture Instance;
    private static Boolean Enabled = false;

    private static Boolean EnabledPlayerPoints = false;
    //This defines all Vault API Thing please don't touch
    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ;
    private static Permission perms;
    private static Chat chat;


    @Override
    public void onEnable() {

        Plugin playerPointsJ = Bukkit.getPluginManager().getPlugin("PlayerPoints");
        if (playerPointsJ != null) {
            if (playerPointsJ.isEnabled()) {
                EnabledPlayerPoints = true;
            }
        }

        //This
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        setupPermissions();
        setupChat();

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        Instance = this;

        Bukkit.getPluginManager().registerEvents(new ConnectionHandler(), this);
        Bukkit.getPluginManager().registerEvents(new ItemsAdderEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PageChange(), this);
        Bukkit.getPluginManager().registerEvents(new ShopEvent(), this);
        Bukkit.getPluginManager().registerEvents(new OpenShopEvent(), this);
        Bukkit.getPluginManager().registerEvents(new BuyConfirmationEvent(), this);

        Objects.requireNonNull(getCommand("furniture-shop")).setExecutor(new CommandHandler());

        if (ItemsAdder.getAllItems() != null) {
            //Items adder is already load data
            System.out.println(colorize("&aItemsAdder Already load data. . . forcing to loading plugin. . ."));
            ConfigLoader.loadEverything();
        }

        TaskRunner.RunRandomFurnitureTask();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reloadConfigFile() {
        reloadConfig();
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    public static DevFurniture getInstance() { return Instance; }
    public static Boolean getEnabled() { return Enabled; }
    public static void setEnabled(Boolean value) { Enabled = value; }

    public static String colorize(String message) {
        if (message == null) {
            message = "";
        }
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static ArrayList<String> colorizeArray(ArrayList<String> array) {
        ArrayList<String> result = new ArrayList<>();
        for (String str : array) {
            result.add(colorize(str));
        }
        return result;
    }

    public static ArrayList<String> replaceArray(ArrayList<String> array, String regex, String replacement) {
        ArrayList<String> result = new ArrayList<>();
        for (String str : array) {
            result.add(str.replace(regex, replacement));
        }
        return result;
    }



    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            econ = null;
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }

    private void setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) { chat = null; return; }
        chat = rsp.getProvider();
    }

    private void setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) { perms = null; return; }
        perms = rsp.getProvider();
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static Permission getPermissions() {
        return perms;
    }

    public static Chat getChat() {
        return chat;
    }
}
