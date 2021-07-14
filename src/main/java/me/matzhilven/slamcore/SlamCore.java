package me.matzhilven.slamcore;

import com.comphenix.protocol.ProtocolLibrary;
import lombok.Getter;
import me.matzhilven.slamcore.commands.gems.GemsBaseCommand;
import me.matzhilven.slamcore.commands.gems.subcommands.GemsAddSubCommand;
import me.matzhilven.slamcore.commands.gems.subcommands.GemsGiveSubCommand;
import me.matzhilven.slamcore.commands.gems.subcommands.GemsRemoveSubCommand;
import me.matzhilven.slamcore.commands.gems.subcommands.GemsSetSubCommand;
import me.matzhilven.slamcore.data.DatabaseHandler;
import me.matzhilven.slamcore.enchantments.PrisonEnchants;
import me.matzhilven.slamcore.listeners.InventoryListeners;
import me.matzhilven.slamcore.listeners.ItemListener;
import me.matzhilven.slamcore.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SlamCore extends JavaPlugin {

    @Getter
    private static SlamCore instance;

    @Getter
    private DatabaseHandler databaseHandler;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        Bukkit.getLogger().info(String.format("[%s] Loading enchants...", getDescription().getName()));
        PrisonEnchants.registerEnchants();
        Bukkit.getLogger().info(String.format("[%s] Loaded enchants", getDescription().getName()));

        Bukkit.getLogger().info(String.format("[%s] Loading database...", getDescription().getName()));
        databaseHandler = new DatabaseHandler(getConfig().getString("database.host"),
                getConfig().getInt("database.port"), "Prison");
        Bukkit.getLogger().info(String.format("[%s] Loaded database ", getDescription().getName()));

        new PlayerListener(this);
        new InventoryListeners(this);
        ProtocolLibrary.getProtocolManager().addPacketListener(new ItemListener(this));

        GemsBaseCommand gemsBaseCommand = new GemsBaseCommand(this);
        gemsBaseCommand.registerSubCommand("add", new GemsAddSubCommand(this));
        gemsBaseCommand.registerSubCommand("give", new GemsGiveSubCommand(this));
        gemsBaseCommand.registerSubCommand("remove", new GemsRemoveSubCommand(this));
        gemsBaseCommand.registerSubCommand("set", new GemsSetSubCommand(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
