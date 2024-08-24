package me.orange.anan.craft.behaviour.teamCore;

import com.cryptomorin.xseries.XMaterial;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import io.fairyproject.bukkit.nbt.NBTKey;
import io.fairyproject.bukkit.nbt.NBTModifier;
import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.container.DependsOn;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.util.Position;
import me.orange.anan.blocks.BlockStats;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.blocks.BlockType;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.craft.behaviour.teamCore.config.TeamCoreConfig;
import me.orange.anan.craft.behaviour.teamCore.config.TeamCoreConfigElement;
import me.orange.anan.craft.behaviour.teamCore.config.TeamCoreInventoryElement;
import me.orange.anan.craft.config.CraftConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@InjectableComponent
public class TeamCoreManager {
    private final TeamCoreConfig teamCoreConfig;
    private List<TeamCore> teamCores = new ArrayList<>();
    private final BlockStatsManager blockStatsManager;
    private final ClanManager clanManager;
    private final CraftManager craftManager;

    public TeamCoreManager(TeamCoreConfig teamCoreConfig, BlockStatsManager blockStatsManager, ClanManager clanManager, CraftManager craftManager) {
        this.teamCoreConfig = teamCoreConfig;
        this.blockStatsManager = blockStatsManager;
        this.clanManager = clanManager;
        this.craftManager = craftManager;

        loadConfig();
    }

    public void loadConfig() {
        for (TeamCoreConfigElement element : teamCoreConfig.getTeamCores()) {
            TeamCore teamCore = new TeamCore(element.getPlacePlayerUUID(), getClosestCreeper(element.getCoreCreeperLocation(), 1), element.getCoreBlockLocation().getBlock());
            teamCore.getCoreCreeper().setHealth(element.getCoreCreeperHealth());
            teamCores.add(teamCore);
            teamCore.setConnectedBlocks(element.getConnectedBlocks());

            for (TeamCoreInventoryElement inventoryElement : element.getInventories()) {
                craftManager.getCrafts().forEach((id, craft) -> {
                    if (inventoryElement.getId().equals(craft.getID())) {
                        ItemStack itemStack = craft.getItemStack().clone();
                        itemStack.setAmount(inventoryElement.getAmount());
                        teamCore.getInventory().setItem(inventoryElement.getSlot(), itemStack);
                    }
                });
            }
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

            int i = 0;
            for (ItemStack itemStack : teamCore.getInventory().getContents()) {
                if (itemStack != null) {
                    TeamCoreInventoryElement inventoryElement = new TeamCoreInventoryElement();
                    inventoryElement.setSlot(i);
                    inventoryElement.setId(NBTModifier.get().getString(itemStack, NBTKey.create("craft")));
                    inventoryElement.setAmount(itemStack.getAmount());
                    element.addInventory(inventoryElement);
                }
                i++;
            }

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
        teamCore.getCoreBlock().setType(XMaterial.AIR.parseMaterial());
        getTeamCores().remove(teamCore);
    }

    public TeamCore getTeamCore(Creeper creeper) {
        for (TeamCore teamCore : teamCores) {
            if (teamCore.getCoreCreeper().equals(creeper)) {
                return teamCore;
            }
        }
        return null;
    }

    public TeamCore getTeamCore(Player player) {
        for (TeamCore teamCore : teamCores) {
            if (clanManager.sameClan(player, Bukkit.getOfflinePlayer(teamCore.getPlacePlayer())))
                return teamCore;
        }
        return null;
    }

    public boolean isInTeamCoreClan(TeamCore teamCore, Player player) {
        return clanManager.sameClan(player, teamCore.getOfflinePlacePlayer());
    }

    public boolean inTerritory(Player player) {
        for (TeamCore teamCore : teamCores) {
            if (teamCore.getTerritoryBlocks().contains(player.getLocation().getBlock()))
                return true;
        }
        return false;
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

    public TeamCore getTeamCoreByBlock(Block block) {
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

    public boolean otherTeamBlockInRadius(Player player, Block block) {
        int radius = 20;
        Location center = block.getLocation();
        double radiusSquared = radius * radius;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location currentLocation = center.clone().add(x, y, z);

                    // Check if the squared distance is within the squared radius
                    if (center.distanceSquared(currentLocation) <= radiusSquared) {
                        Block searchedBlock = currentLocation.getBlock();

                        if (blockStatsManager.getBlockStatsMap().containsKey(searchedBlock)) {
                            for (TeamCore teamCore : getTeamCores()) {
                                if (teamCore.getConnectedBlocks().contains(searchedBlock)) {
                                    if (!isInTeamCoreClan(teamCore, player)) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isAboveOtherTeamBlock(Player player, Block block) {
        Location blockLocation = block.getLocation();

        for (TeamCore teamCore : getTeamCores()) {
            // Skip the player's own team core
            if (isInTeamCoreClan(teamCore, player)) {
                continue;
            }

            for (Block teamBlock : teamCore.getConnectedBlocks()) {
                Location teamBlockLocation = teamBlock.getLocation();

                if (blockLocation.getBlockX() == teamBlockLocation.getBlockX() &&
                        blockLocation.getBlockZ() == teamBlockLocation.getBlockZ()) {
                    return true;
                }
            }
        }
        return false;
    }
}
