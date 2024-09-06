package me.orange.anan.blocks.listener;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.ActionBar;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStats;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.blocks.BlockType;
import me.orange.anan.craft.behaviour.teamCore.TeamCoreManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

@InjectableComponent
@RegisterAsListener
public class SightEventListener implements Listener {
    private final BlockStatsManager blockStatsManager;
    private final TeamCoreManager teamCoreManager;

    public SightEventListener(BlockStatsManager blockStatsManager, TeamCoreManager teamCoreManager) {
        this.blockStatsManager = blockStatsManager;
        this.teamCoreManager = teamCoreManager;
    }

    @EventHandler
    public void onTargetBlock(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        //ignored materials
        Set<Material> materials = new HashSet<>();
        materials.add(XMaterial.AIR.parseMaterial());
        materials.add(XMaterial.WATER.parseMaterial());
        materials.add(XMaterial.LAVA.parseMaterial());

        Block targetBlock = player.getTargetBlock(materials, 4);
        Block mainBlock = blockStatsManager.getMainBlock(targetBlock);

        if (mainBlock != null) {
            BlockStats blockStats = blockStatsManager.getBlockStats(mainBlock);
            if (blockStats != null && blockStats.getBlockType() == BlockType.BUILDING) {
                ActionBar.sendActionBar(player, " health:§a " + blockStats.getHealth());
            }
        }

        Entity target = getTargetEntity(player, 5); // 10 is the max distance to check

        if (target instanceof Slime) {
            Slime slime = (Slime) target;
            double health = slime.getHealth();
            ActionBar.sendActionBar(player, " health:§a " + health);
        }

        if (teamCoreManager.isInTerritory(player)) {
            ActionBar.sendActionBar(player, "§3You are in a territory!");
        }

        if (target instanceof Creeper) {
            Creeper creeper = (Creeper) target;
            ActionBar.sendActionBar(player, "§6Core HP: §a" + creeper.getHealth());
        }
    }

    private Entity getTargetEntity(Player player, int range) {
        Location eyeLocation = player.getEyeLocation();
        Vector direction = eyeLocation.getDirection().normalize();

        for (int i = 0; i < range; i++) {
            Location checkLocation = eyeLocation.add(direction);
            for (Entity entity : player.getNearbyEntities(range, range, range)) {
                if (entity.getLocation().distance(checkLocation) < 1.5) {
                    return entity;
                }
            }
        }
        return null;
    }
}
