package me.matzhilven.slamcore.levelpickaxe;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Data;
import me.matzhilven.slamcore.SlamCore;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Data
public class LevelPickaxe {

    private final SlamCore main = SlamCore.getInstance();

    private final int level;
    private final int brokenBlocks;
    private final int exp;


    public ItemStack getPickaxe() {

        NBTItem nbtItem = new NBTItem(new ItemStack(Material.DIAMOND_PICKAXE));

        nbtItem.setBoolean("levelpick", true);
        nbtItem.setInteger("level", level);
        nbtItem.setInteger("blocks", brokenBlocks);
        nbtItem.setInteger("exp", exp);

        ItemStack pickaxe = nbtItem.getItem();
        ItemMeta meta = pickaxe.getItemMeta();
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);

        for (String s : main.getConfig().getStringList("levels.1.enchants")) {
            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(s.split(" ; ")[0].trim().toLowerCase()));
            int level = Integer.parseInt(s.split(" ; ")[1].trim());
            if (enchantment == null || level == 0) continue;
            meta.addEnchant(enchantment, level, true);
        }

        pickaxe.setItemMeta(meta);

        return pickaxe;
    }
}
