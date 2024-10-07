package me.orange.anan.craft.behaviour.key;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.craft.behaviour.lock.LockManager;
import me.orange.anan.craft.behaviour.teamCore.TeamCore;
import me.orange.anan.events.PlayerRightClickKeyEvent;
import me.orange.anan.events.TeamCoreUnlockEvent;
import org.bukkit.Effect;
import org.bukkit.Sound;
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

        if (lockManager.isUnlockableBlock(block)) {
            if(lockManager.hasLock(block)) {
                lockManager.unlockBlock(block);

                removeItem(player);

                player.getWorld().playSound(block.getLocation(), Sound.ANVIL_BREAK, 1.0f, 1.0f);
                player.sendMessage("§a你成功解鎖了此物");
                return;
            }
            player.sendMessage("§c此物已經沒有被上鎖");
        }
    }

    @EventHandler
    public void onUnlockTeamCore(TeamCoreUnlockEvent event) {
        Player player = event.getPlayer();
        TeamCore teamCore = event.getTeamCore();

        lockManager.unlockTeamCore(teamCore);
        removeItem(player);
        player.getWorld().playSound(teamCore.getCoreCreeper().getLocation(), Sound.CHEST_CLOSE, 1.0f, 1.0f);
        player.sendMessage("§a你成功解鎖了你的隊伍核心");
    }

    private void removeItem(Player player) {
        int amount = player.getItemInHand().getAmount() - 1;
        if (amount == 0)
            player.setItemInHand(null);
        else
            player.getItemInHand().setAmount(amount);
    }
}
