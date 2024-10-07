package me.orange.anan.player.task.menu;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.pane.mapping.PaneMapping;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.player.task.TaskManager;
import me.orange.anan.player.task.TaskStatus;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicInteger;

@InjectableComponent
public class AssignedTaskMenu {
    private final GuiFactory guiFactory;
    private final TaskManager taskManager;

    public AssignedTaskMenu(GuiFactory guiFactory, TaskManager taskManager) {
        this.guiFactory = guiFactory;
        this.taskManager = taskManager;
    }

    public void open(Player player) {
        Gui gui = guiFactory.create(Component.text("Assigned Task"));
        NormalPane pane = Pane.normal(PaneMapping.rectangle(1, 1, 7, 4));
        NormalPane outline = Pane.normal(PaneMapping.outline(9, 6));

        AtomicInteger slot = new AtomicInteger();
        taskManager.getPlayerTasks(player).forEach(task -> {
            if (task.getStatus() != TaskStatus.ASSIGNED) return;
            pane.setSlot(slot.get(), GuiSlot.of(ItemBuilder.of(XMaterial.BOOK)
                    .name(Component.text("§eTask Info"))
                    .lore(taskManager.getTaskInfo(player, task.getId()))
                    .build()));
            slot.getAndIncrement();
        });

        outline.fillEmptySlots(GuiSlot.of(ItemBuilder.of(XMaterial.GRAY_STAINED_GLASS_PANE).build()));
        outline.setSlot(4, 5, GuiSlot.of(ItemBuilder.of(XMaterial.OAK_DOOR).name("§cBack").build(), ctx -> {
            Bukkit.dispatchCommand(player, "player menu " + player.getName());
        }));

        gui.addPane(outline);
        gui.addPane(pane);
        gui.open(player);
    }
}
