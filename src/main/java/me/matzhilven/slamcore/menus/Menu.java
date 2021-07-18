package me.matzhilven.slamcore.menus;

import me.matzhilven.slamcore.SlamCore;
import me.matzhilven.slamcore.utils.ItemBuilder;
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
    protected final SlamCore main = SlamCore.getInstance();
    protected final FileConfiguration config = main.getConfig();

    protected final Player p;
    protected Inventory inventory;

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

    protected int getGemsInInventory() {
        String name = StringUtils.removeColor(config.getString("enchants.gemchance.gem.name"));

        int amount = 0;

        for (ItemStack item : p.getInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR || !(item.getType() == Material.SUNFLOWER)) continue;
            if (!item.hasItemMeta()) continue;
            if (!item.getItemMeta().hasDisplayName()) continue;
            if (!StringUtils.decolorize(item.getItemMeta().getDisplayName()).equals(name)) continue;

            amount += item.getAmount();
        }

        return amount;
    }

    public Inventory getInventory() {
        return inventory;
    }

    protected ItemStack getGem() {
        return new ItemBuilder(
                Material.matchMaterial(config.getString("enchants.gemchance.gem.material")))
                .setName(config.getString("enchants.gemchance.gem.name"))
                .setLore(config.getStringList("enchants.gemchance.gem.lore"))
                .toItemStack();
    }

    protected void removeGems(int amount) {
        int removed = 0;

        String name = StringUtils.removeColor(config.getString("enchants.gemchance.gem.name"));

        for (ItemStack item : p.getInventory().getContents()) {
            if (removed == amount) return;

            if (item == null || item.getType() == Material.AIR || !(item.getType() == Material.SUNFLOWER)) continue;
            if (!item.hasItemMeta()) continue;
            if (!item.getItemMeta().hasDisplayName()) continue;
            if (!StringUtils.decolorize(item.getItemMeta().getDisplayName()).equals(name)) continue;

            if (item.getAmount() >= amount) {
                item.setAmount(item.getAmount() - amount);
                return;
            } else {
                removed += item.getAmount();
                item.setAmount(0);
            }
        }
    }
}
