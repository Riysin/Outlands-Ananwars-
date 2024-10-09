package me.orange.anan.player.task;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.events.TaskCompleteEvent;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

@InjectableComponent
@RegisterAsListener
public class TaskEventListener implements Listener {
    private final TaskManager taskManager;

    public TaskEventListener(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @EventHandler
    public void onTaskComplete(TaskCompleteEvent event) {
        Task task = event.getTask();
        Player player = event.getPlayer();

        task.setStatus(TaskStatus.COMPLETED);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
        player.sendMessage("§eYou completed the task: " + task.getName());
    }

    @EventHandler
    public void fish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        Task playerTask = taskManager.getPlayerTask(player, "task.fisher");

        if (!taskManager.hasTaskAssigned(playerTask)) {
            return;
        }

        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            ItemStack fish = ((Item) event.getCaught()).getItemStack();
            playerTask.onProgress(player, fish);
        }

    }
}
