package me.orange.anan.npc;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.pane.mapping.PaneMapping;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@InjectableComponent
public class NPCTaskMenu {
    private final GuiFactory guiFactory;

    public NPCTaskMenu(GuiFactory guiFactory) {
        this.guiFactory = guiFactory;
    }

    public void open(Player player) {
        Gui gui = guiFactory.create(Component.text("Task"));
        NormalPane pane = Pane.normal(PaneMapping.rectangle(1, 1, 7, 3));
        NormalPane outline = Pane.normal(PaneMapping.outline(9, 5));

        pane.setSlot(3, 1, GuiSlot.of(ItemBuilder.of(XMaterial.BOOK)
                .name(Component.text("Task"))
                .lore("Click to accept task.")
                .build()));

        outline.fillEmptySlots(GuiSlot.of(ItemBuilder.of(XMaterial.GRAY_STAINED_GLASS_PANE).build()));

        gui.addPane(pane);
        gui.addPane(outline);
        gui.open(player);
    }
}
