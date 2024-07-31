package me.orange.anan.craft;

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


        gui.onDrawCallback(updatePlayer -> {
            int i = 0;
            for (CraftTimer craftTimer : craftTimerManager.getPlayerCraftTimerList(player)) {
                List<String> loreLines = new ArrayList<>(craftTimer.getCraft().getLore());
                loreLines.add("§e還剩 " + craftTimer.getTime() + "s");
                loreLines.add("§cclick to stop");

                pane.setSlot(i, GuiSlot.of(ItemBuilder.of(craftTimer.getCraft().getItemStack())
                        .name(craftTimer.getCraft().getName())
                        .lore(loreLines)
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
