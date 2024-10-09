package me.orange.anan.player.task.menu;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.player.task.Task;
import me.orange.anan.player.task.TaskManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@InjectableComponent
public class TaskInfoMenu {
    private final GuiFactory guiFactory;
    private final TaskManager taskManager;

    public TaskInfoMenu(GuiFactory guiFactory, TaskManager taskManager) {
        this.guiFactory = guiFactory;
        this.taskManager = taskManager;
    }

    public void open(Player player, Task task) {
        Gui gui = guiFactory.create(Component.text("Task Info"));
        NormalPane pane = Pane.normal(9, 3);

        pane.setSlot(4, 1, GuiSlot.of(ItemBuilder.of(XMaterial.BOOK)
                .name(Component.text("§eTask Info"))
                .lore(taskManager.getTaskInfo(player, task.getId()))
                .build()));

        pane.fillEmptySlots(GuiSlot.of(ItemBuilder.of(XMaterial.GRAY_STAINED_GLASS_PANE).build()));
        gui.addPane(pane);
        gui.open(player);
    }
}
