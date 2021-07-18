package me.matzhilven.slamcore.commands.levelpickaxe.subcommands;

import de.tr7zw.nbtapi.NBTItem;
import me.matzhilven.slamcore.SlamCore;
import me.matzhilven.slamcore.commands.SubCommand;
import me.matzhilven.slamcore.utils.Messager;
import me.matzhilven.slamcore.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LPSetExpSubCommand implements SubCommand {

    private final SlamCore main;
    private final FileConfiguration config;

    public LPSetExpSubCommand(SlamCore main) {
        this.main = main;
        this.config = main.getConfig();
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {
        if (args.length != 3) {
            StringUtils.sendMessage(sender, Messager.SET_EXP_PICKAXE_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            StringUtils.sendMessage(sender, Messager.INVALID_TARGET.replace("%player%", args[1]));
            return;
        }

        int exp;

        try  {
            exp = Integer.parseInt(args[2]);
        } catch (NumberFormatException ignored) {
            StringUtils.sendMessage(sender, Messager.INVALID_NUMBER.replace("%arg%", args[2]));
            return;
        }

        if (exp < 0) {
            StringUtils.sendMessage(sender, Messager.INVALID_NUMBER.replace("%arg%", args[2]));
            return;
        }

        int i = 0;
        for (ItemStack item : target.getInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR) {
                i++;
                continue;
            }
            NBTItem nbtItem = new NBTItem(item);
            if (nbtItem.hasKey("levelpick")) {
                int level = nbtItem.getInteger("level");
                int newLevel = checkLevel(exp);

                if (level != newLevel) {
                    if (level < newLevel) {
                        nbtItem.setInteger("level", newLevel);
                        nbtItem.setInteger("exp", exp - (config.getInt("levels." + newLevel + ".exp")));
                    } else {
                        nbtItem.setInteger("level", newLevel);
                        nbtItem.setInteger("exp", (config.getInt("levels." + level + ".exp")) - exp);
                    }
                } else {
                    nbtItem.setInteger("exp", exp);
                }

                ItemMeta meta = item.getItemMeta();

                for (String s : main.getConfig().getStringList("levels." + newLevel + ".enchants")) {
                    Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(s.split(" ; ")[0].trim().toLowerCase()));
                    int enchLevel = Integer.parseInt(s.split(" ; ")[1].trim());
                    if (enchantment == null || level == 0) continue;
                    meta.addEnchant(enchantment, enchLevel, true);
                }
                item.setItemMeta(meta);
                target.getInventory().setItem(i, nbtItem.getItem());
            }
            i++;
        }

        StringUtils.sendMessage(sender, Messager.SET_EXP_SENDER.replace("%target%", target.getName()).replace("%exp%", StringUtils.format(exp)));
        StringUtils.sendMessage(target, Messager.SET_EXP.replace("%exp%", StringUtils.format(exp)));
    }

    @Override
    public String getPermission() {
        return "core.levelpick";
    }

    private int checkLevel(int exp) {
        for (String level : config.getConfigurationSection("levels").getKeys(false)) {
            System.out.println(level + " " + exp + " " + config.getInt("levels." + level + ".exp"));
            if (exp <= config.getInt("levels." + level + ".exp")) {
                System.out.println(level);
                return Integer.parseInt(level);
            }
        }
        return 0;
    }
}
