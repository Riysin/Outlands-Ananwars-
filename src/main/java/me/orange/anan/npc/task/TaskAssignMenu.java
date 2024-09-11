package me.orange.anan.npc.task;

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

import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class TaskAssignMenu {
    private final GuiFactory guiFactory;

    public TaskAssignMenu(GuiFactory guiFactory) {
        this.guiFactory = guiFactory;
    }

    public void open(Player player, String npcName) {
        Gui gui = guiFactory.create(Component.text("Task"));
        NormalPane pane = Pane.normal(PaneMapping.rectangle(1, 1, 7, 3));
        NormalPane outline = Pane.normal(PaneMapping.outline(9, 5));

        pane.setSlot(1,1, GuiSlot.of(ItemBuilder.of(XMaterial.GREEN_STAINED_GLASS_PANE)
                .name(Component.text("&aAccept Task"))
                .lore("&elick to accept the task.")
                .build()));

        pane.setSlot(3, 1, GuiSlot.of(ItemBuilder.of(XMaterial.BOOK)
                .name(Component.text("&fTask Info"))
                .lore(getTaskInfo(npcName))
                .build()));

        pane.setSlot(5, 1, GuiSlot.of(ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE)
                .name(Component.text("&cReject Task"))
                .lore("&eClick to reject task.")
                .build(), clicker -> {
            player.closeInventory();
        }));

        outline.fillEmptySlots(GuiSlot.of(ItemBuilder.of(XMaterial.GRAY_STAINED_GLASS_PANE).build()));

        gui.addPane(pane);
        gui.addPane(outline);
        gui.open(player);
    }

    private List<String> getTaskInfo(String npcName) {
        // Get task info
        List<String> taskInfo = new ArrayList<>();
        taskInfo.add("Task Info");
        taskInfo.add("Task: ");
        taskInfo.add("Reward: ");
        taskInfo.add("Time: ");
        return taskInfo;
    }
}
