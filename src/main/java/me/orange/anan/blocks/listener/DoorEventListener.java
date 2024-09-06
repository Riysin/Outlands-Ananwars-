package me.orange.anan.blocks.listener;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.craft.behaviour.lock.LockManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Door;

@InjectableComponent
@RegisterAsListener
public class DoorEventListener implements Listener {
    private final BlockStatsManager blockStatsManager;
    private final CraftManager craftManager;
    private final LockManager lockManager;

    public DoorEventListener(BlockStatsManager blockStatsManager, CraftManager craftManager, LockManager lockManager) {
        this.blockStatsManager = blockStatsManager;
        this.craftManager = craftManager;
        this.lockManager = lockManager;
    }

    @EventHandler
    public void onDoorClicked(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            Block block = blockStatsManager.getMainBlock(event.getClickedBlock());
            Material type = block.getType();
            boolean isKeyInHand = player.getItemInHand().isSimilar(craftManager.getItemStack(craftManager.getCrafts().get("key"), player));
            boolean isLockInHand = player.getItemInHand().isSimilar(craftManager.getItemStack(craftManager.getCrafts().get("lock"), player));

            if (lockManager.isLockableBlock(block)) {

                if (lockManager.hasLock(block) && !lockManager.isInOwnerClan(player, block)) {
                    event.setCancelled(true);
                    player.sendMessage("§c此物已經被上鎖了!");
                    return;
                }
            }

            // open iron door on player right click
            if (type == Material.IRON_DOOR_BLOCK || type == Material.IRON_DOOR) {
                if ((!lockManager.hasLock(block)|| lockManager.isInOwnerClan(player,block)) && !isKeyInHand && !isLockInHand) {
                    Door door = (Door) block.getState().getData();
                    door.setOpen(!door.isOpen());
                    block.setData(door.getData());
                    block.getWorld().playSound(block.getLocation(), Sound.DOOR_OPEN, 1.0f, 1.0f);
                }else {
                    event.setCancelled(true);
                }
            }
        }
    }
}
