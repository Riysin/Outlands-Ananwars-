package me.orange.anan.npc;

import com.cryptomorin.xseries.messages.ActionBar;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.log.Log;
import me.orange.anan.craft.config.ToolConfig;
import me.orange.anan.events.NPCResourceDieEvent;
import me.orange.anan.events.PlayerDamageNPCResourceEvent;
import net.citizensnpcs.api.event.CitizensEnableEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

@InjectableComponent
@RegisterAsListener
public class NPCEventListener implements Listener {
    private final ToolConfig toolConfig;

    public NPCEventListener(ToolConfig toolConfig) {
        this.toolConfig = toolConfig;
    }

    @EventHandler
    public void onCitizensEnabled(CitizensEnableEvent event) {
        Log.info("NPCs enabled.");
    }

    @EventHandler
    public void onResourceDamaged(PlayerDamageNPCResourceEvent event) {
        Player player = event.getPlayer();
        NPC npc = event.getNpc();
        LivingEntity entity = (LivingEntity) npc.getEntity();
        int toolDamage = toolConfig.getToolDamage(player.getItemInHand());

        if (entity.getHealth() <= toolDamage) {
            Bukkit.getPluginManager().callEvent(new NPCResourceDieEvent(player, npc));
            return;
        }

        entity.damage(toolDamage);
        player.playEffect(player.getLocation(), Effect.ZOMBIE_CHEW_WOODEN_DOOR, 1);
        Bukkit.getPluginManager().callEvent(new PlayerMoveEvent(player, player.getLocation(), player.getLocation()));
    }
}
