package me.matzhilven.slamcore.menus.gembank;

import me.matzhilven.slamcore.menus.Menu;
import me.matzhilven.slamcore.utils.ItemBuilder;
import me.matzhilven.slamcore.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GemBankMenu extends Menu {

    public GemBankMenu(Player p) {
        super(p);
    }

    @Override
    public String getMenuName() {
        return config.getString("gembank.main-menu.name");
    }

    @Override
    public int getSlots() {
        return config.getInt("gembank.main-menu.slots");
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        if (e.getSlot() == config.getInt("gembank.main-menu.withdraw.slot")) {
            WithdrawMenu withdrawMenu = new WithdrawMenu(p);
            withdrawMenu.open();
        } else if (e.getSlot() == config.getInt("gembank.main-menu.deposit.slot")) {
            DepositMenu depositMenu = new DepositMenu(p);
            depositMenu.open();
        } else if (e.getSlot() == config.getInt("gembank.main-menu.close.slot")) {
            p.closeInventory();
        }
    }

    @Override
    public void setMenuItems() {

        inventory.setItem(config.getInt("gembank.main-menu.info.slot"),
                new ItemBuilder(Material.matchMaterial(config.getString("gembank.main-menu.info.material")))
                        .setName(config.getString("gembank.main-menu.info.name"))
                        .setLore(config.getStringList("gembank.main-menu.info.lore"))
                        .replace("%gems%", StringUtils.format(main.getDatabaseHandler().getUserByPlayer(p).getGems()))
                        .toItemStack()
        );

        inventory.setItem(config.getInt("gembank.main-menu.withdraw.slot"),
                new ItemBuilder(Material.matchMaterial(config.getString("gembank.main-menu.withdraw.material")))
                        .setName(config.getString("gembank.main-menu.withdraw.name"))
                        .setLore(config.getStringList("gembank.main-menu.withdraw.lore"))
                        .toItemStack()
        );

        inventory.setItem(config.getInt("gembank.main-menu.deposit.slot"),
                new ItemBuilder(Material.matchMaterial(config.getString("gembank.main-menu.deposit.material")))
                        .setName(config.getString("gembank.main-menu.deposit.name"))
                        .setLore(config.getStringList("gembank.main-menu.deposit.lore"))
                        .toItemStack()
        );

        inventory.setItem(config.getInt("gembank.main-menu.close.slot"),
                new ItemBuilder(Material.matchMaterial(config.getString("gembank.main-menu.close.material")))
                        .setName(config.getString("gembank.main-menu.close.name"))
                        .setLore(config.getStringList("gembank.main-menu.close.lore"))
                        .toItemStack()
        );

        setFillerGlass(new ItemBuilder(Material.matchMaterial(config.getString("gembank.filler.material")))
                .setName(config.getString("pickaxe-menu.filler.name"))
                .setLore(config.getStringList("pickaxe-menu.filler.lore"))
                .toItemStack());
    }
}
