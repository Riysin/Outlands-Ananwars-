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
        Gui gui = guiFactory.create(Component.text("Admin Wand Menu"));
        NormalPane pane = Pane.normal(9,6);

        pane.setSlot(0, GuiSlot.of(ItemBuilder.of(XMaterial.NOTE_BLOCK)
                .name("Place loot")
                .build(),clicker->{
            adminWandManager.setAction(player, AdminWandAction.LOOT);
            clicker.closeInventory();
        }));
        pane.setSlot(1, GuiSlot.of(ItemBuilder.of(XMaterial.VILLAGER_SPAWN_EGG)
                .name("Place NPC")
                .build(),clicker->{
            adminWandManager.setAction(player, AdminWandAction.NPC_FISHER);
            clicker.closeInventory();
        }));

        gui.addPane(pane);
        gui.open(player);
    }
}
