package me.orange.anan.player.task;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.player.job.JobManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class TaskRewardMenu {
    private final GuiFactory guiFactory;
    private final TaskManager taskManager;
    private final JobManager jobManager;

    public TaskRewardMenu(GuiFactory guiFactory, TaskManager taskManager, JobManager jobManager) {
        this.guiFactory = guiFactory;
        this.taskManager = taskManager;
        this.jobManager = jobManager;
    }

    public void open(Player player, Task task) {
        Gui gui = guiFactory.create(Component.text("Task"));
        NormalPane pane = Pane.normal(9, 3);

        pane.setSlot(4, 1, GuiSlot.of(ItemBuilder.of(XMaterial.GOLD_BLOCK)
                .name(Component.text("§6Reward"))
                .lore(getRewardInfo(task))
                .build(), clicker -> {
            player.sendMessage("§aReward claimed.");
            player.closeInventory();
            task.setStatus(TaskStatus.CLAIMED);
            jobManager.addPlayer(player, jobManager.getJobByID(task.getId()));
        }));

        pane.fillEmptySlots(GuiSlot.of(ItemBuilder.of(XMaterial.GRAY_STAINED_GLASS_PANE).build()));

        gui.addPane(pane);
        gui.open(player);
    }

    private List<String> getRewardInfo(Task task) {
        // Get reward info
        List<String> rewardInfo = new ArrayList<>();
        rewardInfo.add("§eReward:");
        rewardInfo.add("§a" + task.getReward());
        rewardInfo.add("§fReward Info:");
        rewardInfo.add("§a" + task.getRewardDescription());
        rewardInfo.add("");
        rewardInfo.add("&eClick to claim reward.");
        return rewardInfo;
    }
}
