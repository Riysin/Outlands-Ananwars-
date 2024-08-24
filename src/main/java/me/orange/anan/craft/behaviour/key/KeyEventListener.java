package me.orange.anan.craft.behaviour.key;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.craft.behaviour.lock.LockManager;
import me.orange.anan.events.PlayerRightClickKeyEvent;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@InjectableComponent
@RegisterAsListener
public class KeyEventListener implements Listener {
    private final LockManager lockManager;
    private final BlockStatsManager blockStatsManager;

    public KeyEventListener(LockManager lockManager, BlockStatsManager blockStatsManager) {
        this.lockManager = lockManager;
        this.blockStatsManager = blockStatsManager;
    }

    @EventHandler
    public void onRightClickKey(PlayerRightClickKeyEvent event) {
        Block block = blockStatsManager.getMainBlock(event.getBlock());
        Player player = event.getPlayer();

        if (lockManager.isLockableBlock(block)) {
            if(lockManager.hasLock(block)) {
                lockManager.unlockBlock(block);

                int amount = player.getItemInHand().getAmount() - 1;
                if (amount == 0)
                    player.setItemInHand(null);
                else
                    player.getItemInHand().setAmount(amount);

                player.getWorld().playEffect(block.getLocation(), Effect.DOOR_TOGGLE, 0);
                player.sendMessage("§aYou have unlocked the door");
                return;
            }
            player.sendMessage("§cThis door is not locked");
        }
    }
}
