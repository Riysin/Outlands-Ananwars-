package me.orange.anan.craft.behaviour.teamCore;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStats;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.blocks.BlockType;
import me.orange.anan.events.PlayerLeftClickHammerEvent;
import me.orange.anan.events.PlayerPlaceTeamCoreEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.metadata.MetadataValue;

import java.util.EventListener;

@InjectableComponent
@RegisterAsListener
public class TeamCoreEventListener implements Listener {
    private final TeamCoreManager teamCoreManager;
    private final BlockStatsManager blockStatsManager;

    public TeamCoreEventListener(TeamCoreManager teamCoreManager, BlockStatsManager blockStatsManager) {
        this.teamCoreManager = teamCoreManager;
        this.blockStatsManager = blockStatsManager;
    }

    @EventHandler
    public void playerPlaceTeamCore(PlayerPlaceTeamCoreEvent event){
        Player player = event.getPlayer();
        Block block = event.getPlaceBlock();
        Location location = block.getLocation();
        World world = player.getWorld();
        BlockStats belowBlockStat = blockStatsManager.getBlockStats(world.getBlockAt(location.clone().add(0, -1, 0)));

        if(belowBlockStat.getBlockType() != BlockType.BUILDING){
            player.sendMessage("§c隊伍核心只能放置在建築方塊上方!");
            player.playSound(player.getLocation(), Sound.FIZZ, 1, 1);
            event.setCancelled(true);
            return;
        }

        Creeper teamCore = block.getWorld().spawn(location.clone().add(0.5, 0.8125, 0.5), Creeper.class);
        teamCore.setCustomNameVisible(false);
        teamCore.setRemoveWhenFarAway(false);
        teamCore.setMaxHealth(300);
        teamCore.setHealth(300);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "entitydata " + teamCore.getUniqueId() + " {NoAI:1b}");

        teamCoreManager.addTeamCore(player, block, teamCore);
    }
}
