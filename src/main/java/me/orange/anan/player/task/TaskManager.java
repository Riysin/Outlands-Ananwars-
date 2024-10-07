package me.orange.anan.player.task;

import io.fairyproject.container.InjectableComponent;
import me.orange.anan.player.PlayerDataManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class TaskManager {
    private final PlayerDataManager playerDataManager;

    public TaskManager(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    public List<Task> getPlayerTasks(Player player) {
        return playerDataManager.getPlayerData(player).getTasks();
    }

    public Task getPlayerTask(Player player, String taskID) {
        return playerDataManager.getPlayerData(player).getTasks().stream()
                .filter(task -> task.getId().equals(taskID))
                .findFirst()
                .orElse(null);
    }

    public boolean hasTaskAssigned(Task task) {
        return task != null && task.getStatus() == TaskStatus.ASSIGNED;
    }

    public void addTask(Player player, String jobID) {
        Task task = TaskRegistry.create(jobID);
        task.setStatus(TaskStatus.ASSIGNED);
        getPlayerTasks(player).add(task);
    }

    public void removeTask(Player player, String jobID) {
        getPlayerTasks(player).remove(getPlayerTask(player, jobID));
    }

    public List<String> getTaskInfo(Player player, String taskID) {
        // Get task info
        Task task = TaskRegistry.create(taskID);
        if (getPlayerTask(player, taskID) != null) {
            task = getPlayerTask(player, taskID);
        }
        List<String> taskInfo = new ArrayList<>();
        taskInfo.add("§6" + task.getName() + ":");
        taskInfo.add("§7" + task.getDescription());
        taskInfo.add("");
        taskInfo.add("§6Progress: §f" + task.getProgress());
        taskInfo.add("§6Status: " + task.getStatus().getString());
        taskInfo.add("");
        taskInfo.add("§6Reward: §a" + task.getReward());
        return taskInfo;
    }
}
