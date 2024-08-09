package me.orange.anan.craft.behaviour.hammer;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.CraftManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@InjectableComponent
public class HammerMenu {
    private final GuiFactory guiFactory;
    private final CraftManager craftManager;
    private final HammerManager hammerManager;

    public HammerMenu(GuiFactory guiFactory, CraftManager craftManager, HammerManager hammerManager) {
        this.guiFactory = guiFactory;
        this.craftManager = craftManager;
        this.hammerManager = hammerManager;
    }

    public void open(Player player) {
        Gui gui = guiFactory.create(Component.text("§f§l升級鎚子"));
        NormalPane pane = Pane.normal(9, 3);;

        pane.setSlot(2,1, GuiSlot.of(ItemBuilder.of(craftManager.getCraftElementWithID("buildLv2").getIcon())
                .lore("§7點擊選擇要升級的建材").build(),player1 -> {
            hammerManager.setHammerStat(player, HammerAction.UPGRADELv2);
            player1.closeInventory();
        }));
        pane.setSlot(3,1, GuiSlot.of(ItemBuilder.of(craftManager.getCraftElementWithID("buildLv3").getIcon())
                .lore("§7點擊選擇要升級的建材").build(),player1 -> {
            hammerManager.setHammerStat(player, HammerAction.UPGRADELv3);
            player1.closeInventory();
        }));
        pane.setSlot(4,1, GuiSlot.of(ItemBuilder.of(craftManager.getCraftElementWithID("buildLv4").getIcon())
                .lore("§7點擊選擇要升級的建材").build(),player1 -> {
            hammerManager.setHammerStat(player, HammerAction.UPGRADELv4);
            player1.closeInventory();
        }));
        pane.setSlot(5,1, GuiSlot.of(ItemBuilder.of(craftManager.getCraftElementWithID("buildLv5").getIcon())
                .lore("§7點擊選擇要升級的建材").build(),player1 -> {
            hammerManager.setHammerStat(player, HammerAction.UPGRADELv5);
            player1.closeInventory();
        }));
        pane.setSlot(6,1, GuiSlot.of(ItemBuilder.of(XMaterial.BARRIER)
                .lore("§c拆除方塊").build(),player1 -> {
            hammerManager.setHammerStat(player, HammerAction.BREAK);
            player1.closeInventory();
        }));

        gui.addPane(pane);
        gui.open(player);
    }
}
