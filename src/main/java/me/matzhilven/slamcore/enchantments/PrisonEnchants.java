package me.matzhilven.slamcore.enchantments;

import me.matzhilven.slamcore.enchantments.impl.*;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class PrisonEnchants {

    private static final Map<String, Enchantment> enchants = new HashMap<>();

    public static final PrisonEnchant AUTOBLOCK = new AutoBlockEnchant();
    public static final PrisonEnchant AUTODEPOSIT = new AutoDepositEnchant();
    public static final PrisonEnchant AUTOMINER = new AutoMinerEnchant();
    public static final PrisonEnchant AUTOSELL = new AutoSellEnchant();
    public static final PrisonEnchant AUTOSMELT = new AutoSmeltEnchant();
    public static final PrisonEnchant EXPLOSIVE = new ExplosiveEnchant();
    public static final PrisonEnchant FLIGHT = new FlightEnchant();
    public static final PrisonEnchant GEMCHANCE = new GemChanceEnchant();
    public static final PrisonEnchant HASTE = new HasteEnchant();
    public static final PrisonEnchant JACKHAMMER = new JackHammerEnchant();
    public static final PrisonEnchant JUMP = new JumpEnchant();
    public static final PrisonEnchant KEYFINDER = new KeyFinderEnchant();
    public static final PrisonEnchant LASER = new LaserEnchant();
    public static final PrisonEnchant LIGHTNING = new LightningEnchant();
    public static final PrisonEnchant SPEED = new SpeedEnchant();
    public static final PrisonEnchant TELEPATHY = new TelepathyEnchant();

    public static void registerEnchants() {

        enchants.put("autoblock", AUTOBLOCK);
        enchants.put("autodeposit", AUTODEPOSIT);
        enchants.put("autominer", AUTOMINER);
        enchants.put("autosell", AUTOSELL);
        enchants.put("autosmelt", AUTOSMELT);
        enchants.put("explosive", EXPLOSIVE);
        enchants.put("flight", FLIGHT);
        enchants.put("gemchance", GEMCHANCE);
        enchants.put("haste", HASTE);
        enchants.put("jackhammer", JACKHAMMER);
        enchants.put("jump", JUMP);
        enchants.put("keyfinder", KEYFINDER);
        enchants.put("laser", LASER);
        enchants.put("lightning", LIGHTNING);
        enchants.put("speed", SPEED);
        enchants.put("telepathy", TELEPATHY);

        try {
            Field acceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
            acceptingNew.setAccessible(true);
            acceptingNew.set(null, true);

            enchants.values().forEach(enchantment -> {
                if (enchantment instanceof PrisonEnchant) {
                    Enchantment.registerEnchantment(enchantment);
                }
            });
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Enchantment getEnchantByName(String name) {
        return enchants.get(name);
    }

}
