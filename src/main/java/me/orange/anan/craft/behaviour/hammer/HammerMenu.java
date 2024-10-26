package me.orange.anan.craft.behaviour.hammer;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.Craft;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.util.ItemLoreBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
        NormalPane pane = Pane.normal(9, 3);

        pane.setSlot(2, 1, GuiSlot.of(ItemBuilder.of(craftManager.getItemStack("buildLv2", player))
                .name("§6BuildLv2")
                .clearLore()
                .lore(getLore(2, player)).build(), player1 -> {
            hammerManager.setHammerStat(player, HammerAction.UPGRADELv2);
            player1.playSound(player1.getLocation(), Sound.CLICK, 1, 1);
            player1.closeInventory();
        }));
        pane.setSlot(3, 1, GuiSlot.of(ItemBuilder.of(craftManager.getItemStack("buildLv3", player))
                .name("§6BuildLv3")
                .clearLore()
                .lore(getLore(3, player)).build(), player1 -> {
            player1.playSound(player1.getLocation(), Sound.CLICK, 1, 1);
            hammerManager.setHammerStat(player, HammerAction.UPGRADELv3);
            player1.closeInventory();
        }));
        pane.setSlot(4, 1, GuiSlot.of(ItemBuilder.of(craftManager.getItemStack("buildLv4", player))
                .name("§6BuildLv4")
                .clearLore()
                .lore(getLore(4, player)).build(), player1 -> {
            player1.playSound(player1.getLocation(), Sound.CLICK, 1, 1);
            hammerManager.setHammerStat(player, HammerAction.UPGRADELv4);
            player1.closeInventory();
        }));
        pane.setSlot(5, 1, GuiSlot.of(ItemBuilder.of(craftManager.getItemStack("buildLv5", player))
                .name("§6BuildLv5")
                .clearLore()
                .lore(getLore(5, player)).build(), player1 -> {
            player1.playSound(player1.getLocation(), Sound.CLICK, 1, 1);
            hammerManager.setHammerStat(player, HammerAction.UPGRADELv5);
            player1.closeInventory();
        }));
        pane.setSlot(6, 1, GuiSlot.of(ItemBuilder.of(XMaterial.BARRIER)
                .name("§c拆除")
                .clearLore()
                .lore("§e可拆除1分鐘內建造的方塊").build(), player1 -> {
            player1.playSound(player1.getLocation(), Sound.CLICK, 1, 1);
            hammerManager.setHammerStat(player, HammerAction.BREAK);
            player1.closeInventory();
        }));

        gui.addPane(pane);
        gui.open(player);
    }

    private List<String> getLore(int level, Player player) {
        Craft craft = craftManager.getCrafts().get("buildLv" + level);

        return ItemLoreBuilder.of(craft.getItemStack())
                .setCraft(craftManager, craft)
                .craftType()
                .description()
                .upgrade(player)
                .build();
    }
}
