package me.orange.anan.player.job.jobs;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.fishing.FishManager;
import me.orange.anan.player.job.Job;
import me.orange.anan.player.job.JobManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Random;

@InjectableComponent
@RegisterAsListener
public class FisherEventListener implements Listener {
    private final JobManager jobManager;
    private final FishManager fishManager;

    public FisherEventListener(JobManager jobManager, FishManager fishManager) {
        this.jobManager = jobManager;
        this.fishManager = fishManager;
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        Job job = jobManager.getJobByID("fisher");
        Entity caught = event.getCaught();
        event.setExpToDrop(0);

        if (!isFisher(player)) {
            return;
        }

        // Skill 0: Double Fish
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            ItemStack fish = fishManager.getFishingLoot(player, 1);

            if (job.upgradeSKill(player, player.getLevel())) {
                fish = fishManager.getFishingLoot(player, 2);
                player.sendMessage("§a你的技能讓你釣到了兩條魚!");
            }
            ((Item) caught).setItemStack(fish);
        }


        if (caught instanceof Player) {
            Player hookedPlayer = (Player) caught;
            Player fisher = event.getPlayer();
            // Skill 3: Damage on Hook
            if (job.skill3(player, jobManager.getJobLevel(player, job))) {
                hookedPlayer.damage(1);
                player.sendMessage("§c你的技能讓你的釣魚鉤傷害了對方!");
            }
            // Skill active: Pull Player
            if (job.active(player, jobManager.getJobLevel(player, job)) || player.isSneaking()) {
                Vector pullDirection = fisher.getLocation().toVector().subtract(hookedPlayer.getLocation().toVector()).normalize();
                hookedPlayer.setVelocity(pullDirection.multiply(0.4));

                fisher.sendMessage("You pulled " + hookedPlayer.getName() + " towards you!");
            }
        }
    }

    @EventHandler
    public void onHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Job job = jobManager.getJobByID("fisher");
        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        ItemStack previousItem = player.getInventory().getItem(event.getPreviousSlot());

        if (!isFisher(player) || !job.skill1(player, jobManager.getJobLevel(player, job))) {
            return;
        }

        // Skill 1: add Lure Level
        adjustLureLevel(newItem, 1);
        adjustLureLevel(previousItem, -1);

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();

        if (!isFisher(player)) return;

        adjustLureLevel(clickedItem, -1);
        adjustLureLevel(cursorItem, 1);
    }

    // Handle when a player drops the fishing rod
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack droppedItem = event.getItemDrop().getItemStack();

        if (!isFisher(player)) return;

        adjustLureLevel(droppedItem, -1);  // Decrease Lure when the rod is dropped
    }

    private void adjustLureLevel(ItemStack item, int adjustment) {
        if (item != null && item.getType() == Material.FISHING_ROD) {
            int currentLureLevel = item.getEnchantmentLevel(Enchantment.LURE);
            int newLureLevel = currentLureLevel + adjustment;

            if (newLureLevel > 0) {
                item.addUnsafeEnchantment(Enchantment.LURE, newLureLevel);
            } else {
                item.removeEnchantment(Enchantment.LURE);
            }
        }
    }

    @EventHandler
    public void onPlayerInWater(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        Material blockType = location.getBlock().getType();
        Job job = jobManager.getJobByID("fisher");

        if (!isFisher(player)) {
            return;
        }

        // Skill 2: Water Breathing
        if (job.skill3(player, jobManager.getJobLevel(player, job))) {
            if (blockType == Material.WATER || blockType == Material.STATIONARY_WATER) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 20 * 10, 0));

                if (player.isSneaking()) {
                    player.setVelocity(player.getLocation().getDirection().multiply(0.5));
                }
            }
        }
    }

    private boolean isFisher(Player player) {
        return jobManager.hasCurrentJob(player) && jobManager.geCurrentJob(player).getID().equals("fisher");
    }
}
