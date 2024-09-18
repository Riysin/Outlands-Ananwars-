package me.orange.anan;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.craft.behaviour.teamCore.TeamCoreManager;
import me.orange.anan.world.resource.OreClusterPopulator;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.world.WorldInitEvent;

@InjectableComponent
@RegisterAsListener
public class GeneralEventListener implements Listener {
    private final TeamCoreManager teamCoreManager;
    private final BlockStatsManager blockStatsManager;

    public GeneralEventListener(TeamCoreManager teamCoreManager, BlockStatsManager blockStatsManager) {
        this.teamCoreManager = teamCoreManager;
        this.blockStatsManager = blockStatsManager;
    }

    @EventHandler
    public void ServerListPingEvent(ServerListPingEvent event) {
        event.setMotd("§b§lAnan§f§lWars §r§7| §e§lby §6§l@PvpForOrange\n      §f❥ §b§l§n1.8 Available!");
        event.setMaxPlayers(134);
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void populateChunk(WorldInitEvent event) {
        Bukkit.broadcastMessage("Populating chunk");
        event.getWorld().getPopulators().add(new OreClusterPopulator(teamCoreManager, blockStatsManager));
    }
}
