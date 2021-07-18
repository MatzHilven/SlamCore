package me.matzhilven.slamcore.menus.gembank;

import me.matzhilven.slamcore.data.User;
import me.matzhilven.slamcore.menus.Menu;
import me.matzhilven.slamcore.utils.ItemBuilder;
import me.matzhilven.slamcore.utils.Messager;
import me.matzhilven.slamcore.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class DepositMenu extends Menu {

    public DepositMenu(Player p) {
        super(p);
    }

    @Override
    public String getMenuName() {
        return config.getString("gembank.deposit-menu.name");
    }

    @Override
    public int getSlots() {
        return config.getInt("gembank.deposit-menu.slots");
    }

    @Override
    public void handleClick(InventoryClickEvent e) {

        User user = main.getDatabaseHandler().getUserByPlayer(p);
        long bankGems = user.getGems();
        int inventoryGems = getGemsInInventory();

        int toDeposit = 1;

        if (e.getSlot() == config.getInt("gembank.deposit-menu.10.slot")) {
            toDeposit = 10;
        } else if (e.getSlot() == config.getInt("gembank.deposit-menu.all.slot")) {
            toDeposit = inventoryGems;
        } else if (e.getSlot() == config.getInt("gembank.deposit-menu.back.slot")) {
            GemBankMenu gemBankMenu = new GemBankMenu(p);
            gemBankMenu.open();
            return;
        }

        if (inventoryGems < toDeposit || toDeposit == 0) {
            StringUtils.sendMessage(p, Messager.INVALID_DEPOSIT.replace("%gems%",
                    toDeposit == 0 ? "any" : String.valueOf(toDeposit)));
            return;
        }

        user.setGems((bankGems + toDeposit));

        removeGems(toDeposit);

        StringUtils.sendMessage(p, Messager.DEPOSIT_GEMS
                .replace("%gems%", StringUtils.format(toDeposit))
                .replace("%balance%", StringUtils.format(user.getGems())));

        main.getDatabaseHandler().saveUser(user);
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(config.getInt("gembank.deposit-menu.1.slot"),
                new ItemBuilder(Material.matchMaterial(config.getString("gembank.deposit-menu.1.material")),
                        config.getInt("gembank.deposit-menu.1.amount"))
                        .setName(config.getString("gembank.deposit-menu.1.name"))
                        .setLore(config.getStringList("gembank.deposit-menu.1.lore"))
                        .toItemStack()
        );

        inventory.setItem(config.getInt("gembank.deposit-menu.10.slot"),
                new ItemBuilder(Material.matchMaterial(config.getString("gembank.deposit-menu.10.material")),
                        config.getInt("gembank.deposit-menu.10.amount"))
                        .setName(config.getString("gembank.deposit-menu.10.name"))
                        .setLore(config.getStringList("gembank.deposit-menu.10.lore"))
                        .toItemStack()
        );

        inventory.setItem(config.getInt("gembank.deposit-menu.all.slot"),
                new ItemBuilder(Material.matchMaterial(config.getString("gembank.deposit-menu.all.material")),
                        config.getInt("gembank.deposit-menu.all.amount"))
                        .setName(config.getString("gembank.deposit-menu.all.name"))
                        .setLore(config.getStringList("gembank.deposit-menu.all.lore"))
                        .toItemStack()
        );

        inventory.setItem(config.getInt("gembank.deposit-menu.back.slot"),
                new ItemBuilder(Material.matchMaterial(config.getString("gembank.deposit-menu.back.material")))
                        .setName(config.getString("gembank.deposit-menu.back.name"))
                        .setLore(config.getStringList("gembank.deposit-menu.back.lore"))
                        .toItemStack()
        );

        setFillerGlass(new ItemBuilder(Material.matchMaterial(config.getString("gembank.filler.material")))
                .setName(config.getString("pickaxe-menu.filler.name"))
                .setLore(config.getStringList("pickaxe-menu.filler.lore"))
                .toItemStack());
    }
}
