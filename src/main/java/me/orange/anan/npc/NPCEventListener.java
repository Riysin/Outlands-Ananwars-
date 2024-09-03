package me.orange.anan.npc;

import com.cryptomorin.xseries.messages.ActionBar;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.log.Log;
import me.orange.anan.events.NPCResourceDieEvent;
import me.orange.anan.events.PlayerDamageNPCResourceEvent;
import net.citizensnpcs.api.event.CitizensEnableEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@InjectableComponent
@RegisterAsListener
public class NPCEventListener implements Listener {
    @EventHandler
    public void onCitizensEnabled(CitizensEnableEvent event) {
        Log.info("NPCs enabled.");
    }

    @EventHandler
    public void onResourceDamaged(PlayerDamageNPCResourceEvent event) {
        Player player = event.getPlayer();
        NPC npc = event.getNpc();

        if(npc.getEntity() instanceof LivingEntity) {
            ActionBar.sendActionBar(player, "NPC Health: §a" + ((LivingEntity) npc.getEntity()).getHealth());
        }
    }

    @EventHandler
    public void onResourceDie(NPCResourceDieEvent event) {
        NPC npc = event.getNpc();

        npc.despawn();
        npc.getStoredLocation().getWorld().getBlockAt(npc.getStoredLocation()).breakNaturally();
    }
}
