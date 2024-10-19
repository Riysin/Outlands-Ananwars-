package me.orange.anan.craft.behaviour.adminWand;

public enum AdminWandAction {
    NONE(""),
    LOOT("anpc create loot"),
    MERCHANT_FISHER("anpc create merchant.fisher"),
    TASKNPC_FISHER("anpc create task.fisher"),
    SAFEZONE_FISHER("safezone paste fisher");

    private String command;

    // Constructor
    AdminWandAction(String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }
}
