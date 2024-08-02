package me.orange.anan;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.nametag.NameTagService;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.events.PlayerJoinClanEvent;
import me.orange.anan.events.PlayerLeftClanEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.world.WorldLoadEvent;

@InjectableComponent
@RegisterAsListener
public class GeneralEventListener implements Listener {
    private final NameTagService nameTagService;

    public GeneralEventListener(BlockStatsManager blockStatsManager, NameTagService nameTagService) {
        this.nameTagService = nameTagService;
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
        nameTagService.update(MCPlayer.from(event.getPlayer()));
    }
}
