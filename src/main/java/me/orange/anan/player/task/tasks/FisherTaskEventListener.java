package me.orange.anan.player.task.tasks;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.player.task.Task;
import me.orange.anan.player.task.TaskManager;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

@InjectableComponent
@RegisterAsListener
public class FisherTaskEventListener implements Listener {
    private final TaskManager taskManager;

    public FisherTaskEventListener(TaskManager taskManager) {
        this.taskManager = taskManager;
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
