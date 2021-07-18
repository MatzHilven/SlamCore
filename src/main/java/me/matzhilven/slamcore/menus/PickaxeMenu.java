package me.matzhilven.slamcore.menus;

import me.matzhilven.slamcore.enchantments.PrisonEnchants;
import me.matzhilven.slamcore.menus.gembank.GemBankMenu;
import me.matzhilven.slamcore.utils.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
        if (e.getSlot() == config.getInt("pickaxe-menu.gembank.slot")) {
            GemBankMenu gemBankMenu = new GemBankMenu(p);
            gemBankMenu.open();
        } else if (e.getSlot() == config.getInt("pickaxe-menu.settings.slot")) {

        }


        if (!slots.containsKey(e.getSlot())) return;

        Enchantment enchantment = slots.get(e.getSlot());

        ItemMeta meta = pickaxe.getItemMeta();

        meta.addEnchant(enchantment, meta.getEnchantLevel(enchantment) + 1, true);

        pickaxe.setItemMeta(meta);

        p.getInventory().setItemInMainHand(pickaxe);
        setMenuItems();


        if (meta.hasEnchant(PrisonEnchants.JUMP)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, meta.getEnchantLevel(PrisonEnchants.JUMP)));
        } else {
            if (p.hasPotionEffect(PotionEffectType.JUMP)) {
                p.removePotionEffect(PotionEffectType.JUMP);
            }
        }

        if (meta.hasEnchant(PrisonEnchants.HASTE)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, meta.getEnchantLevel(PrisonEnchants.HASTE)));
        } else {
            if (p.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                p.removePotionEffect(PotionEffectType.FAST_DIGGING);
            }
        }

        if (meta.hasEnchant(PrisonEnchants.SPEED)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, meta.getEnchantLevel(PrisonEnchants.SPEED)));
        } else {
            if (p.hasPotionEffect(PotionEffectType.SPEED)) {
                p.removePotionEffect(PotionEffectType.SPEED);
            }
        }

        if (meta.hasEnchant(PrisonEnchants.FLIGHT)) {
            p.setAllowFlight(true);
            p.setFlying(true);
        } else {
            if (p.isFlying() && !(p.getGameMode() == GameMode.CREATIVE)) {
                p.setAllowFlight(false);
                p.setFlying(false);
            }
        }

    }

    @Override
    public void setMenuItems() {

        inventory.setItem(config.getInt("pickaxe-menu.gembank.slot"),
                new ItemBuilder(Material.matchMaterial(config.getString("pickaxe-menu.gembank.material")))
                        .setName(config.getString("pickaxe-menu.gembank.name"))
                        .setLore(config.getStringList("pickaxe-menu.gembank.lore"))
                        .toItemStack()
        );

        inventory.setItem(config.getInt("pickaxe-menu.settings.slot"),
                new ItemBuilder(Material.matchMaterial(config.getString("pickaxe-menu.settings.material")))
                        .setName(config.getString("pickaxe-menu.settings.name"))
                        .setLore(config.getStringList("pickaxe-menu.settings.lore"))
                        .toItemStack()
        );

        config.getConfigurationSection("enchants").getKeys(false).forEach(enchant -> {
            Material material = Material.matchMaterial(config.getString("enchants." + enchant + ".material"));

            Enchantment enchantment = PrisonEnchants.getEnchantByName(enchant);

            int level = pickaxe.getItemMeta().getEnchantLevel(enchantment);

            int maxLevel = config.getInt("enchants." + enchant + ".max-level");

            double chance = (int) ((level * config.getDouble("enchants." + enchant + ".chance", 1)) * 100);

            int cP = config.getInt("enchants." + enchantment.getKey().getKey().toLowerCase() + ".price");
            int incr = config.getInt("enchants." + enchantment.getKey().getKey().toLowerCase() + ".increase", 0);


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
                            .replace("%price%",
                                    String.valueOf(cP + level * incr))
                            .replace("%state%", level == maxLevel ? "Unlocked" : "Locked")
                            .replace("%percentage%", String.valueOf(100 + (10 * level)))
                            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                            .toItemStack()
            );

            slots.put(config.getInt("enchants." + enchant + ".slot"), enchantment);
        });

        setFillerGlass(new ItemBuilder(Material.matchMaterial(config.getString("pickaxe-menu.filler.material")))
                .setName(config.getString("pickaxe-menu.filler.name"))
                .setLore(config.getStringList("pickaxe-menu.filler.lore"))
                .toItemStack());
    }
}
