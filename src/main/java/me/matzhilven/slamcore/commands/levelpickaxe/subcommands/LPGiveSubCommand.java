package me.matzhilven.slamcore.commands.levelpickaxe.subcommands;

import me.matzhilven.slamcore.commands.SubCommand;
import me.matzhilven.slamcore.levelpickaxe.LevelPickaxe;
import me.matzhilven.slamcore.utils.Messager;
import me.matzhilven.slamcore.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LPGiveSubCommand implements SubCommand {

    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {
        if (args.length != 2) {
            StringUtils.sendMessage(sender, Messager.GIVE_PICKAXE_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            StringUtils.sendMessage(sender, Messager.INVALID_TARGET.replace("%player%", args[1]));
            return;
        }

        LevelPickaxe lp = new LevelPickaxe(1,0,0);

        target.getInventory().addItem(lp.getPickaxe());

        StringUtils.sendMessage(sender, Messager.GIVE_PICKAXE.replace("%target%", target.getName()));
        StringUtils.sendMessage(target, Messager.RECEIVE_PICKAXE);
    }

    @Override
    public String getPermission() {
        return "core.levelpick";
    }
}
