package me.matzhilven.slamcore.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.base.Strings;
import de.tr7zw.nbtapi.NBTItem;
import me.matzhilven.slamcore.SlamCore;
import me.matzhilven.slamcore.listeners.packetwrapper.WrapperPlayServerSetSlot;
import me.matzhilven.slamcore.listeners.packetwrapper.WrapperPlayServerWindowItems;
import me.matzhilven.slamcore.utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class ItemListener extends PacketAdapter {

    private final SlamCore main;
    private final FileConfiguration config;

    public ItemListener(Plugin plugin) {
        super(plugin, PacketType.Play.Server.WINDOW_ITEMS, PacketType.Play.Server.SET_SLOT);
        this.main = (SlamCore) plugin;
        this.config = main.getConfig();
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        if (packet.getType() == PacketType.Play.Server.SET_SLOT) {
            WrapperPlayServerSetSlot setSlot = new WrapperPlayServerSetSlot(packet);
            if (setSlot.getSlotData() != null) {
                ItemStack toModify = setSlot.getSlotData();

                if (modify(toModify) == null) return;

                setSlot.setSlotData(modify(toModify));
            }
        } else if (packet.getType() == PacketType.Play.Server.WINDOW_ITEMS) {
            WrapperPlayServerWindowItems windowItems = new WrapperPlayServerWindowItems(packet);
            ArrayList<ItemStack> result = new ArrayList<>();
            int i = 0;
            for (ItemStack item : windowItems.getSlotData()) {
                if (item == null || item.getType() == Material.AIR) {
                    result.add(item);
                    i++;
                    continue;
                }
                if (modify(item) == null) {
                    result.add(item);
                    i++;
                    continue;
                }
                result.add(modify(item));
                i++;
            }
            windowItems.setSlotData(result);
        }

    }

    private ItemStack modify(ItemStack item) {
        if (item == null || item.getType() == Material.AIR || !item.getType().toString().endsWith("PICKAXE"))
            return null;

        NBTItem nbtItem = new NBTItem(item);

        if (!nbtItem.hasKey("levelpick")) return null;

        int blocksBroken = nbtItem.hasKey("blocks") ? nbtItem.getInteger("blocks") : 0;
        int level = nbtItem.hasKey("level") ? nbtItem.getInteger("level") : 1;

        int exp = nbtItem.hasKey("exp") ? nbtItem.getInteger("exp") : 0;
        int neededExp = config.getInt("levels." + (level + 1) + ".exp");

        ItemMeta meta = item.getItemMeta();

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        List<String> lore = new ArrayList<>();

        HashMap<Integer, String> enchants = new HashMap<>();

        int i = 0;
        for (Map.Entry<Enchantment, Integer> entry : item.getItemMeta().getEnchants().entrySet()) {
            if (entry.getKey().getName().equals("DIG_SPEED")) {
                enchants.put(i, StringUtils.colorize("&7Efficiency " + entry.getValue()));
            } else {
                enchants.put(i, StringUtils.colorize("&7" + entry.getKey().getName() + " " + entry.getValue()));
            }
            i++;
        }

        for (String s : StringUtils.colorize(config.getStringList("pickaxe.lore"))) {
            if (s.contains("Enchant-")) {
                int j = Integer.parseInt(Arrays.toString(s.split("%Enchant-")[1].split("%")).replace("[", "").replace("]", ""));
                if (!enchants.containsKey(j - 1)) {
                    continue;
                }
                lore.add(StringUtils.colorize(s).replace("%Enchant-" + j + "%", enchants.get(j - 1)));
                continue;
            }

            if (enchants.size() == 0 && (s.toLowerCase().contains("enchants") || s.toLowerCase().contains("enchantments")))
                continue;

            lore.add(s
                    .replace("%blocks%", StringUtils.format(blocksBroken))
                    .replace("%exp%", StringUtils.format(exp))
                    .replace("%exp_required%", StringUtils.format(neededExp != 0 ? neededExp : exp))
                    .replace("%level%", String.valueOf(level))
                    .replace("%progress%", getProgressBar(exp, neededExp)));
        }

        meta.setDisplayName(StringUtils.colorize(config.getString("pickaxe.name")
                .replace("%blocks%", StringUtils.format(blocksBroken))
                .replace("%exp%", StringUtils.format(exp))
                .replace("%exp_required%", StringUtils.format(neededExp != 0 ? neededExp : exp))
                .replace("%level%", String.valueOf(level))
                .replace("%progress%", getProgressBar(exp, neededExp))));

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public String getProgressBar(int current, int max) {
        boolean notCompletedBold = false;
        boolean completedBold = false;

        ChatColor bold = ChatColor.BOLD;

        String completedColor;
        String notCompletedColor;

        if (config.getString("progress.not-completed").length() > 2) {
            notCompletedBold = true;
            notCompletedColor = config.getString("progress.not-completed").substring(0, 2);
        } else {
            notCompletedColor = config.getString("progress.not-completed");
        }

        if (config.getString("progress.completed").length() > 2) {
            completedBold = true;
            completedColor = config.getString("progress.completed").substring(0, 2);
        } else {
            completedColor = config.getString("progress.completed");
        }

        if (completedColor == null) {
            completedColor = "&2";
        }
        if (notCompletedColor == null) {
            notCompletedColor = "&4";
        }

        char symbol = config.getString("progress.char").charAt(0);
        int totalBars = config.getInt("progress.bars", 10);

        float percent = max != 0 ? (float) current / max : 1;
        int progressBars = (int) (totalBars * percent);

        return StringUtils.colorize(Strings.repeat("" + completedColor + ((completedBold) ? bold : "") + symbol, progressBars)
                + Strings.repeat("&r" + notCompletedColor + ((notCompletedBold) ? bold : "") + symbol, totalBars - progressBars)
        );
    }
}
