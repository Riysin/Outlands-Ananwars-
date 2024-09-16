package me.orange.anan.npc.task;

import io.fairyproject.container.InjectableComponent;
import me.orange.anan.player.PlayerDataManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class TaskManager {
    private final PlayerDataManager playerDataManager;
    private final TaskRegistry taskRegistry;

    public TaskManager(PlayerDataManager playerDataManager, TaskRegistry taskRegistry) {
        this.playerDataManager = playerDataManager;
        this.taskRegistry = taskRegistry;
    }

    public Task getPlayerTask(Player player, String taskID) {
        return playerDataManager.getPlayerData(player).getTasks().stream()
                .filter(task -> task.getId().equals(taskID))
                .findFirst()
                .orElse(null);
    }

    public List<Task> getPlayerTasks(Player player) {
        return playerDataManager.getPlayerData(player).getTasks();
    }

    public Task getTask(String id) {
        return taskRegistry.getTasks().stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean hasTaskAssigned(Task task) {
        return task != null && task.getStatus() == TaskStatus.ASSIGNED;
    }

    public void addTask(Player player, String jobID) {
        Task task = getTask(jobID);
        task.setStatus(TaskStatus.ASSIGNED);
        getPlayerTasks(player).add(task);
    }

    public void removeTask(Player player, String jobID) {
        getPlayerTasks(player).remove(getPlayerTask(player, jobID));
    }

    public List<String> getTaskInfo(String taskID) {
        // Get task info
        Task task = getTask(taskID);
        List<String> taskInfo = new ArrayList<>();
        taskInfo.add("§eTask:§f " + task.getName());
        taskInfo.add("§eDescription: §f" + task.getDescription());
        taskInfo.add("§eReward: §f" + task.getReward());
        return taskInfo;
    }
}
