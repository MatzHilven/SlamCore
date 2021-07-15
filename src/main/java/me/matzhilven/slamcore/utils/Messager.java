package me.matzhilven.slamcore.utils;


import me.matzhilven.slamcore.SlamCore;

import java.util.List;

public class Messager {

    public static final String INVALID_PERMISSION = SlamCore.getInstance().getConfig().getString("messages.invalid-permission");
    public static final String INVALID_SENDER = SlamCore.getInstance().getConfig().getString("messages.invalid-sender");
    public static final String INVALID_TARGET = SlamCore.getInstance().getConfig().getString("messages.invalid-target");
    public static final String INVALID_NUMBER = SlamCore.getInstance().getConfig().getString("messages.invalid-number");
    public static final String INSUFFICIENT_FUNDS = SlamCore.getInstance().getConfig().getString("messages.insufficient-funds");
    public static final String INVENTORY_FULL = SlamCore.getInstance().getConfig().getString("messages.inventory-full");

    public static final List<String> USAGE_GEMS = SlamCore.getInstance().getConfig().getStringList("messages.gems-usage");
    public static final List<String> USAGE_PICKAXE = SlamCore.getInstance().getConfig().getStringList("messages.pickaxe-usage");
    public static final String SET_GEMS_USAGE = SlamCore.getInstance().getConfig().getString("messages.set-gems-usage");
    public static final String ADD_GEMS_USAGE = SlamCore.getInstance().getConfig().getString("messages.add-gems-usage");
    public static final String REMOVE_GEMS_USAGE = SlamCore.getInstance().getConfig().getString("messages.remove-gems-usage");
    public static final String GIVE_GEMS_USAGE = SlamCore.getInstance().getConfig().getString("messages.give-gems-usage");
    public static final String GIVE_MINEBOMB_USAGE = SlamCore.getInstance().getConfig().getString("messages.give-minebomb-usage");

    public static final String GIVE_PICKAXE_USAGE = SlamCore.getInstance().getConfig().getString("messages.give-pickaxe-usage");
    public static final String RESET_PICKAXE_USAGE = SlamCore.getInstance().getConfig().getString("messages.reset-pickaxe-usage");
    public static final String SET_BLOCKS_PICKAXE_USAGE = SlamCore.getInstance().getConfig().getString("messages.set-blocks-usage");
    public static final String SET_LEVEL_PICKAXE_USAGE = SlamCore.getInstance().getConfig().getString("messages.set-level-usage");
    public static final String SET_EXP_PICKAXE_USAGE = SlamCore.getInstance().getConfig().getString("messages.set-exp-usage");

    public static final String GEMS = SlamCore.getInstance().getConfig().getString("messages.gems");
    public static final String SET_GEMS = SlamCore.getInstance().getConfig().getString("messages.set-gems");
    public static final String SET_GEMS_SENDER = SlamCore.getInstance().getConfig().getString("messages.set-gems-sender");
    public static final String ADD_GEMS = SlamCore.getInstance().getConfig().getString("messages.add-gems");
    public static final String ADD_GEMS_SENDER = SlamCore.getInstance().getConfig().getString("messages.add-gems-sender");
    public static final String REMOVE_GEMS = SlamCore.getInstance().getConfig().getString("messages.remove-gems");
    public static final String REMOVE_GEMS_SENDER = SlamCore.getInstance().getConfig().getString("messages.remove-gems-sender");
    public static final String GIVE_GEMS = SlamCore.getInstance().getConfig().getString("messages.give-gems");
    public static final String RECEIVE_GEMS = SlamCore.getInstance().getConfig().getString("messages.receive-gems");
    public static final String GEM_CHANCE = SlamCore.getInstance().getConfig().getString("messages.gem-chance");

    public static final String GIVE_PICKAXE = SlamCore.getInstance().getConfig().getString("messages.give-pickaxe");
    public static final String RECEIVE_PICKAXE = SlamCore.getInstance().getConfig().getString("messages.receive-pickaxe");
    public static final String RESET_PICKAXE = SlamCore.getInstance().getConfig().getString("messages.reset-pickaxe");
    public static final String RESET_PICKAXE_SENDER = SlamCore.getInstance().getConfig().getString("messages.reset-pickaxe-sender");
    public static final String SET_LEVEL = SlamCore.getInstance().getConfig().getString("messages.set-level");
    public static final String SET_LEVEL_SENDER = SlamCore.getInstance().getConfig().getString("messages.set-level-sender");
    public static final String SET_BLOCKS = SlamCore.getInstance().getConfig().getString("messages.set-blocks");
    public static final String SET_BLOCKS_SENDER = SlamCore.getInstance().getConfig().getString("messages.set-blocks-sender");
    public static final String SET_EXP = SlamCore.getInstance().getConfig().getString("messages.set-exp");
    public static final String SET_EXP_SENDER = SlamCore.getInstance().getConfig().getString("messages.set-exp-sender");

}
