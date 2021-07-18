package me.matzhilven.slamcore.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.matzhilven.slamcore.SlamCore;
import me.matzhilven.slamcore.utils.StringUtils;
import org.bukkit.OfflinePlayer;

public class PlaceHolderAPI extends PlaceholderExpansion {

    private final SlamCore main;

    public PlaceHolderAPI(SlamCore main) {
        this.main = main;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "gem";
    }

    @Override
    public String getAuthor() {
        return main.getDescription().getAuthors().get(0);
    }

    @Override
    public String getVersion() {
        return main.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {

        if (identifier.equals("balance")) {
            return StringUtils.format(main.getDatabaseHandler().getUserByUUID(player.getUniqueId()).getGems());
        }

        return null;
    }
}
