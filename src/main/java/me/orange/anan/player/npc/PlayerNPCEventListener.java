package me.orange.anan.player.npc;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.log.Log;
import me.orange.anan.player.deathloot.DeathLootManager;
import net.citizensnpcs.api.event.CitizensEnableEvent;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

@InjectableComponent
public class PlayerNPCEventListener implements Listener {
    private final PlayerNPCManager playerNPCManager;
    private final DeathLootManager deathLootManager;

    public PlayerNPCEventListener(PlayerNPCManager playerNPCManager, DeathLootManager deathLootManager) {
        this.playerNPCManager = playerNPCManager;
        this.deathLootManager = deathLootManager;
    }

    @EventHandler
    public void onCitizensEnabled(CitizensEnableEvent event) {
        Log.info("NPCs enabled.");
    }

    @EventHandler
    public void onNPCDeath(NPCDeathEvent event) {
        NPC npc = event.getNPC();
        Player owner = playerNPCManager.getNPCOwner(npc);

        deathLootManager.addPlayer(owner, owner.getLocation());
        playerNPCManager.getTraitInventory(owner).getInventoryView().clear();
    }

    @EventHandler
    public void onClickNPC(NPCRightClickEvent event) {
        Player player = event.getClicker();

        Inventory inventory = playerNPCManager.getTraitInventory(player).getInventoryView();
        player.openInventory(inventory);
    }

}
