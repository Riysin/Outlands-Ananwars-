package me.orange.anan.craft.behaviour.teamCore;

import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStats;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.blocks.BlockType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

@InjectableComponent
public class TeamCoreManager {
    private final TeamCoreConfig teamCoreConfig;
    private List<TeamCore> teamCores = new ArrayList<>();
    private final BlockStatsManager blockStatsManager;

    public TeamCoreManager(TeamCoreConfig teamCoreConfig, BlockStatsManager blockStatsManager) {
        this.teamCoreConfig = teamCoreConfig;
        this.blockStatsManager = blockStatsManager;

        loadConfig();
    }

    public void loadConfig() {
        for (TeamCoreConfigElement element : teamCoreConfig.getTeamCores()) {
            TeamCore teamCore = new TeamCore(element.getPlacePlayerUUID(), getClosestCreeper(element.getCoreCreeperLocation(), 1), element.getCoreBlockLocation().getBlock());
            teamCore.getCoreCreeper().setHealth(element.getCoreCreeperHealth());
            teamCores.add(teamCore);
            teamCore.setConnectedBlocks(element.getConnectedBlocks());
        }
    }

    public void saveConfig() {
        teamCoreConfig.getTeamCores().clear();
        for (TeamCore teamCore : teamCores) {
            TeamCoreConfigElement element = new TeamCoreConfigElement();
            element.setPlacePlayerUUID(teamCore.getPlacePlayer());
            element.setCoreCreeperHealth(teamCore.getCoreCreeper().getHealth());
            element.setCoreCreeperPosition(teamCore.getCoreCreeper().getLocation());
            element.setCoreBlockLocation(teamCore.getCoreBlock().getLocation());
            element.setConnectedBlocks(teamCore.getConnectedBlocks());
            teamCoreConfig.addTeamCore(element);
        }
    }

    private Creeper getClosestCreeper(Location location, double radius) {
        World world = location.getWorld();
        Creeper closestCreeper = null;
        double closestDistance = radius * radius;

        for (Entity entity : world.getNearbyEntities(location, radius, radius, radius)) {
            if (entity instanceof Creeper) {
                double distanceSquared = entity.getLocation().distanceSquared(location);
                if (distanceSquared < closestDistance) {
                    closestDistance = distanceSquared;
                    closestCreeper = (Creeper) entity;
                }
            }
        }
        return closestCreeper;
    }

    public List<TeamCore> getTeamCores() {
        return this.teamCores;
    }

    public void addTeamCore(Player player, Creeper creeper, Block block) {
        TeamCore teamCore = new TeamCore(player.getUniqueId(), creeper, block);
        teamCore.setConnectedBlocks(exploreConnectedBlocks(block));
        teamCores.add(teamCore);
    }

    public void removeTeamCore(TeamCore teamCore) {
        teamCores.remove(teamCore);
    }

    public TeamCore getTeamCore(Block block) {
        for (TeamCore teamCore : teamCores) {
            if (teamCore.getCoreBlock().equals(block)) {
                return teamCore;
            }
        }
        return null;
    }

    public TeamCore getTeamCore(UUID uuid) {
        for (TeamCore teamCore : teamCores) {
            if (teamCore.getPlacePlayer().equals(uuid)) {
                return teamCore;
            }
        }
        return null;
    }

    public TeamCore getTeamCore(Creeper creeper) {
        for (TeamCore teamCore : teamCores) {
            if (teamCore.getCoreCreeper().equals(creeper)) {
                return teamCore;
            }
        }
        return null;
    }

    public TeamCore getTeamCoreByLocation(Location location) {
        Block block = location.getBlock();
        for (TeamCore teamCore : teamCores) {
            if (teamCore.getConnectedBlocks().contains(block) || teamCore.getTerritoryBlocks().contains(block)) {
                return teamCore;
            }
        }
        return null;
    }

    public void addConnectedTeamBlocks(TeamCore teamCore, Block block) {
        Set<Block> visitedBlocks = new HashSet<>();
        teamCore.setConnectedBlocks(exploreConnectedBlocks(block));
    }

    public TeamCore findAdjacentBlockTeamCore(Block block) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (Math.abs(dx) + Math.abs(dy) + Math.abs(dz) == 1) {
                        Block adjacentBlock = block.getRelative(dx, dy, dz);
                        BlockStats adjacentBlockStats = blockStatsManager.getBlockStats(adjacentBlock);
                        if (adjacentBlockStats != null && adjacentBlockStats.getBlockType() == BlockType.BUILDING) {
                            TeamCore teamCore = getTeamCoreByLocation(adjacentBlock.getLocation());
                            if (teamCore != null)
                                return teamCore;
                        }
                    }
                }
            }
        }
        return null;
    }

    public void onBlockBreak(Block brokenBlock) {
        TeamCore teamCore = getTeamCoreForBlock(brokenBlock);

        if (teamCore == null) return;

        teamCore.getConnectedBlocks().remove(brokenBlock);

        // Re-explore
        Set<Block> remainingConnectedBlocks = exploreConnectedBlocks(teamCore.getCoreBlock());

        // Find and remove blocks that are no longer connected to the core
        Set<Block> toRemove = new HashSet<>(teamCore.getConnectedBlocks());
        toRemove.removeAll(remainingConnectedBlocks);
        teamCore.getConnectedBlocks().removeAll(toRemove);

        teamCore.setConnectedBlocks(remainingConnectedBlocks);
    }

    private TeamCore getTeamCoreForBlock(Block block) {
        for (TeamCore teamCore : getTeamCores()) {
            if (teamCore.getConnectedBlocks().contains(block)) {
                return teamCore;
            }
        }
        return null;
    }

    private Set<Block> exploreConnectedBlocks(Block startBlock) {
        Set<Block> visitedBlocks = new HashSet<>();
        exploreConnectedBlocksIterative(startBlock, visitedBlocks);
        return visitedBlocks;
    }

    private void exploreConnectedBlocksIterative(Block startBlock, Set<Block> visitedBlocks) {
        Set<Block> stack = new HashSet<>();
        stack.add(startBlock);

        while (!stack.isEmpty()) {
            Block block = stack.iterator().next();
            stack.remove(block);

            if (visitedBlocks.contains(block)) continue;

            visitedBlocks.add(block);

            BlockStats blockStats = blockStatsManager.getBlockStats(block);
            if (blockStats == null || blockStats.getBlockType() != BlockType.BUILDING) continue;

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (Math.abs(dx) + Math.abs(dy) + Math.abs(dz) == 1) {
                            Block adjacentBlock = block.getRelative(dx, dy, dz);
                            if (!visitedBlocks.contains(adjacentBlock)) {
                                BlockStats adjacentBlockStats = blockStatsManager.getBlockStats(adjacentBlock);
                                if (adjacentBlockStats != null && adjacentBlockStats.getBlockType() == BlockType.BUILDING) {
                                    stack.add(adjacentBlock);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
