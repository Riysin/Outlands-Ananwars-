package me.orange.anan.npc.task;

import io.fairyproject.container.InjectableComponent;
import me.orange.anan.player.PlayerDataManager;
import org.bukkit.entity.Player;

import java.util.List;

@InjectableComponent
public class TaskManager {
    private final PlayerDataManager playerDataManager;
    private final TaskAssignMenu taskAssignMenu;
    private final TaskRewardMenu taskRewardMenu;

    public TaskManager(PlayerDataManager playerDataManager, TaskAssignMenu taskAssignMenu, TaskRewardMenu taskRewardMenu) {
        this.playerDataManager = playerDataManager;
        this.taskAssignMenu = taskAssignMenu;
        this.taskRewardMenu = taskRewardMenu;
    }

    public void openTaskNpcMenu(Player player, String jobID) {
        Task task = getTask(player, jobID);
        if (task.getStatus() == TaskStatus.UNASSIGNED) {
            taskAssignMenu.open(player, jobID);
        } else if (task.getStatus() == TaskStatus.ASSIGNED) {
            player.sendMessage("Task already assigned.");
        } else {
            taskRewardMenu.open(player, jobID);
        }
    }

    public Task getTask(Player player, String jobID) {
        return playerDataManager.getPlayerData(player).getTasks().stream()
                .filter(task -> task.getID().equals(jobID))
                .findFirst()
                .orElse(null);
    }

    public List<Task> getTasks(Player player) {
        return playerDataManager.getPlayerData(player).getTasks();
    }

    public void addTask(Player player, String jobID) {
        getTasks(player).add(getTask(player, jobID));
    }

    public void removeTask(Player player, String jobID) {
        getTasks(player).remove(getTask(player, jobID));
    }
}
