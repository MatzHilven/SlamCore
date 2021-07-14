package me.matzhilven.slamcore.menus;

import de.tr7zw.nbtapi.NBTItem;
import me.matzhilven.slamcore.enchantments.PrisonEnchants;
import me.matzhilven.slamcore.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class PickaxeMenu extends Menu {

    private final ItemStack pickaxe;
    private final HashMap<Integer, Enchantment> slots = new HashMap<>();

    public PickaxeMenu(Player p, ItemStack pickaxe) {
        super(p);
        this.pickaxe = pickaxe;
    }

    @Override
    public String getMenuName() {
        return config.getString("pickaxe-menu.name");
    }

    @Override
    public int getSlots() {
        return config.getInt("pickaxe-menu.slots");
    }

    @Override
    public void handleClick(InventoryClickEvent e) {

    }

    @Override
    public void setMenuItems() {
        Material mat = Material.matchMaterial(config.getString("pickaxe-menu.filler.material"));


        config.getConfigurationSection("enchants").getKeys(false).forEach(enchant -> {
            Material material = Material.matchMaterial(config.getString("enchants." + enchant + ".material"));

            Enchantment enchantment = PrisonEnchants.getEnchantByName(enchant);

            int level = pickaxe.getItemMeta().getEnchantLevel(enchantment);

            int maxLevel = config.getInt("enchants." + enchant + ".max-level");

            double chance = (int) ((level * config.getDouble("enchants." + enchant + ".chance")) * 100);

            int cP = config.getInt("enchants." + enchantment.getKey().getKey().toLowerCase() + ".price");
            int incr = config.getInt("enchants." + enchantment.getKey().getKey().toLowerCase() + ".increase");

            inventory.setItem(config.getInt("enchants." + enchant + ".slot"),
                    new ItemBuilder(material)
                            .setName(config.getString("enchants." + enchant + ".name"))
                            .setLore(config.getStringList("enchants." + enchant + ".lore"))
                            .replace("%level%",
                                    String.valueOf(level))
                            .replace("%max_level%",
                                    String.valueOf(maxLevel))
                            .replace("%chance%",
                                    String.valueOf(chance))
                            .replace("%price-1%",
                                    String.valueOf(cP + level * incr))
                            .toItemStack()
                    );

            slots.put(config.getInt("enchants." + enchant + ".slot"), enchantment);
        });

        setFillerGlass(new ItemBuilder(mat)
                .setName(config.getString("pickaxe-menu.filler.name"))
                .setLore(config.getStringList("pickaxe-menu.filler.lore"))
                .toItemStack());
    }
}
