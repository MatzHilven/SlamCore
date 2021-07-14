package me.matzhilven.slamcore.commands.levelpickaxe;

import me.matzhilven.slamcore.SlamCore;
import me.matzhilven.slamcore.commands.SubCommand;
import me.matzhilven.slamcore.data.User;
import me.matzhilven.slamcore.utils.Messager;
import me.matzhilven.slamcore.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LevelPickaxeBaseCommand implements CommandExecutor, TabExecutor {

    private final SlamCore main;
    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public LevelPickaxeBaseCommand(SlamCore main) {
        this.main = main;
        main.getCommand("levelpick").setExecutor(this);
        main.getCommand("levelpick").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("core.levelpick")) {
            StringUtils.sendMessage(sender, Messager.INVALID_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                StringUtils.sendMessage(sender, Messager.INVALID_SENDER);
                return true;
            }

            StringUtils.sendMessage(sender, Messager.USAGE_PICKAXE);
            return true;
        }

        String subCommandString = args[0];

        if (!subCommands.containsKey(subCommandString)) {
            StringUtils.sendMessage(sender, Messager.USAGE_PICKAXE);
            return true;
        }

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission(subCommands.get(args[0]).getPermission())) {
                StringUtils.sendMessage(sender, Messager.INVALID_PERMISSION);
                return true;
            }
        }

        subCommands.get(args[0]).onCommand(sender, command, args);

        return true;
    }

    public void registerSubCommand(String cmd, SubCommand subCommand) {
        subCommands.put(cmd, subCommand);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> cmds = new ArrayList<>();
        switch (args.length) {
            case 1:
                cmds.add("give");
                cmds.add("setlevel");
                cmds.add("setblocks");
                cmds.add("setexp");

                return StringUtil.copyPartialMatches(args[0], cmds, new ArrayList<>());
            case 2:
                cmds.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
                return StringUtil.copyPartialMatches(args[1], cmds, new ArrayList<>());
            case 3:
                cmds.add("1");
                cmds.add("5");
                cmds.add("10");
                return StringUtil.copyPartialMatches(args[2], cmds, new ArrayList<>());
        }
        return null;
    }
}
