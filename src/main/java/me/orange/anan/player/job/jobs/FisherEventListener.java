package me.orange.anan.player.job.jobs;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.fishing.FishManager;
import me.orange.anan.player.job.Job;
import me.orange.anan.player.job.JobManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@InjectableComponent
@RegisterAsListener
public class FisherEventListener implements Listener {
    private final JobManager jobManager;
    private final CraftManager craftManager;
    private final FishManager fishManager;

    public FisherEventListener(JobManager jobManager, CraftManager craftManager, FishManager fishManager) {
        this.jobManager = jobManager;
        this.craftManager = craftManager;
        this.fishManager = fishManager;
    }

    @EventHandler
    public void onHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Job job = jobManager.getJobByID("fisher");

        if (jobManager.hasJob(player) && jobManager.getPlayerCurrentJob(player) == job) {
            ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
            ItemStack previousItem = player.getInventory().getItem(event.getPreviousSlot());

            if (newItem != null && newItem.getType() == Material.FISHING_ROD) {
                int currentLureLevel = newItem.getEnchantmentLevel(Enchantment.LURE);
                newItem.addUnsafeEnchantment(Enchantment.LURE, currentLureLevel + 1);
            }

            if (previousItem != null && previousItem.getType() == Material.FISHING_ROD) {
                int currentLureLevel = previousItem.getEnchantmentLevel(Enchantment.LURE);
                if (currentLureLevel > 1) {
                    previousItem.addUnsafeEnchantment(Enchantment.LURE, currentLureLevel - 1);
                } else {
                    previousItem.removeEnchantment(Enchantment.LURE);
                }
            }
        }
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        Job job = jobManager.getJobByID("fisher");
        event.setExpToDrop(0);
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            ItemStack fish = ((Item) event.getCaught()).getItemStack();

            if (!jobManager.hasJob(player) || jobManager.getPlayerCurrentJob(player) != job) {
                fish = fishManager.getFishingLoot(player, 1);
            } else if (job.upgradeSKill(player.getLevel())) {
                fish = fishManager.getFishingLoot(player, 2);
                player.sendMessage("§a你的技能讓你釣到了兩條魚!");
            }
            ((Item) event.getCaught()).setItemStack(fish);
        }
    }

    @EventHandler
    public void onPlayerInWater(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Job job = jobManager.getJobByID("fisher");

        if (!jobManager.hasJob(player) || jobManager.getPlayerCurrentJob(player) != job) {
            return;
        }
        Fisher fisher = (Fisher) job;
        if (fisher.skill3(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 20 * 10, 0));
        }
    }
}
