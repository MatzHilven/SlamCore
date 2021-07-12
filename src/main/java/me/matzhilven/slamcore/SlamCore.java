package me.matzhilven.slamcore;

import lombok.Getter;
import me.matzhilven.slamcore.commands.gems.GemsBaseCommand;
import me.matzhilven.slamcore.commands.gems.subcommands.*;
import me.matzhilven.slamcore.data.DatabaseHandler;
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

        Bukkit.getLogger().info(String.format("[%s] Loading database...", getDescription().getName()));
        databaseHandler = new DatabaseHandler("95.217.194.15", 27017, "Prison");
        Bukkit.getLogger().info(String.format("[%s] Loaded database ", getDescription().getName()));

        new PlayerListener(this);

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
