package me.orange.anan.craft.behaviour.hammer;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStats;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.blocks.BlockType;
import me.orange.anan.blocks.config.BuildConfig;
import me.orange.anan.craft.Craft;
import me.orange.anan.craft.CraftManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
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

        if (Objects.requireNonNull(hammerAction) == HammerAction.BREAK) {
            if (!blockStats.isJustPlaced()) {
                player.sendMessage("§c方塊放置1分鐘後即無法破壞!");
                return;
            }
            if (blockStats.getBlockType() == BlockType.BUILDING) {
                if (block.getType() != XMaterial.END_PORTAL_FRAME.parseMaterial()) {
                    blockStatsMap.remove(block);
                    block.setType(Material.AIR);
                    player.sendMessage("§c方塊已被破壞!");
                } else
                    player.sendMessage("§c無法破壞!");
            } else
                player.sendMessage("§c非建築方塊無法破壞!");
        } else {
            handleUpgrade(player, block, blockStats, hammerAction);
        }
    }

    private void handleUpgrade(Player player, Block block, BlockStats blockStats, HammerAction hammerAction) {
        String newBuild = getConfigItemID(hammerAction);
        int newLevel = getUpgradeLevel(hammerAction);

        if (blockStats.getBlockType() != BlockType.BUILDING) {
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
            player.sendMessage("§c此方塊非建築方塊!");
            return;
        }

        if (newBuild == null || newLevel < getCurrentBlockLevel(block)) {
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
            player.sendMessage("§c無法升級成你選擇的建材!");
            return;
        }

        Map<String, Craft> crafts = craftManager.getCrafts();
        List<ItemStack> recipes = craftManager.getRecipesFromIDs(crafts.get(newBuild).getRecipe(), player);

        for (ItemStack recipe : recipes) {
            if (craftManager.hasEnough(player, recipe)) {
                String currentBuild = craftManager.getCraft(block).getID();
                int maxHealth = buildConfig.getBuildBlocks().get(newBuild);
                int healthLost = buildConfig.getBuildBlocks().get(currentBuild) - blockStats.getHealth();
                blockStats.setHealth(maxHealth - healthLost);

                ItemStack newBuildItem = craftManager.getConfigItemWithID(newBuild);
                block.setType(newBuildItem.getType());
                block.setData(newBuildItem.getData().getData(), true);

                player.getWorld().playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
                craftManager.removeItemsFromInventory(player, recipe, 1);
                player.sendMessage(" §e已消耗 " + craftManager.getCraft(recipe).getName() + " x " + recipe.getAmount() + " !");
                return;
            } else {
                player.sendMessage("§c材料不足!");
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
            }
        }
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

        if (blockStats.isGettingDestroyed()) {
            player.sendMessage("§c方塊遭破壞的30秒內無法修復!");
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
            return;
        }

        if (blockStats.getBlockType() != BlockType.BUILDING) {
            player.sendMessage("§c此方塊無法修復!");
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
            return;
        }

        int currentHealth = blockStats.getHealth();
        int maxHealth = buildConfig.getBuildBlocks().get("buildLv1");
        Craft craft = craftManager.getCraft(block);
        List<ItemStack> recipes = craftManager.getRecipesFromIDs(craft.getRecipe(), player);

        for (ItemStack recipe : recipes) {
            if (!craftManager.hasEnough(player, recipe)) {
                player.sendMessage("§c材料不足!");
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                return;
            }

            if (currentHealth < maxHealth) {
                blockStats.setHealth(currentHealth + 1);
                craftManager.removeItemsFromInventory(player, recipe, recipe.getAmount() / 2);
                player.sendMessage(" §e已消耗 " + craftManager.getCraft(recipe).getName() + " x " + recipe.getAmount() / 2 + " !");
                player.getWorld().playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
                Bukkit.getPluginManager().callEvent(new PlayerMoveEvent(player, player.getLocation(), player.getLocation()));
                return;
            } else {
                player.sendMessage("§c方塊已達到最大耐久度!");
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                return;
            }
        }
    }
}
