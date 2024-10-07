package me.orange.anan.craft.behaviour.adminWand;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@InjectableComponent
public class AdminWandMenu {
    private final GuiFactory guiFactory;
    private final AdminWandManager adminWandManager;

    public AdminWandMenu(GuiFactory guiFactory, AdminWandManager adminWandManager) {
        this.guiFactory = guiFactory;
        this.adminWandManager = adminWandManager;
    }

    public void open(Player player) {
        Gui gui = guiFactory.create(Component.text("§fAdmin Wand Menu"));
        NormalPane pane = Pane.normal(9, 6);

        int i = 0;

        for (AdminWandAction action : AdminWandAction.values()) {
            pane.setSlot(i, GuiSlot.of(ItemBuilder.of(getMaterial(action))
                    .name("§e" + action.toString())
                    .lore("§6Command:", "§7/"+action.getCommand())
                    .build(), event -> {
                adminWandManager.setAction(player, action);
                player.closeInventory();
            }));
            i++;
        }


        gui.addPane(pane);
        gui.open(player);
    }

    private XMaterial getMaterial(AdminWandAction action) {
        if (action.toString().contains("LOOT")) {
            return XMaterial.CHEST;
        } else if (action.toString().contains("MERCHANT")) {
            return XMaterial.EMERALD;
        } else if (action.toString().contains("TASKNPC")) {
            return XMaterial.BOOK;
        } else if (action.toString().contains("SAFEZONE")) {
            return XMaterial.OAK_LOG;
        } else {
            return XMaterial.BARRIER;
        }
    }
}
