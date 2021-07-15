package me.matzhilven.slamcore.commands.minebomb;

import de.tr7zw.nbtapi.NBTItem;
import me.matzhilven.slamcore.SlamCore;
import me.matzhilven.slamcore.utils.ItemBuilder;
import me.matzhilven.slamcore.utils.Messager;
import me.matzhilven.slamcore.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MineBombCommand implements CommandExecutor, TabExecutor {

    private final SlamCore main;

    public MineBombCommand(SlamCore main) {
        this.main = main;
        main.getCommand("givebomb").setExecutor(this);
        main.getCommand("givebomb").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("core.minebomb")) {
            StringUtils.sendMessage(sender, Messager.INVALID_PERMISSION);
            return true;
        }

        if (args.length != 2) {
            StringUtils.sendMessage(sender, Messager.GIVE_MINEBOMB_USAGE);
            return true;
        }

        int amount;

        try  {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException ignored) {
            StringUtils.sendMessage(sender, Messager.INVALID_NUMBER.replace("%arg%", args[2]));
            return true;
        }

        if (amount < 0) {
            StringUtils.sendMessage(sender, Messager.INVALID_NUMBER.replace("%arg%", args[2]));
            return true;
        }

        ItemStack minebomb = new ItemBuilder(Material.GUNPOWDER, amount)
                .setName(StringUtils.colorize(main.getConfig().getString("minebomb.name")))
                .setLore(StringUtils.colorize(main.getConfig().getStringList("minebomb.lore")))
                .toItemStack();

        NBTItem nbtItem = new NBTItem(minebomb);

        nbtItem.setBoolean("minebomb", true);

        ((Player) sender).getInventory().addItem(nbtItem.getItem());


        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        ArrayList<String> cmds = new ArrayList<>();
        switch (args.length) {
            case 1:
                cmds.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
                return StringUtil.copyPartialMatches(args[0], cmds, new ArrayList<>());
            case 2:
                cmds.add("1");
                cmds.add("5");
                cmds.add("10");
                return StringUtil.copyPartialMatches(args[1], cmds, new ArrayList<>());
        }
        return null;
    }
}
