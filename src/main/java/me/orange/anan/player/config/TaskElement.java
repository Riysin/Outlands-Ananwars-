package me.orange.anan.player.config;

import io.fairyproject.config.annotation.ConfigurationElement;
import me.orange.anan.player.task.TaskStatus;

@ConfigurationElement
public class TaskElement {
    private TaskStatus status = TaskStatus.UNASSIGNED;
    private int progress = 0;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
