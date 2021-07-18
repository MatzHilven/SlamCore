package me.matzhilven.slamcore.menus.gembank;

import me.matzhilven.slamcore.menus.Menu;
import me.matzhilven.slamcore.utils.ItemBuilder;
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
        if (e.getSlot() == config.getInt("gembank.deposit-menu.1.slot")) {

        } else if (e.getSlot() == config.getInt("gembank.deposit-menu.10.slot")) {

        } else if (e.getSlot() == config.getInt("gembank.deposit-menu.all.slot")) {

        } else if (e.getSlot() == config.getInt("gembank.deposit-menu.back.slot")) {
            GemBankMenu gemBankMenu = new GemBankMenu(p);
            gemBankMenu.open();
        }
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
