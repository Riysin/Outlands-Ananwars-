package me.orange.anan.player.task;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.events.TaskCompleteEvent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@InjectableComponent
@RegisterAsListener
public class TaskEventListener implements Listener {
    @EventHandler
    public void onTaskComplete(TaskCompleteEvent event) {
        Task task = event.getTask();
        Player player = event.getPlayer();

        task.setStatus(TaskStatus.COMPLETED);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
        player.sendMessage("§eYou completed the task: " + task.getName());
    }
}
