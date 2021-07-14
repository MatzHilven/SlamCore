package me.matzhilven.slamcore.listeners;

import de.tr7zw.nbtapi.NBTItem;
import me.matzhilven.slamcore.SlamCore;
import me.matzhilven.slamcore.data.User;
import me.matzhilven.slamcore.enchantments.PrisonEnchants;
import me.matzhilven.slamcore.menus.PickaxeMenu;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerListener implements Listener {

    private final SlamCore main;
    private final FileConfiguration config;

    public PlayerListener(SlamCore main) {
        this.main = main;
        this.config = main.getConfig();
        main.getServer().getPluginManager().registerEvents(this,main);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        User user =  main.getDatabaseHandler().getUserByPlayer(e.getPlayer());
        // Todo Recode playerdata
    }

    @EventHandler
    private void onLeave(PlayerQuitEvent e) {
        main.getDatabaseHandler().savePlayer(e.getPlayer());
    }

    @EventHandler
    private void onSwapItem(PlayerItemHeldEvent e) {
        if (e.isCancelled()) return;

        ItemStack item = e.getPlayer().getInventory().getItem(e.getNewSlot());
        Player p = e.getPlayer();
        if (item == null || item.getType() == org.bukkit.Material.AIR) {
            if (p.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                p.removePotionEffect(PotionEffectType.FAST_DIGGING);
            }
            if (p.hasPotionEffect(PotionEffectType.JUMP)) {
                p.removePotionEffect(PotionEffectType.JUMP);
            }
            return;
        }

        if (item.containsEnchantment(PrisonEnchants.JUMP)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1));
        } else {
            if (p.hasPotionEffect(PotionEffectType.JUMP)) {
                p.removePotionEffect(PotionEffectType.JUMP);
            }
        }

        if (item.containsEnchantment(PrisonEnchants.HASTE)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1));
        } else {
            if (p.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                p.removePotionEffect(PotionEffectType.FAST_DIGGING);
            }
        }
    }

    @EventHandler
    private void onPickaxeClick(PlayerInteractEvent e) {
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (e.getItem() == null || e.getItem().getType() == org.bukkit.Material.AIR) return;
        if (e.getItem().getType().toString().endsWith("PICKAXE")) {
            PickaxeMenu pickaxeMenu = new PickaxeMenu(e.getPlayer(), e.getItem());
            pickaxeMenu.open();
        }
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand().getType() == org.bukkit.Material.AIR) return;
        Block b = e.getBlock();
        boolean cancel = false;

        NBTItem nbtItem = new NBTItem(p.getInventory().getItemInMainHand());

        if (!nbtItem.hasKey("levelpick")) return;

        if (cancel) return;

        int blockExp = config.getInt("blocks." + b.getType(), 1);

        int level = nbtItem.getInteger("level");
        int brokenBlocks = nbtItem.getInteger("blocks");

        int exp = nbtItem.getInteger("exp");
        int neededExp = main.getConfig().getInt("levels." + (level + 1) + ".exp");

        nbtItem.setInteger("blocks", brokenBlocks + 1);

        if (neededExp != 0) {
            if (exp + blockExp >= neededExp) {
                nbtItem.setInteger("exp", ((exp+ blockExp) - neededExp));
                nbtItem.setInteger("level", level + 1);

                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                main.getConfig().getStringList("levels." + (level + 1) + ".rewards").forEach(cmd -> {
                    Bukkit.dispatchCommand(console, cmd.replace("%player%", p.getName()));
                });
            } else {
                nbtItem.setInteger("exp", exp + blockExp);
            }
        }

        p.getInventory().setItemInMainHand(nbtItem.getItem());
    }

    public static void setAir(Location loc) {
        net.minecraft.server.v1_16_R3.World nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();
        BlockPosition bp = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        IBlockData ibd = net.minecraft.server.v1_16_R3.Block.getByCombinedId(0);
        nmsWorld.setTypeAndData(bp, ibd, 2);
    }
}
