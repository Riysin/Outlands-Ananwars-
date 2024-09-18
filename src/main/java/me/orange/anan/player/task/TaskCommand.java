package me.orange.anan.player.task;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.entity.Player;

@InjectableComponent
@Command(value = "task", permissionNode = "task.admin")
public class TaskCommand extends BaseCommand {
    private final TaskManager taskManager;

    public TaskCommand(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Command("assign")
    public void assignTask(BukkitCommandContext ctx, @Arg("player") Player player, @Arg("task") String task) {
        taskManager.addTask(player, task);
        ctx.getPlayer().sendMessage("Assigned task to " + player.getName());
    }

    @Command("info")
    public void taskInfo(BukkitCommandContext ctx, @Arg("task") String task) {
        ctx.getPlayer().sendMessage(taskManager.getTaskInfo(task).toArray(new String[0]));
    }

    @Command("list")
    public void listTasks(BukkitCommandContext ctx, @Arg("player") Player player) {
        ctx.getPlayer().sendMessage(taskManager.getPlayerTasks(player).toString());
    }

    @Command("progress")
    public void setCurrentProgress(BukkitCommandContext ctx, @Arg("player") Player player, @Arg("task") String task, @Arg("progress") int progress) {
        taskManager.getPlayerTask(player, task).setProgress(progress);
        ctx.getPlayer().sendMessage("Set progress for " + player.getName() + "'s task to " + progress);
    }

    @Command("remove")
    public void removeTask(BukkitCommandContext ctx, @Arg("player") Player player, @Arg("task") String task) {
        taskManager.removeTask(player, task);
        ctx.getPlayer().sendMessage("Removed task from " + player.getName());
    }
}
