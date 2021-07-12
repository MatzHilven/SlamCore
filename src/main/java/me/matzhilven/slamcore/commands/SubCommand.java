package me.matzhilven.slamcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface SubCommand {
    void onCommand(CommandSender sender, Command command, String[] args);
    String getPermission();
}
