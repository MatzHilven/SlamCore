package me.matzhilven.slamcore.enchantments;

import me.matzhilven.slamcore.SlamCore;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class PrisonEnchant extends Enchantment {

    private final SlamCore main = SlamCore.getInstance();

    public PrisonEnchant(String key) {
        super(NamespacedKey.minecraft(key));
    }

    @Override
    public String getName() {
        return getKey().getKey();
    }

    @Override
    public int getMaxLevel() {
        return main.getConfig().getInt("enchants." + getName().trim().toLowerCase() + ".max-level");
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item.getType().toString().endsWith("PICKAXE");
    }
}
