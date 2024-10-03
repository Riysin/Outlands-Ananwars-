package me.orange.anan.craft.behaviour.adminWand;

public enum AdminWandAction {
    NONE(""),
    NPC_FISHER("anpc merchant fisher"),
    LOOT("anpc loot");

    private String command;

    // Constructor
    AdminWandAction(String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }
}
