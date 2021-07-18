package me.matzhilven.slamcore.commands.gems.gembank;

import me.matzhilven.slamcore.SlamCore;
import me.matzhilven.slamcore.menus.gembank.GemBankMenu;
import me.matzhilven.slamcore.utils.Messager;
import me.matzhilven.slamcore.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GemBankCommand implements CommandExecutor {

    public GemBankCommand(SlamCore main) {
        main.getCommand("gembank").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("core.gembank")) {
            StringUtils.sendMessage(sender, Messager.INVALID_PERMISSION);
            return true;
        }

        if (!(sender instanceof Player)) {
            StringUtils.sendMessage(sender, Messager.INVALID_SENDER);
            return true;
        }

        GemBankMenu gemBankMenu = new GemBankMenu((Player) sender);
        gemBankMenu.open();

        return true;
    }
}
