package me.matzhilven.slamcore.commands.gems.gemtop;

import me.matzhilven.slamcore.SlamCore;
import me.matzhilven.slamcore.data.User;
import me.matzhilven.slamcore.utils.Messager;
import me.matzhilven.slamcore.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GemTopCommand implements CommandExecutor {

    private final SlamCore main;

    public GemTopCommand(SlamCore main) {
        this.main = main;
        main.getCommand("gemtop").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("core.gemtop")) {
            StringUtils.sendMessage(sender, Messager.INVALID_PERMISSION);
            return true;
        }

        List<User> users = main.getDatabaseHandler().getAllUsers();

        users.sort(new SortByGems());

        List<String> message = Messager.GEMTOP;
        List<String> finalMessage = new ArrayList<>();

        int index;
        User user;
        for (String msg : message) {
            System.out.println(msg);

            index = message.indexOf(msg);
            if (index == 0) {

                finalMessage.add(msg);
                continue;
            }

            if ((index - 1) >= users.size()) {

                continue;
            }

            user = users.get(index - 1);

            msg = msg.replace("%player-" + index + "%", user.getUsername())
                    .replace("%balance-" + index + "%", StringUtils.format(user.getGems()));

            finalMessage.add(msg);
        }

        StringUtils.sendMessage(sender, finalMessage);

        return false;
    }

    static class SortByGems implements Comparator<User> {

        @Override
        public int compare(User user1, User user2) {
            return Long.compare(user2.getGems(), user1.getGems());
        }
    }
}
