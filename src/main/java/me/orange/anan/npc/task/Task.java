package me.orange.anan.npc.task;

public abstract class Task {
    protected String id;
    protected String name;
    protected String description;
    protected int goal;
    protected int progress;
    protected TaskStatus status;

    public Task(String id, String name, String description, int goal) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.goal = goal;
        this.progress = 0;
        this.status = TaskStatus.UNASSIGNED;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getGoal() {
        return goal;
    }

    public int getProgress() {
        return progress;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void startTask() {
        if (status == TaskStatus.UNASSIGNED) {
            status = TaskStatus.ASSIGNED;
            System.out.println("Task started: " + name);
        }
    }

    protected void updateProgress(int amount) {
        if (status == TaskStatus.ASSIGNED) {
            progress += amount;
            if (progress >= goal) {
                progress = goal;
                status = TaskStatus.COMPLETED;
                System.out.println("Task completed: " + name);
            }
        }
    }

    // Abstract method to be implemented by subclasses
    public abstract void onTaskEvent();
}
