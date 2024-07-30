package me.orange.anan.blocks;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.ActionBar;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.bukkit.xseries.XMaterialSerializer;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

    public BlockEventListener(BlockStatsManager blockStatsManager) {
        this.blockStatsManager = blockStatsManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        BlockStats blockStats = blockStatsManager.getBlockStats(block);

        if (blockStats.getBlockType() == BlockType.BUILDING) {
            blockStatsManager.breakBlock(player, block);
            if (blockStatsManager.checkBlockBreak(block))
                blockStatsManager.getBlockStatsMap().remove(event.getBlock());
            else
                event.setCancelled(true);
            return;
        }

        Material type = block.getType();
        byte data = block.getData();
        if (type == Material.LOG && data == 0) {
            dropItem(player, ItemBuilder.of(XMaterial.STICK)
                    .lore("§fuse this to craft wood block")
                    .tag("stick","resource")
                    .build());
        }else {
            event.setCancelled(true);
        }
    }

    private void dropItem(Player player, ItemStack itemStack) {
        player.getInventory().addItem(itemStack).forEach((k,v)-> {
            player.getWorld().dropItem(player.getLocation(),v);
        });
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event){
        blockStatsManager.placeBlock(event.getPlayer(), event.getBlockPlaced());
    }
}
