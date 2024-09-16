package me.orange.anan.npc.task;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.events.TaskCompleteEvent;
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
        task.setStatus(TaskStatus.COMPLETED);
        event.getPlayer().sendMessage("You completed the task: " + task.getName());
    }

    @EventHandler
    public void fish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        Task playerTask = taskManager.getPlayerTask(player, "fisher");

        if (!taskManager.hasTaskAssigned(playerTask)) {
            return;
        }

        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            ItemStack fish = ((Item) event.getCaught()).getItemStack();
            playerTask.onProgress(player, fish);
        }

    }
}
