package me.orange.anan.craft.behaviour.lock;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.craft.behaviour.teamCore.TeamCore;
import me.orange.anan.events.PlayerRightClickLockEvent;
import me.orange.anan.events.TeamCoreLockEvent;
import org.bukkit.Material;
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

        if (lockManager.isLockableBlock(block)) {
            if(lockManager.hasLock(block)) {
                player.sendMessage("§c此物已經被上鎖");
                return;
            }
            lockManager.lockBlock(player, block);
            removeItem(player);
            player.getWorld().playSound(block.getLocation(), org.bukkit.Sound.ANVIL_USE, 0.5f, 1.0f);
            player.sendMessage("§a你成功上鎖了此物");
        }
    }

    @EventHandler
    public void onLockTeamCore(TeamCoreLockEvent event) {
        Player player = event.getPlayer();
        TeamCore teamCore = event.getTeamCore();

        lockManager.lockTeamCore(player, teamCore);
        removeItem(player);
        player.getWorld().playSound(teamCore.getCoreCreeper().getLocation(), org.bukkit.Sound.ANVIL_USE, 0.5f, 1.0f);
        player.sendMessage("§a你成功上鎖了你的隊伍核心");
    }

    private void removeItem(Player player) {
        int amount = player.getItemInHand().getAmount() - 1;
        if (amount == 0)
            player.setItemInHand(null);
        else
            player.getItemInHand().setAmount(amount);
    }
}
