package me.orange.anan.craft.behaviour.lock;

import io.fairyproject.container.InjectableComponent;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.craft.behaviour.teamCore.TeamCore;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

@InjectableComponent
public class LockManager {
    private final LockConfig lockConfig;
    private final ClanManager clanManager;
    private List<Lock> locks = new ArrayList<>();

    public LockManager(LockConfig lockConfig, ClanManager clanManager) {
        this.lockConfig = lockConfig;
        this.clanManager = clanManager;

        loadConfig();
    }

    public void loadConfig() {
        lockConfig.getLocks().forEach(lockConfigElement -> {
            Lock lock = new Lock();
            lock.setOwner(lockConfigElement.getOwner());
            lock.setLocation(lockConfigElement.getLocation());
            locks.add(lock);
        });
    }

    public void saveConfig() {
        lockConfig.getLocks().clear();
        locks.forEach(lock -> {
            LockConfigElement lockConfigElement = new LockConfigElement();
            lockConfigElement.setOwner(lock.getOwner());
            lockConfigElement.setLocation(lock.getLocation());
            lockConfig.getLocks().add(lockConfigElement);
        });
        lockConfig.save();
    }

    public List<Lock> getLocks() {
        return locks;
    }

    public void setLocks(List<Lock> locks) {
        this.locks = locks;
    }

    public Lock getLock(TeamCore teamCore) {
        return locks.stream().filter(lock -> lock.getLocation().equals(teamCore.getCoreBlock().getLocation())).findFirst().orElse(null);
    }

    public void lockBlock(Player player, Block block) {
        Lock lock = new Lock();
        lock.setOwner(player.getUniqueId());
        lock.setLocation(block.getLocation());
        locks.add(lock);
    }

    public void lockTeamCore(Player player, TeamCore teamCore) {
        Lock lock = new Lock();
        lock.setOwner(player.getUniqueId());
        lock.setLocation(teamCore.getCoreBlock().getLocation());
        locks.add(lock);
    }

    public void unlockBlock(Block block) {
        locks.removeIf(lock -> lock.getLocation().equals(block.getLocation()));
    }

    public void unlockTeamCore(TeamCore teamCore) {
        locks.removeIf(lock -> lock.getLocation().equals(teamCore.getCoreBlock().getLocation()));
    }

    public boolean hasLock(Block block) {
        return locks.stream().anyMatch(lock -> lock.getLocation().equals(block.getLocation()));
    }

    public boolean hasLock(TeamCore teamCore) {
        return locks.stream().anyMatch(lock -> lock.getLocation().equals(teamCore.getCoreBlock().getLocation()));
    }

    public boolean isInOwnerClan(Player player, Block block) {
        return locks.stream().anyMatch(lock -> lock.getLocation().equals(block.getLocation()) && clanManager.sameClan(player, lock.getOfflineOwner()));
    }

    private final Set<Material> LOCKABLE_MATERIALS = EnumSet.of(
            Material.FENCE_GATE,
            Material.TRAP_DOOR,
            Material.WOODEN_DOOR,
            Material.IRON_DOOR_BLOCK,
            Material.IRON_DOOR,
            Material.CHEST
    );

    public boolean isLockableBlock(Block block) {
        return LOCKABLE_MATERIALS.contains(block.getType());
    }

    private final Set<Material> UNLOCKABLE_MATERIALS = EnumSet.of(
            Material.FENCE_GATE,
            Material.TRAP_DOOR,
            Material.WOODEN_DOOR,
            Material.IRON_DOOR_BLOCK,
            Material.IRON_DOOR
    );

    public boolean isUnlockableBlock(Block block) {
        return LOCKABLE_MATERIALS.contains(block.getType());
    }
}
