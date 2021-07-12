package me.matzhilven.slamcore.utils;


import me.matzhilven.slamcore.SlamCore;

import java.util.List;

public class Messager {

    public static final String INVALID_PERMISSION = SlamCore.getInstance().getConfig().getString("messages.invalid-permission");
    public static final String INVALID_SENDER = SlamCore.getInstance().getConfig().getString("messages.invalid-sender");
    public static final String INVALID_TARGET = SlamCore.getInstance().getConfig().getString("messages.invalid-target");
    public static final String INVALID_NUMBER = SlamCore.getInstance().getConfig().getString("messages.invalid-number");
    public static final String INSUFFICIENT_FUNDS = SlamCore.getInstance().getConfig().getString("messages.insufficient-funds");

    public static final List<String> USAGE = SlamCore.getInstance().getConfig().getStringList("messages.usage");
    public static final String SET_GEMS_USAGE = SlamCore.getInstance().getConfig().getString("messages.set-gems-usage");
    public static final String ADD_GEMS_USAGE = SlamCore.getInstance().getConfig().getString("messages.add-gems-usage");
    public static final String REMOVE_GEMS_USAGE = SlamCore.getInstance().getConfig().getString("messages.remove-gems-usage");
    public static final String GIVE_GEMS_USAGE = SlamCore.getInstance().getConfig().getString("messages.give-gems-usage");

    public static final String GEMS = SlamCore.getInstance().getConfig().getString("messages.gems");
    public static final String SET_GEMS = SlamCore.getInstance().getConfig().getString("messages.set-gems");
    public static final String SET_GEMS_SENDER = SlamCore.getInstance().getConfig().getString("messages.set-gems-sender");
    public static final String ADD_GEMS = SlamCore.getInstance().getConfig().getString("messages.add-gems");
    public static final String ADD_GEMS_SENDER = SlamCore.getInstance().getConfig().getString("messages.add-gems-sender");
    public static final String REMOVE_GEMS = SlamCore.getInstance().getConfig().getString("messages.remove-gems");
    public static final String REMOVE_GEMS_SENDER = SlamCore.getInstance().getConfig().getString("messages.remove-gems-sender");
    public static final String GIVE_GEMS = SlamCore.getInstance().getConfig().getString("messages.give-gems");
    public static final String RECEIVE_GEMS = SlamCore.getInstance().getConfig().getString("messages.receive-gems");

}
