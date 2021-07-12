package me.matzhilven.slamcore.commands.gems.subcommands;

import me.matzhilven.slamcore.SlamCore;
import me.matzhilven.slamcore.commands.SubCommand;
import me.matzhilven.slamcore.data.User;
import me.matzhilven.slamcore.utils.Messager;
import me.matzhilven.slamcore.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GemsRemoveSubCommand implements SubCommand {

    private final SlamCore main;

    public GemsRemoveSubCommand(SlamCore main) {
        this.main = main;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {
        if (args.length != 3) {
            StringUtils.sendMessage(sender, Messager.REMOVE_GEMS_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            StringUtils.sendMessage(sender, Messager.INVALID_TARGET.replace("%player%", args[1]));
            return;
        }

        int gems;

        try  {
            gems = Integer.parseInt(args[2]);
        } catch (NumberFormatException ignored) {
            StringUtils.sendMessage(sender, Messager.INVALID_NUMBER.replace("%arg%", args[2]));
            return;
        }

        if (gems <= 0) {
            StringUtils.sendMessage(sender, Messager.INVALID_NUMBER.replace("%arg%", args[2]));
            return;
        }

        StringUtils.sendMessage(sender, Messager.REMOVE_GEMS_SENDER.replace("%target%", target.getName()).replace("%gems%", StringUtils.format(gems)));
        StringUtils.sendMessage(target, Messager.REMOVE_GEMS.replace("%gems%", StringUtils.format(gems)));


        User user = main.getDatabaseHandler().getUserByPlayer(target);
        user.setGems(user.getGems() - gems < 0 ? 0 : user.getGems() - gems);
        main.getDatabaseHandler().saveUser(user);
    }

    @Override
    public String getPermission() {
        return "core.gem.admin";
    }
}
