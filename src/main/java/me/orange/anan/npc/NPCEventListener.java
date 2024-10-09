package me.orange.anan.npc;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.log.Log;
import io.fairyproject.mc.scheduler.MCSchedulers;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.events.NPCResourceDieEvent;
import me.orange.anan.events.PlayerDamageNPCResourceEvent;
import net.citizensnpcs.api.event.CitizensEnableEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

@InjectableComponent
@RegisterAsListener
public class NPCEventListener implements Listener {
    private final CraftManager craftManager;
    private final NPCLootManager npcLootManager;
    private final NPCManager npcManager;

    public NPCEventListener(CraftManager craftManager, NPCLootManager npcLootManager, NPCManager npcManager) {
        this.craftManager = craftManager;
        this.npcLootManager = npcLootManager;
        this.npcManager = npcManager;
    }

    @EventHandler
    public void onCitizensEnabled(CitizensEnableEvent event) {
        Log.info("NPCs enabled.");
    }

    @EventHandler
    public void onResourceDamaged(PlayerDamageNPCResourceEvent event) {
        Player player = event.getPlayer();
        NPC npc = event.getNpc();
        Block block = event.getBlock();

        LivingEntity entity = (LivingEntity) npc.getEntity();
        int toolDamage = craftManager.getDamage(player.getItemInHand());

        if (entity.getHealth() <= toolDamage) {
            Bukkit.getPluginManager().callEvent(new NPCResourceDieEvent(player, npc, block));
            return;
        }

        entity.damage(toolDamage);
        player.getWorld().playEffect(player.getLocation(), Effect.ZOMBIE_CHEW_WOODEN_DOOR, 1);
        Bukkit.getPluginManager().callEvent(new PlayerMoveEvent(player, player.getLocation(), player.getLocation()));
    }

    @EventHandler
    public void onResourceDie(NPCResourceDieEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        ItemStack loot = npcLootManager.getLoot(player, block);
        player.getWorld().dropItem(block.getLocation(), loot);
    }

    @EventHandler
    public void onNPCSpawn(NPCSpawnEvent event) {
        NPC npc = event.getNPC();
        if (npc.getEntity().getType().equals(EntityType.SLIME))
            MCSchedulers.getGlobalScheduler().schedule(() -> npcManager.setUpLootNPC(npc), 1);
    }
}
