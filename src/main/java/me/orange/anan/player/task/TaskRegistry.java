package me.orange.anan.player.task;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.log.Log;
import me.orange.anan.player.task.tasks.FisherTask;

import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class TaskRegistry {
    private static List<Task> tasks = new ArrayList<>();

    public TaskRegistry() {
        registerTask(new FisherTask());
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void registerTask(Task task) {
        tasks.add(task);
    }

    public static Task create(String id) {
        Task taskInst = tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (taskInst == null) {
            Log.info("Task with id " + id + " not found");
        }

        return taskInst.cloneTask();
    }
}
