package me.matzhilven.slamcore.menus;

import me.matzhilven.slamcore.SlamCore;
import me.matzhilven.slamcore.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class Menu implements InventoryHolder {
    public final SlamCore main = SlamCore.getInstance();
    public final FileConfiguration config = main.getConfig();

    public final Player p;
    public Inventory inventory;

    public Menu(Player p) {
        this.p = p;
    }

    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract void handleClick(InventoryClickEvent e);

    public abstract void setMenuItems();

    public void open() {
        inventory = Bukkit.createInventory(this, getSlots(), StringUtils.colorize(getMenuName()));
        this.setMenuItems();

        p.openInventory(inventory);
    }

    public void setFillerGlass(ItemStack glass) {
        for (int i = 0; i < getSlots(); i++) {
            if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) {
                inventory.setItem(i, glass);
            }
        }
    }

    public Inventory getInventory() {
        return inventory;
    }
}
