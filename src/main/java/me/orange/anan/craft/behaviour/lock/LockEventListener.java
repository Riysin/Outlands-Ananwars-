package me.orange.anan.craft.behaviour.lock;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.events.PlayerRightClickLockEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@InjectableComponent
@RegisterAsListener
public class LockEventListener implements Listener {
    private final LockManager lockManager;
    private final BlockStatsManager blockStatsManager;

    public LockEventListener(LockManager lockManager, BlockStatsManager blockStatsManager) {
        this.lockManager = lockManager;
        this.blockStatsManager = blockStatsManager;
    }

    @EventHandler
    public void onPlayerRightClickLock(PlayerRightClickLockEvent event) {
        Player player = event.getPlayer();
        Block block = blockStatsManager.getMainBlock(event.getBlock());

        if (block != null) {
            if(lockManager.hasLock(block)) {
                player.sendMessage("§cThis block is already locked");
                return;
            }
            lockManager.lockBlock(player, block);
            int amount = player.getItemInHand().getAmount() - 1;
            if (amount == 0)
                player.setItemInHand(null);
            else
                player.getItemInHand().setAmount(amount);
            player.sendMessage("§aYou have locked the block");
        }
    }
}
