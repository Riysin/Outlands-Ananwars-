package me.orange.anan.npc.task;

import io.fairyproject.container.InjectableComponent;
import me.orange.anan.npc.task.tasks.FisherTask;

import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class TaskRegistry {
    private List<Task> tasks = new ArrayList<>();

    public TaskRegistry() {
        registerTask(new FisherTask());
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void registerTask(Task task) {
        tasks.add(task);
    }
}
