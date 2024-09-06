package me.orange.anan.npc.player;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.player.PlayerDataManager;
import me.orange.anan.player.death.DeathRespawnMenu;
import me.orange.anan.player.death.deathloot.DeathLootManager;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Inventory;
import net.citizensnpcs.trait.HologramTrait;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@InjectableComponent
@RegisterAsListener
public class PlayerNPCEventListener implements Listener {
    private final PlayerNPCManager playerNPCManager;
    private final DeathLootManager deathLootManager;
    private final PlayerDataManager playerDataManager;
    private final DeathRespawnMenu deathRespawnMenu;

    public PlayerNPCEventListener(PlayerNPCManager playerNPCManager, DeathLootManager deathLootManager, PlayerDataManager playerDataManager, DeathRespawnMenu deathRespawnMenu) {
        this.playerNPCManager = playerNPCManager;
        this.deathLootManager = deathLootManager;
        this.playerDataManager = playerDataManager;
        this.deathRespawnMenu = deathRespawnMenu;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerNPCManager.setUpNPC(player);
        playerNPCManager.despawnNPC(player);

        if (playerDataManager.getPlayerData(player).isNpcDied()) {
            playerDataManager.getPlayerData(player).setNpcDied(false);
            player.sendMessage("§c§lYour NPC died while you were offline. You have to respawn!.");
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playerNPCManager.spawnNPC(player);
    }

    @EventHandler
    public void onNPCDeath(NPCDeathEvent event) {
        NPC npc = event.getNPC();

        if(!npc.getOrAddTrait(HologramTrait.class).getLines().get(0).equals("§c[Offline]")) {
            return;
        }
        deathLootManager.addNPC(npc, npc.getStoredLocation());
        Inventory traitInventory = playerNPCManager.getTraitInventory(npc);
        traitInventory.getInventoryView().clear();
        traitInventory.setContents(traitInventory.getInventoryView().getContents());
        playerDataManager.getPlayerData(playerNPCManager.getNPCOfflineOwner(npc)).setNpcDied(true);
    }

    @EventHandler
    public void onClickNPC(NPCRightClickEvent event) {
        Player player = event.getClicker();
        NPC npc = event.getNPC();

        if(npc.hasTrait(HologramTrait.class) && npc.getOrAddTrait(HologramTrait.class).getLines().get(0).equals("§c[Offline]")) {
            playerNPCManager.getTraitInventory(npc).openInventory(player);
        }

    }

}
