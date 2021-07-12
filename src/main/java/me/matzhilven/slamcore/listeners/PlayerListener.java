package me.matzhilven.slamcore.listeners;

import me.matzhilven.slamcore.SlamCore;
import me.matzhilven.slamcore.data.User;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final SlamCore main;
    private final FileConfiguration config;

    public PlayerListener(SlamCore main) {
        this.main = main;
        this.config = main.getConfig();
        main.getServer().getPluginManager().registerEvents(this,main);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        User user =  main.getDatabaseHandler().getUserByPlayer(e.getPlayer());

        System.out.println(user.getGems());
    }

    @EventHandler
    private void onLeave(PlayerQuitEvent e) {
        main.getDatabaseHandler().savePlayer(e.getPlayer());
    }
}
