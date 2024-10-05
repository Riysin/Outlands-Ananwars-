package me.orange.anan.npc.player;

import io.fairyproject.bukkit.events.player.PlayerDamageByPlayerEvent;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.player.PlayerDataManager;
import me.orange.anan.player.death.deathloot.DeathLootManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Inventory;
import net.citizensnpcs.trait.HologramTrait;
import org.bukkit.OfflinePlayer;
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
    private final ClanManager clanManager;

    public PlayerNPCEventListener(PlayerNPCManager playerNPCManager, DeathLootManager deathLootManager, PlayerDataManager playerDataManager, ClanManager clanManager) {
        this.playerNPCManager = playerNPCManager;
        this.deathLootManager = deathLootManager;
        this.playerDataManager = playerDataManager;
        this.clanManager = clanManager;
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

//    @EventHandler
//    public void onPlayerDamameEvent(PlayerDamageByPlayerEvent event) {
//        if (event.getPlayer().hasMetadata("NPC")) {
//            NPC npc = CitizensAPI.getNPCRegistry().getNPC(event.getPlayer());
//            OfflinePlayer npcOwner = playerNPCManager.getNPCOfflineOwner(npc);
//            if (clanManager.sameClan(event.getDamager(), npcOwner)) {
//                event.getDamager().sendMessage("§cYou are in the same clan!");
//                event.setCancelled(true);
//            }
//        }
//    }

    @EventHandler
    public void onNPCDeath(NPCDeathEvent event) {
        NPC npc = event.getNPC();

        if (!npc.getOrAddTrait(HologramTrait.class).getLines().get(0).equals("§c[Offline]")) {
            return;
        }
        deathLootManager.addNPC(npc, npc.getStoredLocation());
        Inventory traitInventory = playerNPCManager.getTraitInventory(npc);
        traitInventory.getInventoryView().clear();
        traitInventory.setContents(traitInventory.getInventoryView().getContents());
        playerDataManager.getPlayerData(playerNPCManager.getNPCOfflineOwner(npc)).setNpcDied(true);
    }

}
