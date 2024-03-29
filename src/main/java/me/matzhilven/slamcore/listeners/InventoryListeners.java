package me.matzhilven.slamcore.listeners;

import me.matzhilven.slamcore.SlamCore;
import me.matzhilven.slamcore.menus.Menu;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class InventoryListeners implements Listener {

    public InventoryListeners(SlamCore main) {
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();

        if (holder instanceof Menu) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;

            e.setCancelled(true);

            if (e.getCurrentItem().getType().toString().contains("STAINED_GLASS_PANE")) return;

            Menu menu = (Menu) holder;
            menu.handleClick(e);
        }
    }
}
