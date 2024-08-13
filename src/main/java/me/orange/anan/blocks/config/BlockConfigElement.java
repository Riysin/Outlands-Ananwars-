package me.orange.anan.blocks.config;

import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.mc.util.Position;
import me.orange.anan.blocks.BlockType;
import org.bukkit.Location;

@ConfigurationElement
public class BlockConfigElement {
    private int health = 0;
    private Position position = new Position();
    private BlockType blockType = BlockType.NATURE;

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Location getLocation() {
        return BukkitPos.toBukkitLocation(position);
    }

    public void setPosition(Location location) {
        this.position = BukkitPos.toMCPos(location);
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public void setBlockType(BlockType blockType) {
        this.blockType = blockType;
    }
}
