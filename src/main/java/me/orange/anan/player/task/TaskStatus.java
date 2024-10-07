package me.orange.anan.player.task;

public enum TaskStatus {
    UNASSIGNED("§cUnassigned"),
    ASSIGNED("§fAssigned"),
    COMPLETED("§eCompleted"),
    CLAIMED("§aRewarded");

    private String status;

    TaskStatus(String status) {
        this.status = status;
    }

    public String getString() {
        return this.status;
    }
}
