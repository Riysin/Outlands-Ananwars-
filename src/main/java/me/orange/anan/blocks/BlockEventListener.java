package me.orange.anan.blocks;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.nbt.NBTKey;
import io.fairyproject.bukkit.nbt.NBTModifier;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.config.BuildConfig;
import me.orange.anan.craft.Craft;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.craft.CraftType;
import me.orange.anan.blocks.config.NatureBlockConfig;
import me.orange.anan.blocks.config.NatureBlockElement;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

@InjectableComponent
@RegisterAsListener
public class BlockEventListener implements Listener {
    private final BlockStatsManager blockStatsManager;
    private final CraftManager craftManager;
    private final NatureBlockConfig natureBlockConfig;
    private final BuildConfig buildConfig;

    public BlockEventListener(BlockStatsManager blockStatsManager, CraftManager craftManager, NatureBlockConfig natureBlockConfig, BuildConfig buildConfig) {
        this.blockStatsManager = blockStatsManager;
        this.craftManager = craftManager;
        this.natureBlockConfig = natureBlockConfig;
        this.buildConfig = buildConfig;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        BlockStats blockStats = blockStatsManager.getBlockStats(block);

        if (blockStats.getBlockType() == BlockType.BUILDING) {

            blockStatsManager.breakBlock(player, block);
            if (player.getGameMode() == GameMode.CREATIVE)
                blockStats.setHealth(0);

            if (blockStatsManager.checkBlockBreak(block))
                blockStatsManager.getBlockStatsMap().remove(event.getBlock());
            else {
                event.setCancelled(true);
            }
            return;
        }

        Integer id = block.getTypeId();
        byte data = block.getData();
        boolean drop = false;

        for (NatureBlockElement natureBlock : natureBlockConfig.getNatureBlocks()) {
            Integer natureBlockData = natureBlock.getData();
            Integer natureBlockBlockId = natureBlock.getBlockId();

            if (id.equals(natureBlockBlockId) && (natureBlockData == -1 || data == natureBlockData)) {
                for (String key : natureBlock.getDrops().keySet()) {
                    ItemStack itemStack = craftManager.getCrafts().get(key).getItemStack();
                    itemStack.setAmount(natureBlock.getDrops().get(key));
                    dropItem(player, itemStack);
                    drop = true;
                }
                break;
            }
        }

        if(!drop && player.getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }

    private void dropItem(Player player, ItemStack itemStack) {
        player.getInventory().addItem(itemStack).forEach((k, v) -> {
            player.getWorld().dropItem(player.getLocation(), v);
        });
        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();
        Block block = event.getBlock();
        String nbtValue = NBTModifier.get().getString(item, NBTKey.create("craft"));
        Craft craft = craftManager.getCrafts().get(nbtValue);

        if(craft == null || craft.getType() != CraftType.BUILD){
            if(player.getGameMode() != GameMode.CREATIVE)
                event.setCancelled(true);
            return;
        }
        else if(isBesideNatureBlock(block)){
            event.setCancelled(true);
            player.sendMessage("§c你不能在可挖掘的資源旁建造方塊!");
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 0.2f);
            return;
        }

        int health = buildConfig.getBuildBlocks().get(nbtValue);
        blockStatsManager.placeBlock(event.getPlayer(), event.getBlockPlaced(), health);
    }

    private boolean isBesideNatureBlock(Block block){
        return isNatureBlock(block.getLocation().clone().add(0, 1, 0).getBlock())
                || isNatureBlock(block.getLocation().clone().add(0, -1, 0).getBlock())
                || isNatureBlock(block.getLocation().clone().add(1, 0, 0).getBlock())
                || isNatureBlock(block.getLocation().clone().add(-1, 0, 0).getBlock())
                || isNatureBlock(block.getLocation().clone().add(0, 0, 1).getBlock())
                || isNatureBlock(block.getLocation().clone().add(0, 0, -1).getBlock());
    }

    private boolean isNatureBlock(Block block){
        Integer id = block.getTypeId();

        for (NatureBlockElement natureBlock : natureBlockConfig.getNatureBlocks()) {
            Integer data = natureBlock.getData();
            if(id.equals(natureBlock.getBlockId()) && (data == -1 || block.getData() == data))
                return true;
        }
        return false;
    }
}
