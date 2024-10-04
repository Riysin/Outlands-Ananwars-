package me.orange.anan.craft.behaviour.adminWand;

public enum AdminWandAction {
    NONE(""),
    LOOT("anpc loot"),
    NPC_FISHER("anpc merchant fisher"),
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
