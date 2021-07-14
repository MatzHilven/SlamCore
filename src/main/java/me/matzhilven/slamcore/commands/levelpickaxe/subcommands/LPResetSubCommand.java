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

public class LPResetSubCommand implements SubCommand {
    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {
        if (args.length != 2) {
            StringUtils.sendMessage(sender, Messager.RESET_PICKAXE_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            StringUtils.sendMessage(sender, Messager.INVALID_TARGET.replace("%player%", args[1]));
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
                nbtItem.setInteger("level", 1);
                nbtItem.setInteger("exp", 0);
                nbtItem.setInteger("blocks", 0);
                target.getInventory().setItem(i,nbtItem.getItem());
            }
            i++;
        }


        StringUtils.sendMessage(sender, Messager.RESET_PICKAXE_SENDER.replace("%target%", target.getName()));
        StringUtils.sendMessage(target, Messager.RESET_PICKAXE);
    }

    @Override
    public String getPermission() {
        return "core.levelpick";
    }
}
