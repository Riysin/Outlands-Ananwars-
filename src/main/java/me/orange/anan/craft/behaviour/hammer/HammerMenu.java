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
import me.orange.anan.craft.config.CraftElement;
import net.kyori.adventure.text.Component;
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

        pane.setSlot(2, 1, GuiSlot.of(ItemBuilder.of(craftManager.getCraftElementWithID("buildLv2").getIcon())
                .name("§6BuildLv2")
                .lore(getLore(2,player)).build(), player1 -> {
            hammerManager.setHammerStat(player, HammerAction.UPGRADELv2);
            player1.closeInventory();
        }));
        pane.setSlot(3, 1, GuiSlot.of(ItemBuilder.of(craftManager.getCraftElementWithID("buildLv3").getIcon())
                .name("§6BuildLv3")
                .lore(getLore(3,player)).build(), player1 -> {
            hammerManager.setHammerStat(player, HammerAction.UPGRADELv3);
            player1.closeInventory();
        }));
        pane.setSlot(4, 1, GuiSlot.of(ItemBuilder.of(craftManager.getCraftElementWithID("buildLv4").getIcon())
                .name("§6BuildLv4")
                .lore(getLore(4,player)).build(), player1 -> {
            hammerManager.setHammerStat(player, HammerAction.UPGRADELv4);
            player1.closeInventory();
        }));
        pane.setSlot(5, 1, GuiSlot.of(ItemBuilder.of(craftManager.getCraftElementWithID("buildLv5").getIcon())
                .name("§6BuildLv5")
                .lore(getLore(5,player)).build(), player1 -> {
            hammerManager.setHammerStat(player, HammerAction.UPGRADELv5);
            player1.closeInventory();
        }));
        pane.setSlot(6, 1, GuiSlot.of(ItemBuilder.of(XMaterial.BARRIER)
                .name("§c拆除")
                .lore("§e可拆除1分鐘內建造的方塊").build(), player1 -> {
            hammerManager.setHammerStat(player, HammerAction.BREAK);
            player1.closeInventory();
        }));

        gui.addPane(pane);
        gui.open(player);
    }

    private List<String> getLore(int level, Player player) {
        CraftElement element = craftManager.getCraftElementWithID("buildLv" + level);
        List<String> loreLines = new ArrayList<>();

        loreLines.add("§8BUILD");
        loreLines.add("");
        loreLines.addAll(element.getLore());
        loreLines.add("");
        loreLines.add("§e消耗材料:");

        craftManager.getRecipesFromIDs(element.getRecipes(), player).forEach(itemStack -> {
            loreLines.add("  §7" + itemStack.getItemMeta().getDisplayName() + " (§a" + itemStack.getAmount() + "§7)");
        });

        loreLines.add("");
        loreLines.add("§e§l點擊選擇升級為此建材");
        return loreLines;
    }
}
