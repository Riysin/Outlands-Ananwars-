package me.orange.anan.blocks;

import org.bukkit.Location;

public class BlockStats {
    private BlockType blockType = BlockType.NATURE;
    private int health;
    private Location location;
    private boolean justPlaced = true;
    private boolean isGettingDestroyed = false;

    public BlockType getBlockType() {
        return blockType;
    }

    public void setBlockType(BlockType blockType) {
        this.blockType = blockType;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isJustPlaced() {
        return justPlaced;
    }

    public void setJustPlaced(boolean justPlaced) {
        this.justPlaced = justPlaced;
    }

    public boolean isGettingDestroyed() {
        return isGettingDestroyed;
    }

    public void setGettingDestroyed(boolean gettingDestroyed) {
        isGettingDestroyed = gettingDestroyed;
    }
}
