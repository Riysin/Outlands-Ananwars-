package me.orange.anan.player.bed;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.Anan;
import net.kyori.adventure.text.Component;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.Collections;

@InjectableComponent
public class BedMenu implements Listener {
    private final GuiFactory guiFactory;

    public BedMenu(GuiFactory guiFactory) {
        this.guiFactory = guiFactory;
    }

    public void open(Player player, Bed bed) {
        Gui gui = guiFactory.create(Component.text("§f§lBed Menu"));
        NormalPane pane = Pane.normal(9, 3);

        pane.setSlot(4, 1, GuiSlot.of(ItemBuilder.of(XMaterial.RED_BED)
                .name(bed.getBedName())
                .lore("§eClick to rename bed")
                .build(), clicker -> {
            openAnvilGUI(player, bed);
        }));

        gui.addPane(pane);
        gui.open(player);
    }

    public void openAnvilGUI(Player player, Bed bed) {
        AnvilGUI.Builder builder = new AnvilGUI.Builder();
        builder.onClick((slot, stateSnapshot) -> {
                    if(slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    bed.setBedName(stateSnapshot.getText());
                    stateSnapshot.getPlayer().sendMessage("§aBed renamed to " + stateSnapshot.getText());
                    return Arrays.asList(AnvilGUI.ResponseAction.close());
                })
                .text("Name")
                .preventClose()
                .plugin(BukkitPlugin.INSTANCE)
                .open(player);

    }
}
