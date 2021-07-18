package me.matzhilven.slamcore.menus.gembank;

import me.matzhilven.slamcore.data.User;
import me.matzhilven.slamcore.menus.Menu;
import me.matzhilven.slamcore.utils.ItemBuilder;
import me.matzhilven.slamcore.utils.Messager;
import me.matzhilven.slamcore.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WithdrawMenu extends Menu {

    public WithdrawMenu(Player p) {
        super(p);
    }

    @Override
    public String getMenuName() {
        return config.getString("gembank.withdraw-menu.name");
    }

    @Override
    public int getSlots() {
        return config.getInt("gembank.withdraw-menu.slots");
    }

    @Override
    public void handleClick(InventoryClickEvent e) {

        User user = main.getDatabaseHandler().getUserByPlayer(p);
        long bankGems = user.getGems();

        int toWithdraw = 1;

        if (e.getSlot() == config.getInt("gembank.withdraw-menu.10.slot")) {
            toWithdraw = 10;
        } else if (e.getSlot() == config.getInt("gembank.withdraw-menu.all.slot")) {
            toWithdraw = (int) bankGems;
        } else if (e.getSlot() == config.getInt("gembank.withdraw-menu.back.slot")) {
            GemBankMenu gemBankMenu = new GemBankMenu(p);
            gemBankMenu.open();
            return;
        }

        System.out.println(toWithdraw);


        if (bankGems - toWithdraw < 0) {
            StringUtils.sendMessage(p, Messager.INVALID_WITHDRAW.replace("%gems%", StringUtils.format(toWithdraw)));
            return;
        }

        if (toWithdraw > getFreeSlots()) {
            StringUtils.sendMessage(p, Messager.NOT_ENOUGH_SPACE.replace("%gems%", StringUtils.format(toWithdraw)));
            return;
        }

        user.setGems((bankGems - toWithdraw));

        ItemStack gem = getGem();
        gem.setAmount(toWithdraw);

        p.getInventory().addItem(gem);

        StringUtils.sendMessage(p, Messager.WITHDRAW_GEMS
                .replace("%gems%", StringUtils.format(toWithdraw))
                .replace("%balance%", StringUtils.format(user.getGems())));

        main.getDatabaseHandler().saveUser(user);
    }

    private int getFreeSlots() {
        int slots = 0;

        String name = StringUtils.removeColor(config.getString("enchants.gemchance.gem.name"));

        for (ItemStack item : p.getInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR) {
                slots += 64;
                continue;
            }
            if (item.hasItemMeta()) {
                if (item.getItemMeta().hasDisplayName()) {
                    if (StringUtils.decolorize(item.getItemMeta().getDisplayName()).equals(name))
                        slots += (64 - item.getAmount());
                }
            }
        }

        return slots;
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(config.getInt("gembank.withdraw-menu.1.slot"),
                new ItemBuilder(Material.matchMaterial(config.getString("gembank.withdraw-menu.1.material")),
                        config.getInt("gembank.withdraw-menu.1.amount"))
                        .setName(config.getString("gembank.withdraw-menu.1.name"))
                        .setLore(config.getStringList("gembank.withdraw-menu.1.lore"))
                        .toItemStack()
        );

        inventory.setItem(config.getInt("gembank.withdraw-menu.10.slot"),
                new ItemBuilder(Material.matchMaterial(config.getString("gembank.withdraw-menu.10.material")),
                        config.getInt("gembank.withdraw-menu.10.amount"))
                        .setName(config.getString("gembank.withdraw-menu.10.name"))
                        .setLore(config.getStringList("gembank.withdraw-menu.10.lore"))
                        .toItemStack()
        );

        inventory.setItem(config.getInt("gembank.withdraw-menu.all.slot"),
                new ItemBuilder(Material.matchMaterial(config.getString("gembank.withdraw-menu.all.material")),
                        config.getInt("gembank.withdraw-menu.all.amount"))
                        .setName(config.getString("gembank.withdraw-menu.all.name"))
                        .setLore(config.getStringList("gembank.withdraw-menu.all.lore"))
                        .toItemStack()
        );

        inventory.setItem(config.getInt("gembank.withdraw-menu.back.slot"),
                new ItemBuilder(Material.matchMaterial(config.getString("gembank.withdraw-menu.back.material")))
                        .setName(config.getString("gembank.withdraw-menu.back.name"))
                        .setLore(config.getStringList("gembank.withdraw-menu.back.lore"))
                        .toItemStack()
        );

        setFillerGlass(new ItemBuilder(Material.matchMaterial(config.getString("gembank.filler.material")))
                .setName(config.getString("pickaxe-menu.filler.name"))
                .setLore(config.getStringList("pickaxe-menu.filler.lore"))
                .toItemStack());
    }
}
