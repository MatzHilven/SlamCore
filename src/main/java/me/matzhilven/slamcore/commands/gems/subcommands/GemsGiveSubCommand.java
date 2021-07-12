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

public class GemsGiveSubCommand implements SubCommand {

    private final SlamCore main;

    public GemsGiveSubCommand(SlamCore main) {
        this.main = main;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {
        if (!(sender instanceof Player)) {
            StringUtils.sendMessage(sender, Messager.INVALID_SENDER);
            return;
        }

        if (args.length != 3) {
            StringUtils.sendMessage(sender, Messager.GIVE_GEMS_USAGE);
            return;
        }

        Player player = (Player) sender;

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            StringUtils.sendMessage(sender, Messager.INVALID_TARGET.replace("%player%", args[1]));
            return;
        }

        if (player == target) {
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

        User user = main.getDatabaseHandler().getUserByPlayer(player);

        if (user.getGems() < gems) {
            StringUtils.sendMessage(sender, Messager.INSUFFICIENT_FUNDS);
            return;
        }

        StringUtils.sendMessage(sender, Messager.GIVE_GEMS.replace("%target%", target.getName()).replace("%gems%", StringUtils.format(gems)));
        StringUtils.sendMessage(target, Messager.RECEIVE_GEMS.replace("%sender%", player.getName()).replace("%gems%", StringUtils.format(gems)));

        User targetUser = main.getDatabaseHandler().getUserByPlayer(player);

        user.setGems(user.getGems() - gems);
        targetUser.setGems(targetUser.getGems() + gems);

        main.getDatabaseHandler().saveUsers(user,targetUser);
    }

    @Override
    public String getPermission() {
        return "core.gem";
    }
}
