package me.orange.anan.craft.behaviour.hammer;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.events.PlayerLeftClickHammerEvent;
import me.orange.anan.events.PlayerRightClickHammerEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@InjectableComponent
@RegisterAsListener
public class HammerEventListener implements Listener {
    private final HammerMenu hammerMenu;
    private final HammerManager hammerManager;

    public HammerEventListener(HammerMenu hammerMenu, HammerManager hammerManager) {
        this.hammerMenu = hammerMenu;
        this.hammerManager = hammerManager;
    }

    @EventHandler
    public void onPlayerRightClickHammer(PlayerRightClickHammerEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if(player.isSneaking()){
            hammerMenu.open(player);
            return;
        }
        if(block !=null) {
            HammerAction hammerAction = hammerManager.getHammerStat(player).getHammerAction();
            hammerManager.upgradeBlock(player, hammerAction, block);
        }
    }

    @EventHandler
    public void onPlayerLeftClickHammer(PlayerLeftClickHammerEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        hammerManager.fixBlock(player, block);
    }
}
