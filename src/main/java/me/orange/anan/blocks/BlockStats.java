package me.orange.anan.blocks;

public class BlockStats {
    private BlockType blockType = BlockType.NATURE;
    private int health;
    private boolean breakable;

    public BlockStats(int health) {
        this.health = health;
        this.breakable =false;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public void setBlockType(BlockType blockType) {
        this.blockType = blockType;
    }

    public boolean isBreakable() {
        return breakable;
    }

    public void setBreakable(boolean breakable) {
        this.breakable = breakable;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}
