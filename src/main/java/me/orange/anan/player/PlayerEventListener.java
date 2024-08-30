package me.orange.anan.player;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.ActionBar;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.nametag.NameTagService;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.craft.crafting.CraftTimerManager;
import me.orange.anan.job.JobManager;
import me.orange.anan.job.JobStats;
import me.orange.anan.player.config.PlayerConfig;
import me.orange.anan.player.npc.PlayerNPCManager;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

@InjectableComponent
@RegisterAsListener
public class PlayerEventListener implements Listener {
    private final PlayerDataManager playerDataManager;
    private final CraftTimerManager craftTimerManager;
    private final NameTagService nameTagService;
    private final JobManager jobManager;
    private final CraftManager craftManager;

    public PlayerEventListener(PlayerDataManager playerDataManager, CraftTimerManager craftTimerManager, NameTagService nameTagService, JobManager jobManager, CraftManager craftManager) {
        this.playerDataManager = playerDataManager;
        this.craftTimerManager = craftTimerManager;
        this.nameTagService = nameTagService;
        this.jobManager = jobManager;
        this.craftManager = craftManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerDataManager.setUpPlayer(event);
        jobManager.getJobStatsMap().putIfAbsent(player.getUniqueId(), new JobStats());

        Bukkit.getOnlinePlayers().forEach(player1 -> {
            nameTagService.update(MCPlayer.from(player1));
        });

    }

    @EventHandler
    public void onEntityKilled(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null)
            return;
        if (killer.getType() == EntityType.PLAYER) {
            playerDataManager.getPlayerData(killer).addKill();
        }
    }

    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent event) {
        event.setQuitMessage("§e" + event.getPlayer().getName() + " has left!");

        craftTimerManager.getPlayerCraftTimerList(event.getPlayer()).forEach(craftTimer -> {
            craftTimerManager.craftingFailed(event.getPlayer(), craftTimer);
        });
    }


    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        if (craftManager.getCraft(item) == null) {
            event.setCancelled(true);
            return;
        }
        ItemStack itemStack = craftManager.getItemStack(item, event.getPlayer());
        item.setItemMeta(itemStack.getItemMeta());
    }
}
