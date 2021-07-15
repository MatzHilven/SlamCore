package me.matzhilven.slamcore.levelpickaxe;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Data
public class LevelPickaxe {

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
        pickaxe.setItemMeta(meta);

        return pickaxe;
    }
}
