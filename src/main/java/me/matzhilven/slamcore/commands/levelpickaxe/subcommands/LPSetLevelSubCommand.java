package me.matzhilven.slamcore.commands.levelpickaxe.subcommands;

import de.tr7zw.nbtapi.NBTItem;
import me.matzhilven.slamcore.commands.SubCommand;
import me.matzhilven.slamcore.utils.Messager;
import me.matzhilven.slamcore.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LPSetLevelSubCommand implements SubCommand {

    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {
        if (args.length != 3) {
            StringUtils.sendMessage(sender, Messager.SET_LEVEL_PICKAXE_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            StringUtils.sendMessage(sender, Messager.INVALID_TARGET.replace("%player%", args[1]));
            return;
        }

        int level;

        try  {
            level = Integer.parseInt(args[2]);
        } catch (NumberFormatException ignored) {
            StringUtils.sendMessage(sender, Messager.INVALID_NUMBER.replace("%arg%", args[2]));
            return;
        }

        if (level <= 0) {
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
                nbtItem.setInteger("level", level);
                target.getInventory().setItem(i,nbtItem.getItem());
            }
            i++;
        }

        StringUtils.sendMessage(sender, Messager.SET_LEVEL_SENDER.replace("%target%", target.getName()).replace("%level%", StringUtils.format(level)));
        StringUtils.sendMessage(target, Messager.SET_LEVEL.replace("%level%", StringUtils.format(level)));
    }

    @Override
    public String getPermission() {
        return "core.levelpick";
    }
}
