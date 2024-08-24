package me.orange.anan.craft.behaviour.hammer;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.events.PlayerLeftClickHammerEvent;
import me.orange.anan.events.PlayerRightClickHammerEvent;
import me.orange.anan.events.PlayerShiftRightClickHammerEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@InjectableComponent
@RegisterAsListener
public class HammerEventListener implements Listener {
    private final HammerMenu hammerMenu;
    private final HammerManager hammerManager;
    private final BlockStatsManager blockStatsManager;

    public HammerEventListener(HammerMenu hammerMenu, HammerManager hammerManager, BlockStatsManager blockStatsManager) {
        this.hammerMenu = hammerMenu;
        this.hammerManager = hammerManager;
        this.blockStatsManager = blockStatsManager;
    }

    @EventHandler
    public void onPlayerRightClickHammer(PlayerRightClickHammerEvent event) {
        Player player = event.getPlayer();
        Block block = blockStatsManager.getMainBlock(event.getBlock());

        if(block !=null) {
            HammerAction hammerAction = hammerManager.getHammerStat(player).getHammerAction();
            hammerManager.upgradeBlock(player, hammerAction, block);
        }
    }

    @EventHandler
    public void onPlayerShiftRightClickHammer(PlayerShiftRightClickHammerEvent event) {
        Player player = event.getPlayer();
        hammerMenu.open(player);
    }

    @EventHandler
    public void onPlayerLeftClickHammer(PlayerLeftClickHammerEvent event) {
        Player player = event.getPlayer();
        Block block = blockStatsManager.getMainBlock(event.getBlock());
        hammerManager.fixBlock(player, block);
    }
}
