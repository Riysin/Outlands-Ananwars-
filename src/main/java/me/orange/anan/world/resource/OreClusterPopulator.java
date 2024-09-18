package me.orange.anan.world.resource;

import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.craft.behaviour.teamCore.TeamCoreManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

@InjectableComponent
public class OreClusterPopulator extends BlockPopulator {
    private final TeamCoreManager teamCoreManager;
    private final BlockStatsManager blockStatsManager;

    public OreClusterPopulator(TeamCoreManager teamCoreManager, BlockStatsManager blockStatsManager) {
        this.teamCoreManager = teamCoreManager;
        this.blockStatsManager = blockStatsManager;
    }

    @Override
    public void populate(World world, Random random, Chunk source) {
        int chunkX = source.getX();
        int chunkZ = source.getZ();

        if (Math.abs(chunkX * 16) > 3000 || Math.abs(chunkZ * 16) > 3000) {
            return;
        }

        double spawnChance = random.nextDouble();
        if (spawnChance < 0.02) {
            generateOreClusters(world, random, source, Material.GOLD_ORE, 1, 8); // Gold cluster
        } else if (spawnChance < 0.06) {
            generateOreClusters(world, random, source, Material.IRON_ORE, 1, 8); // Iron cluster
        }
    }

    // Method to generate clusters of ores
    private void generateOreClusters(World world, Random random, Chunk chunk, Material oreType, int clusterCount, int clusterSize) {
        for (int i = 0; i < clusterCount; i++) {
            int x = chunk.getX() * 16 + random.nextInt(16);
            int z = chunk.getZ() * 16 + random.nextInt(16);
            int y = world.getHighestBlockYAt(x, z);

            createOreCluster(world, random, x, y, z, oreType, clusterSize);
        }
    }

    // Create a cluster of ores around a central block
    private void createOreCluster(World world, Random random, int x, int y, int z, Material oreType, int clusterSize) {
        for (int i = 0; i < clusterSize; i++) {
            int offsetX = x + random.nextInt(2) - 1;
            int offsetZ = z + random.nextInt(2) - 1;
            int offsetY = y + random.nextInt(2);

            Block currentBlock = world.getBlockAt(offsetX, offsetY, offsetZ);
            Block relative = currentBlock.getRelative(BlockFace.DOWN);

            if (relative.getType() == Material.AIR
                    || relative.getType() == Material.GRASS
                    || relative.getType() == Material.LONG_GRASS) {
                currentBlock = relative;
            }

            if (!teamCoreManager.isInTerritory(currentBlock.getLocation())
                    && (!blockStatsManager.isBesideNatureBlock(currentBlock) || blockStatsManager.isBesideSameType(currentBlock, oreType))
                    && world.getBlockAt(offsetX, y, offsetZ).getRelative(BlockFace.DOWN).getType().isSolid()) {
                currentBlock.setType(oreType);
            }
        }
    }
}
