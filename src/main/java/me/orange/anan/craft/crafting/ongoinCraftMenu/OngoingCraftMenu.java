package me.orange.anan.craft.crafting.ongoinCraftMenu;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.Craft;
import me.orange.anan.craft.crafting.CraftTimer;
import me.orange.anan.craft.crafting.CraftTimerManager;
import me.orange.anan.events.CraftTimerCountDownEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class OngoingCraftMenu {
    private final GuiFactory guiFactory;
    private final CraftTimerManager craftTimerManager;

    public OngoingCraftMenu(GuiFactory guiFactory, CraftTimerManager craftTimerManager) {
        this.guiFactory = guiFactory;
        this.craftTimerManager = craftTimerManager;
    }

    public void open(Player player) {
        Gui gui = guiFactory.create(Component.text("§f§l製作中的物品"));
        NormalPane pane = Pane.normal(9, 3);

        gui.onOpenCallback($ -> {
            gui.getEventNode().addListener(CraftTimerCountDownEvent.class, event -> {
                if (event.getPlayer() == player)
                    gui.update(player);
            });
        });

        gui.onDrawCallback($ -> {
            pane.clear();

            int i = 0;
            for (CraftTimer craftTimer : craftTimerManager.getPlayerCraftTimerList(player)) {
                Craft craft = craftTimer.getCraft();
                List<String> loreLines = new ArrayList<>();
                loreLines.addAll(craft.getLore());
                loreLines.add("§e還剩 " + (craftTimer.getTime() - 1) + "s");
                loreLines.add("§cclick to stop");

                pane.setSlot(i, GuiSlot.of(ItemBuilder.of(craft.getMenuIcon())
                        .clearLore()
                        .name(craft.getName())
                        .lore(loreLines)
                        .amount(craftTimer.getAmount())
                        .build(), clicker -> {
                    craftTimerManager.craftingFailed(clicker, craftTimer);
                    gui.update(clicker);
                }));
                i++;
            }
        });

        pane.fillEmptySlots(GuiSlot.of(XMaterial.GRAY_STAINED_GLASS_PANE));

        gui.addPane(pane);
        gui.open(player);
    }
}
