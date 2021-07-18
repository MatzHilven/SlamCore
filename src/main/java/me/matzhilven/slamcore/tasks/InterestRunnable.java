package me.matzhilven.slamcore.tasks;

import me.matzhilven.slamcore.SlamCore;
import me.matzhilven.slamcore.data.DatabaseHandler;
import me.matzhilven.slamcore.data.User;
import me.matzhilven.slamcore.utils.Messager;
import me.matzhilven.slamcore.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class InterestRunnable extends BukkitRunnable {

    private final SlamCore main;
    private final DatabaseHandler databaseHandler;

    public InterestRunnable(SlamCore main) {
        this.main = main;
        this.databaseHandler = main.getDatabaseHandler();
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            User user = databaseHandler.getUserByPlayer(player);
            long gems = user.getGems();
            long newGems = (long) Math.floor(gems * 1.1);
            user.setGems(newGems);
            databaseHandler.saveUser(user);

            StringUtils.sendMessage(player, Messager.INTEREST
                    .replace("%gems%", StringUtils.format(newGems - gems)
                            .replace("%balance%", StringUtils.format(newGems))));
        });
    }
}
