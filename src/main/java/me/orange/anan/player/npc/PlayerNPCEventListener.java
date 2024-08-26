package me.orange.anan.player.npc;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.log.Log;
import me.orange.anan.player.deathloot.DeathLootManager;
import net.citizensnpcs.api.event.CitizensEnableEvent;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

@InjectableComponent
@RegisterAsListener
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
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerNPCManager.setUpNPC(player);
        playerNPCManager.despawnNPC(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playerNPCManager.spawnNPC(player);
    }

    @EventHandler
    public void onNPCDeath(NPCDeathEvent event) {
        Bukkit.broadcastMessage("NPC died.");
        NPC npc = event.getNPC();
        Player owner = playerNPCManager.getNPCOwner(npc);

        deathLootManager.addPlayer(owner, npc.getStoredLocation());
        playerNPCManager.getTraitInventory(owner).getInventoryView().clear();
    }

    @EventHandler
    public void onClickNPC(NPCRightClickEvent event) {
        Bukkit.broadcastMessage("NPC clicked.");
        Player player = event.getClicker();

        Inventory inventory = playerNPCManager.getTraitInventory(player).getInventoryView();
        player.openInventory(inventory);
    }

}
