package me.orange.anan;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.nametag.NameTagService;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.craft.behaviour.teamCore.TeamCoreManager;
import me.orange.anan.events.PlayerJoinClanEvent;
import me.orange.anan.events.PlayerLeftClanEvent;
import me.orange.anan.player.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.world.WorldUnloadEvent;

@InjectableComponent
@RegisterAsListener
public class GeneralEventListener implements Listener {
    private final NameTagService nameTagService;
    private final PlayerDataManager playerDataManager;
    private final TeamCoreManager teamCoreManager;

    public GeneralEventListener(BlockStatsManager blockStatsManager, NameTagService nameTagService, PlayerDataManager playerDataManager, TeamCoreManager teamCoreManager) {
        this.nameTagService = nameTagService;
        this.playerDataManager = playerDataManager;
        this.teamCoreManager = teamCoreManager;
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onJoinTeam(PlayerJoinClanEvent event) {
        nameTagService.update(MCPlayer.from(event.getPlayer()));
    }

    @EventHandler
    public void onLeftTeam(PlayerLeftClanEvent event) {
        event.getPlayers().forEach(player -> nameTagService.update(MCPlayer.from(player)));
    }

    @EventHandler
    public void onServerClose(WorldUnloadEvent event){
        playerDataManager.saveConfig();
        teamCoreManager.saveConfig();
    }
}
