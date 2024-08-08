package me.orange.anan.craft.behaviour.hammer;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.nbt.NBTKey;
import io.fairyproject.bukkit.nbt.NBTModifier;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.bukkit.xseries.XMaterialSerializer;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStats;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.blocks.config.BlockConfig;
import me.orange.anan.blocks.config.BuildConfig;
import me.orange.anan.craft.Craft;
import me.orange.anan.craft.CraftManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@InjectableComponent
public class HammerManager {
    private Map<UUID, HammerStat> hammerStatsMap = new HashMap<>();
    private final BlockStatsManager blockStatsManager;
    private final CraftManager craftManager;
    private final BuildConfig buildConfig;

    public HammerManager(BlockStatsManager blockStatsManager, CraftManager craftManager, BuildConfig buildConfig) {
        this.blockStatsManager = blockStatsManager;
        this.craftManager = craftManager;
        this.buildConfig = buildConfig;
    }

    public Map<UUID, HammerStat> getHammerStatsMap() {
        return hammerStatsMap;
    }

    public void setHammerStatsMap(Map<UUID, HammerStat> hammerStatsMap) {
        this.hammerStatsMap = hammerStatsMap;
    }

    public HammerStat getHammerStat(Player player) {
        UUID uuid = player.getUniqueId();

        if (hammerStatsMap.containsKey(uuid))
            return hammerStatsMap.get(uuid);
        HammerStat hammerStat = new HammerStat();
        hammerStatsMap.put(uuid, hammerStat);
        return hammerStat;
    }

    public void setHammerStat(Player player, HammerAction hammerAction) {
        UUID uuid = player.getUniqueId();
        HammerStat hammerStat = getHammerStat(player);
        hammerStat.setHammerAction(hammerAction);
        hammerStatsMap.put(uuid, hammerStat);
    }

    public void upgradeBlock(Player player, HammerAction hammerAction, Block block) {
        Map<Block, BlockStats> blockStatsMap = blockStatsManager.getBlockStatsMap();
        BlockStats blockStats = blockStatsManager.getBlockStats(block);

        switch (hammerAction) {
            case BREAK:
                blockStatsMap.remove(block);
                block.setType(Material.AIR);
                player.sendMessage("§c方塊已被破壞!");
                break;
            default:
                handleUpgrade(player, block, blockStats, hammerAction);
                break;
        }
    }

    private void handleUpgrade(Player player, Block block, BlockStats blockStats, HammerAction hammerAction) {
        Map<Block, BlockStats> blockStatsMap = blockStatsManager.getBlockStatsMap();
        String configItemID = getConfigItemID(hammerAction);
        int newLevel = getUpgradeLevel(hammerAction);

        if (configItemID != null && newLevel > getCurrentBlockLevel(block)) {
            Map<String, Craft> crafts = craftManager.getCrafts();
            List<ItemStack> recipes = craftManager.getRecipesFromIDs(crafts.get(configItemID).getRecipe(), player);

            for (ItemStack recipe : recipes) {
                if (craftManager.hasEnough(player, recipe)) {
                    int maxHealth = buildConfig.getBuildBlocks().get(configItemID);
                    int healthLost = maxHealth - blockStats.getHealth();

                    craftManager.removeItemsFromInventory(player, recipe, 1);
                    player.updateInventory();

                    blockStatsMap.remove(block);
                    ItemStack configItemWithID = craftManager.getConfigItemWithID(configItemID);
                    block.setType(configItemWithID.getType());
                    block.setData(configItemWithID.getData().getData(), true);
                    BlockStats newBlockStat = blockStatsManager.placeBlock(player, block, maxHealth);
                    newBlockStat.setHealth(maxHealth - healthLost);
                    return; // Exit after a successful upgrade
                } else
                    player.sendMessage("§c材料不足!");
            }
        } else
            player.sendMessage("§c無法升級方塊!");
    }

    private String getConfigItemID(HammerAction hammerAction) {
        switch (hammerAction) {
            case UPGRADELv2:
                return "buildLv2";
            case UPGRADELv3:
                return "buildLv3";
            case UPGRADELv4:
                return "buildLv4";
            case UPGRADELv5:
                return "buildLv5";
            default:
                return null;
        }
    }

    private int getUpgradeLevel(HammerAction hammerAction) {
        switch (hammerAction) {
            case UPGRADELv2:
                return 2;
            case UPGRADELv3:
                return 3;
            case UPGRADELv4:
                return 4;
            case UPGRADELv5:
                return 5;
            default:
                return 0;
        }
    }

    private int getCurrentBlockLevel(Block block) {
        for (int level = 1; level <= 4; level++) {
            ItemStack buildLv = craftManager.getConfigItemWithID("buildLv" + level);
            if (block.getTypeId() == buildLv.getTypeId() && block.getData() == buildLv.getData().getData()) {
                return level;
            }
        }
        return 6;
    }

    public void fixBlock(Player player, Block block) {
        BlockStats blockStats = blockStatsManager.getBlockStats(block);
        int currentHealth = blockStats.getHealth();;
        int maxHealth = buildConfig.getBuildBlocks().get("buildLv1");

        if (currentHealth < maxHealth) {
            blockStats.setHealth(currentHealth + 1);
        } else
            player.sendMessage("§c方塊已達到最大耐久度!");

    }

}
