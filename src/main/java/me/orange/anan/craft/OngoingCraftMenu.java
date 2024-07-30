package me.orange.anan.craft;

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
public class OngoingCraftMenu {
    private final GuiFactory guiFactory;
    private final CraftTimerManager craftTimerManager;

    public OngoingCraftMenu(GuiFactory guiFactory, CraftTimerManager craftTimerManager) {
        this.guiFactory = guiFactory;
        this.craftTimerManager = craftTimerManager;
    }

    public void open(Player player) {
        Gui gui = guiFactory.create(Component.text("§f正在製作中的物品"));
        NormalPane pane = Pane.normal(9, 3);

        int i = 0;
        for (CraftTimer craftTimer : craftTimerManager.getPlayerCraftTimerList(player)) {
            pane.setSlot(i, GuiSlot.of(ItemBuilder.of(craftTimer.getCraft().getItemStack())
                    .name(craftTimer.getCraft().getName())
                    .lore("click to stop")
                    .build(), clicker -> {
                craftTimerManager.craftingFailed(clicker,craftTimer);
                gui.update(clicker);
            }));
        }

        gui.addPane(pane);
        gui.open(player);
    }
}
