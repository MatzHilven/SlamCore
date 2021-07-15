package me.matzhilven.slamcore.listeners;

import com.asylumdevs.mines.Mines;
import com.asylumdevs.mines.mine.Mine;
import com.asylumdevs.mines.utils.Region;
import de.tr7zw.nbtapi.NBTItem;
import me.matzhilven.slamcore.SlamCore;
import me.matzhilven.slamcore.data.User;
import me.matzhilven.slamcore.enchantments.PrisonEnchants;
import me.matzhilven.slamcore.menus.PickaxeMenu;
import me.matzhilven.slamcore.utils.ItemBuilder;
import me.matzhilven.slamcore.utils.Messager;
import me.matzhilven.slamcore.utils.StringUtils;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.IBlockData;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerListener implements Listener {

    private final SlamCore main;
    private final FileConfiguration config;
    private final ArrayList<UUID> invFull = new ArrayList<>();

    public PlayerListener(SlamCore main) {
        this.main = main;
        this.config = main.getConfig();
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        // Todo Give Levelpick
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
        if (item == null || item.getType() == Material.AIR) {
            if (p.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                p.removePotionEffect(PotionEffectType.FAST_DIGGING);
            }
            if (p.hasPotionEffect(PotionEffectType.JUMP)) {
                p.removePotionEffect(PotionEffectType.JUMP);
            }
            if (p.hasPotionEffect(PotionEffectType.SPEED)) {
                p.removePotionEffect(PotionEffectType.SPEED);
            }

            if (p.isFlying() && !(p.getGameMode() == GameMode.CREATIVE)) {
                p.setAllowFlight(false);
                p.setFlying(false);
            }
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

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

    @EventHandler
    private void onPickaxeClick(PlayerInteractEvent e) {
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (e.getItem() == null || e.getItem().getType() == Material.AIR) return;
        if (e.getItem().getType().toString().endsWith("PICKAXE")) {
            PickaxeMenu pickaxeMenu = new PickaxeMenu(e.getPlayer(), e.getItem());
            pickaxeMenu.open();
        }
    }

    @EventHandler
    private void onItemPickup(EntityPickupItemEvent e) {

        if (!(e.getEntity() instanceof Player)) return;

        Player p = (Player) e.getEntity();

        ItemStack item = e.getItem().getItemStack();
        if (item.getType() == Material.AIR) {
            if (p.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                p.removePotionEffect(PotionEffectType.FAST_DIGGING);
            }
            if (p.hasPotionEffect(PotionEffectType.JUMP)) {
                p.removePotionEffect(PotionEffectType.JUMP);
            }
            if (p.hasPotionEffect(PotionEffectType.SPEED)) {
                p.removePotionEffect(PotionEffectType.SPEED);
            }

            if (p.isFlying() && !(p.getGameMode() == GameMode.CREATIVE)) {
                p.setAllowFlight(false);
                p.setFlying(false);
            }
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

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

    @EventHandler
    private void onItemDrop(PlayerDropItemEvent e) {
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();

        Player p = e.getPlayer();
        if (item.getType() == Material.AIR) {
            if (p.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                p.removePotionEffect(PotionEffectType.FAST_DIGGING);
            }
            if (p.hasPotionEffect(PotionEffectType.JUMP)) {
                p.removePotionEffect(PotionEffectType.JUMP);
            }
            if (p.hasPotionEffect(PotionEffectType.SPEED)) {
                p.removePotionEffect(PotionEffectType.SPEED);
            }

            if (p.isFlying() && !(p.getGameMode() == GameMode.CREATIVE)) {
                p.setAllowFlight(false);
                p.setFlying(false);
            }
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

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

        NBTItem nbtItem = new NBTItem(e.getItemDrop().getItemStack());

        if (nbtItem.hasKey("minebomb")) {
            Item drop = e.getItemDrop();

            drop.setPickupDelay(10000);
            drop.setCustomNameVisible(true);
            drop.setCustomName(StringUtils.colorize("&d&l2.5s"));

            new BukkitRunnable() {

                int count = 50;
                int secs;
                int ms;

                Location loc;
                World world;

                @Override
                public void run() {
                    if (count == 0) {
                        loc = e.getItemDrop().getLocation();
                        world.createExplosion(e.getItemDrop().getLocation().clone().add(0,1,0), 0);
                        drop.remove();
                        Mine mine = Mines.getAPI().getByLocation(e.getItemDrop().getLocation());

                        if (mine == null) {
                            loc.clone().subtract(0, 2, 0);
                            mine = Mines.getAPI().getByLocation(loc);
                        }

                        List<Collection<ItemStack>> dropList = new ArrayList<>();
                        int radius = 5;

                        int bx = loc.getBlockX();
                        int by = loc.getBlockY();
                        int bz = loc.getBlockZ();

                        for (int x = bx - radius; x <= bx + radius; x++) {
                            for (int y = by - radius; y <= by + radius; y++) {
                                for (int z = bz - radius; z <= bz + radius; z++) {
                                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
                                    if (distance < radius * radius) {
                                        Block loopBlock = world.getBlockAt(x, y, z);

                                        if (Mines.getAPI().getByLocation(loopBlock.getLocation()) == null) continue;
                                        if (mine == null) mine = Mines.getAPI().getByLocation(loopBlock.getLocation());

                                        if (loopBlock.getType() != Material.AIR) {
                                            dropList.add(loopBlock.getDrops());
                                            setAir(loopBlock.getLocation());
                                        }
                                    }
                                }
                            }
                        }

                        if (mine != null) {
                            mine.setBlocksBroken(mine.getBlocksBroken() + dropList.size());
                            mine.setBlocksRemaining(mine.getBlocksRemaining() - dropList.size());
                        }

                        for (ItemStack item : p.getInventory().getContents()) {
                            if (item == null || item.getType() == Material.AIR) continue;

                            NBTItem nbtItem = new NBTItem(item);
                            if (nbtItem.hasKey("levelpick")) {
                                ItemMeta meta = item.getItemMeta();
                                if (meta.hasEnchant(PrisonEnchants.TELEPATHY)) {
                                    if (meta.hasEnchant(PrisonEnchants.AUTOSMELT)) {
                                        dropList.forEach(drops -> p.getInventory().addItem(autoSmelt(drops.iterator().next())));
                                    } else {
                                        dropList.forEach(drops -> p.getInventory().addItem(drops.iterator().next()));
                                    }
                                } else {
                                    if (meta.hasEnchant(PrisonEnchants.AUTOSMELT)) {
                                        dropList.forEach(drops -> world.dropItemNaturally(drop.getLocation(), autoSmelt(drops.iterator().next())));
                                    } else {
                                        dropList.forEach(drops -> world.dropItemNaturally(drop.getLocation(), drops.iterator().next()));
                                    }
                                }
                                return;
                            }
                        }
                        dropList.forEach(drops -> world.dropItemNaturally(drop.getLocation(), drops.iterator().next()));
                        cancel();
                    }

                    secs = Math.floorDiv(count, 20);
                    ms = count % 20;

                    drop.setCustomName(StringUtils.colorize("&d&l" + secs + "." + ms + "s"));
                    count--;
                }
            }.runTaskTimer(main, 0L, 1L);
        }
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand().getType() == Material.AIR) return;
        if (!p.getInventory().getItemInMainHand().getType().toString().endsWith("PICKAXE")) return;

        Block block = e.getBlock();
        World world = block.getWorld();

        Mine mine = Mines.getAPI().getByLocation(block.getLocation());

        if (mine == null || mine.isResetting()) return;

        ItemStack pickaxe = p.getInventory().getItemInMainHand();
        ItemMeta meta = pickaxe.getItemMeta();

        if (meta == null) return;

        NBTItem nbtItem = new NBTItem(p.getInventory().getItemInMainHand());

        if (!nbtItem.hasKey("levelpick")) return;

        if (meta.hasEnchant(PrisonEnchants.TELEPATHY)) {
            e.setDropItems(false);
            if (meta.hasEnchant(PrisonEnchants.AUTOSMELT)) {
                p.getInventory().addItem(autoSmelt(block.getDrops().iterator().next()));
            } else {
                p.getInventory().addItem(block.getDrops().iterator().next());
            }
        } else {
            if (meta.hasEnchant(PrisonEnchants.AUTOSMELT)) {
                Collection<ItemStack> drops = e.getBlock().getDrops();
                e.setDropItems(false);
                world.dropItemNaturally(block.getLocation(), autoSmelt(drops.stream().iterator().next()));
            }
        }

        ArrayList<Collection<ItemStack>> dropList = new ArrayList<>();

        if (meta.hasEnchant(PrisonEnchants.JACKHAMMER)) {
            if (trigger(PrisonEnchants.JACKHAMMER, meta)) {
                Region region = mine.getRegion();
                int y = block.getY();
                for (int x = region.getMinX(); x < (region.getMaxX() + 1); x++) {
                    for (int z = region.getMinZ(); z < (region.getMaxZ() + 1); z++) {
                        Block loopBlock = world.getBlockAt(x, y, z);
                        if (loopBlock.getType() != Material.AIR) {
                            dropList.add(loopBlock.getDrops());
                            setAir(loopBlock.getLocation());
                        }
                    }
                }
            }
        }

        if (meta.hasEnchant(PrisonEnchants.LASER)) {
            if (trigger(PrisonEnchants.LASER, meta)) {
                if (ThreadLocalRandom.current().nextBoolean()) {
                    Region region = mine.getRegion();
                    int z = block.getZ();
                    for (int x = region.getMinX(); x < (region.getMaxX() + 1); x++) {
                        for (int y = region.getMinY(); y < (region.getMaxY() + 1); y++) {
                            Block loopBlock = world.getBlockAt(x, y, z);
                            if (loopBlock.getType() != Material.AIR) {
                                dropList.add(loopBlock.getDrops());
                                setAir(loopBlock.getLocation());
                            }
                        }
                    }
                } else {
                    Region region = mine.getRegion();
                    int x = block.getX();
                    for (int z = region.getMinZ(); z < (region.getMaxZ() + 1); z++) {
                        for (int y = region.getMinY(); y < (region.getMaxY() + 1); y++) {
                            Block loopBlock = world.getBlockAt(x, y, z);
                            if (loopBlock.getType() != Material.AIR) {
                                dropList.add(loopBlock.getDrops());
                                setAir(loopBlock.getLocation());
                            }
                        }
                    }
                }
            }
        }

        if (meta.hasEnchant(PrisonEnchants.EXPLOSIVE)) {
            if (trigger(PrisonEnchants.EXPLOSIVE, meta)) {
                int radius = 5;

                Location loc = block.getLocation().clone();

                world.createExplosion(loc.clone().subtract(0,2,0), 0);

                int bx = loc.getBlockX();
                int by = loc.getBlockY();
                int bz = loc.getBlockZ();

                for (int x = bx - radius; x <= bx + radius; x++) {
                    for (int y = by - radius; y <= by + radius; y++) {
                        for (int z = bz - radius; z <= bz + radius; z++) {
                            double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
                            if (distance < radius * radius) {
                                Block loopBlock = world.getBlockAt(x, y, z);

                                if (Mines.getAPI().getByLocation(loopBlock.getLocation()) == null) continue;

                                if (loopBlock.getType() != Material.AIR) {
                                    dropList.add(loopBlock.getDrops());
                                    setAir(loopBlock.getLocation());
                                }
                            }

                        }
                    }
                }
            }
        }

        if (meta.hasEnchant(PrisonEnchants.LIGHTNING)) {
            if (trigger(PrisonEnchants.LIGHTNING, meta)) {
                Location loc = block.getLocation();

                world.strikeLightningEffect(block.getLocation());

                for (int x = (loc.getBlockX() - 1); x < (loc.getBlockX() + 2); x++) {
                    for (int y = (loc.getBlockY() - 1); y < (loc.getBlockY() + 2); y++) {
                        for (int z = (loc.getBlockZ() - 1); z < (loc.getBlockZ() + 2); z++) {
                            Block loopBlock = world.getBlockAt(x, y, z);

                            if (Mines.getAPI().getByLocation(loopBlock.getLocation()) == null) continue;

                            if (loopBlock.getType() != Material.AIR) {
                                dropList.add(loopBlock.getDrops());
                                setAir(loopBlock.getLocation());
                            }
                        }
                    }
                }

            }
        }

        if (meta.hasEnchant(PrisonEnchants.GEMCHANCE)) {
            if (trigger(PrisonEnchants.GEMCHANCE, meta)) {
                if (meta.hasEnchant(PrisonEnchants.AUTODEPOSIT)) {
                    User user = main.getDatabaseHandler().getUserByPlayer(p);
                    user.setGems(user.getGems() + 1);
                    main.getDatabaseHandler().saveUser(user);
                } else {
                    p.getInventory().addItem(new ItemBuilder(
                            Material.matchMaterial(config.getString("enchants.gemchance.gem.material")))
                            .setName(config.getString("enchants.gemchance.gem.name"))
                            .setLore(config.getStringList("enchants.gemchance.gem.lore"))
                            .toItemStack());
                }
                StringUtils.sendMessage(p, Messager.GEM_CHANCE);
            }
        }

        if (meta.hasEnchant(PrisonEnchants.KEYFINDER)) {
            // Todo remake chance system
            if (trigger(PrisonEnchants.KEYFINDER, meta)) {
                double chance = ThreadLocalRandom.current().nextDouble();

                for (String cmd : config.getStringList("enchants.keyfinder.commands")) {
                    String command = cmd.split(" ; ")[0].trim();
                    double c = Double.parseDouble(cmd.split(" ; ")[1].trim());
                    if (c > chance) {
                        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command.replace("%player%", p.getName()));
                    }
                }
            }
        }

        mine.setBlocksBroken(mine.getBlocksBroken() + dropList.size());
        mine.setBlocksRemaining(mine.getBlocksRemaining() - dropList.size());

        if (meta.hasEnchant(PrisonEnchants.TELEPATHY)) {
            if (meta.hasEnchant(PrisonEnchants.AUTOSMELT)) {
                dropList.forEach(drops -> p.getInventory().addItem(autoSmelt(drops.iterator().next())));
            } else {
                dropList.forEach(drops -> p.getInventory().addItem(drops.iterator().next()));
            }
        } else {
            if (meta.hasEnchant(PrisonEnchants.AUTOSMELT)) {
                dropList.forEach(drops -> world.dropItemNaturally(block.getLocation(), autoSmelt(drops.iterator().next())));
            } else {
                dropList.forEach(drops -> world.dropItemNaturally(block.getLocation(), drops.iterator().next()));
            }
        }

        int blockExp = config.getInt("blocks." + block.getType(), 1);

        int level = nbtItem.getInteger("level");
        int brokenBlocks = nbtItem.getInteger("blocks");

        int exp = nbtItem.getInteger("exp");
        int neededExp = main.getConfig().getInt("levels." + (level + 1) + ".exp");

        nbtItem.setInteger("blocks", brokenBlocks + 1);

        if (neededExp != 0) {
            if (exp + blockExp >= neededExp) {
                nbtItem.setInteger("exp", ((exp + blockExp) - neededExp));
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

        if (meta.hasEnchant(PrisonEnchants.AUTOBLOCK)) {
            List<Material> checked = new ArrayList<>();
            int ingots;
            int blocks;
            for (ItemStack item : p.getInventory().getContents()) {
                if (item == null || item.getType() == Material.AIR || checked.contains(item.getType())) continue;
                checked.add(item.getType());
                ingots = getAmountOf(p.getInventory(), item.getType());
                blocks = Math.floorDiv(ingots, 9);

                if (blocks == 0) continue;

                switch (item.getType()) {
                    case IRON_INGOT:
                        if (ingots > 64) {
                            for (int i = 0; i < Math.floorDiv(ingots, 64); i++) {
                                p.getInventory().remove(new ItemStack(Material.IRON_INGOT, 64));
                            }
                            p.getInventory().remove(new ItemStack(Material.IRON_INGOT, ingots % 64));
                        } else {
                            p.getInventory().remove(new ItemStack(Material.IRON_INGOT, ingots));
                        }
                        p.getInventory().addItem(new ItemStack(Material.IRON_BLOCK, blocks));
                        break;
                    case GOLD_INGOT:
                        if (ingots > 64) {
                            for (int i = 0; i < Math.floorDiv(ingots, 64); i++) {
                                p.getInventory().remove(new ItemStack(Material.GOLD_INGOT, 64));
                            }
                            p.getInventory().remove(new ItemStack(Material.GOLD_INGOT, ingots % 64));
                        } else {
                            p.getInventory().remove(new ItemStack(Material.GOLD_INGOT, ingots));
                        }
                        p.getInventory().addItem(new ItemStack(Material.GOLD_BLOCK, blocks));
                        break;
                    case DIAMOND:
                        if (ingots > 64) {
                            for (int i = 0; i < Math.floorDiv(ingots, 64); i++) {
                                p.getInventory().remove(new ItemStack(Material.DIAMOND, 64));
                            }
                            p.getInventory().remove(new ItemStack(Material.DIAMOND, ingots % 64));
                        } else {
                            p.getInventory().remove(new ItemStack(Material.DIAMOND, ingots));
                        }
                        p.getInventory().addItem(new ItemStack(Material.DIAMOND_BLOCK, blocks));
                        break;
                    case EMERALD:
                        if (ingots > 64) {
                            for (int i = 0; i < Math.floorDiv(ingots, 64); i++) {
                                p.getInventory().remove(new ItemStack(Material.EMERALD, 64));
                            }
                            p.getInventory().remove(new ItemStack(Material.EMERALD, ingots % 64));
                        } else {
                            p.getInventory().remove(new ItemStack(Material.EMERALD, ingots));
                        }
                        p.getInventory().addItem(new ItemStack(Material.EMERALD_BLOCK, blocks));
                        break;
                    case REDSTONE:
                        if (ingots > 64) {
                            for (int i = 0; i < Math.floorDiv(ingots, 64); i++) {
                                p.getInventory().remove(new ItemStack(Material.REDSTONE, 64));
                            }
                            p.getInventory().remove(new ItemStack(Material.REDSTONE, ingots % 64));
                        } else {
                            p.getInventory().remove(new ItemStack(Material.REDSTONE, ingots));
                        }
                        p.getInventory().addItem(new ItemStack(Material.REDSTONE_BLOCK, blocks));
                        break;
                    case LAPIS_LAZULI:
                        if (ingots > 64) {
                            for (int i = 0; i < Math.floorDiv(ingots, 64); i++) {
                                p.getInventory().remove(new ItemStack(Material.LAPIS_LAZULI, 64));
                            }
                            p.getInventory().remove(new ItemStack(Material.LAPIS_LAZULI, ingots % 64));
                        } else {
                            p.getInventory().remove(new ItemStack(Material.LAPIS_LAZULI, ingots));
                        }
                        p.getInventory().addItem(new ItemStack(Material.LAPIS_BLOCK, blocks));
                        break;
                    case QUARTZ:
                        if (ingots > 64) {
                            for (int i = 0; i < Math.floorDiv(ingots, 64); i++) {
                                p.getInventory().remove(new ItemStack(Material.QUARTZ, 64));
                            }
                            p.getInventory().remove(new ItemStack(Material.QUARTZ, ingots % 64));
                        } else {
                            p.getInventory().remove(new ItemStack(Material.QUARTZ, ingots));
                        }
                        p.getInventory().addItem(new ItemStack(Material.QUARTZ_BLOCK, blocks));
                        break;
                    case COAL:
                        if (ingots > 64) {
                            for (int i = 0; i < Math.floorDiv(ingots, 64); i++) {
                                p.getInventory().remove(new ItemStack(Material.COAL, 64));
                            }
                            p.getInventory().remove(new ItemStack(Material.COAL, ingots % 64));
                        } else {
                            p.getInventory().remove(new ItemStack(Material.COAL, ingots));
                        }
                        p.getInventory().addItem(new ItemStack(Material.COAL_BLOCK, blocks));
                        break;
                }
            }
        }

        if (meta.hasEnchant(PrisonEnchants.AUTOSELL)) {
            if (p.getInventory().firstEmpty() == -1) {
                p.performCommand("sell all");
                invFull.remove(p.getUniqueId());
            }
        }

        if (p.getInventory().firstEmpty() == -1) {
            if (invFull.contains(p.getUniqueId())) return;
            invFull.add(p.getUniqueId());
            p.sendTitle("", StringUtils.colorize(Messager.INVENTORY_FULL), 20, 100, 20);
            Bukkit.getScheduler().runTaskLater(main, () -> invFull.remove(p.getUniqueId()), 60 * 20L);
        }
    }

    public static void setAir(Location loc) {
        net.minecraft.server.v1_16_R3.World nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();
        BlockPosition bp = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        IBlockData ibd = net.minecraft.server.v1_16_R3.Block.getByCombinedId(0);
        nmsWorld.setTypeAndData(bp, ibd, 2);
    }

    private boolean trigger(Enchantment enchantment, ItemMeta meta) {
        int level = meta.getEnchantLevel(enchantment);
        int chance = (int) ((level * main.getConfig().getDouble("enchants." + enchantment.getKey().getKey().toLowerCase() + ".chance")) * 100);
        int r = ThreadLocalRandom.current().nextInt(99) + 1;
        return chance > r;
    }

    private ItemStack autoSmelt(ItemStack ore) {
        if (ore == null) return null;
        if (ore.getType() == Material.IRON_ORE) {
            return new ItemStack(Material.IRON_INGOT, ore.getAmount());
        } else if (ore.getType() == Material.GOLD_ORE) {
            return new ItemStack(Material.GOLD_INGOT, ore.getAmount());
        }
        return ore;
    }

    private int getAmountOf(Inventory inv, Material mat) {
        int amount = 0;

        for (ItemStack item : inv.getContents()) {
            if (item == null || item.getType() == Material.AIR || !(item.getType() == mat)) continue;
            amount += item.getAmount();
        }

        return amount;
    }
}
