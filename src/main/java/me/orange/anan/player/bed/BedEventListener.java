package me.orange.anan.player.bed;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStatsManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@InjectableComponent
@RegisterAsListener
public class BedEventListener implements Listener {
    private final BedManager bedManager;
    private final BedMenu bedMenu;
    private final BlockStatsManager blockStatsManager;

    public BedEventListener(BedManager bedManager, BedMenu bedMenu, BlockStatsManager blockStatsManager) {
        this.bedManager = bedManager;
        this.bedMenu = bedMenu;
        this.blockStatsManager = blockStatsManager;
    }

    @EventHandler
    public void onBedPlace(BlockMultiPlaceEvent event) {
        Player player = event.getPlayer();
        if (event.getItemInHand().getType() == XMaterial.RED_BED.parseMaterial()) {
            Block bed = event.getBlock();
            bedManager.addBed(player, bed.getLocation());
        }
    }

    @EventHandler
    public void onBedBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(event.getBlock().getType() == XMaterial.RED_BED.parseMaterial()) {
            Block bed = event.getBlock();
            bedManager.removeBed(player, bed.getLocation());
        }
    }

    @EventHandler
    public void onClickBed(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();
            Block block = blockStatsManager.getMainBlock(clickedBlock);
            if (block != null && block.getType() == Material.BED_BLOCK) {
                Bed bed = bedManager.getBed(block);
                event.setCancelled(true);
                bedMenu.open(event.getPlayer(), bed);
            }
        }
    }
}
