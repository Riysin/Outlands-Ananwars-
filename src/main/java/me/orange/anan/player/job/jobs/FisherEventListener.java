package me.orange.anan.player.job.jobs;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.ActionBar;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.fishing.FishManager;
import me.orange.anan.player.job.Job;
import me.orange.anan.player.job.JobManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

@InjectableComponent
@RegisterAsListener
public class FisherEventListener implements Listener {
    private final JobManager jobManager;
    private final FishManager fishManager;
    private final ClanManager clanManager;
    private final Job job;

    public FisherEventListener(JobManager jobManager, FishManager fishManager, ClanManager clanManager) {
        this.jobManager = jobManager;
        this.fishManager = fishManager;
        this.job = jobManager.getJobByID("fisher");
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
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


        if (event.getCaught() instanceof Player) {
            Player hookedPlayer = (Player) caught;
            Player fisher = event.getPlayer();

            // Skill active: Pull Player
            if (job.active(player, jobManager.getJobLevel(player, job)) && player.isSneaking()) {
                Vector pullDirection = fisher.getLocation().toVector().subtract(hookedPlayer.getLocation().toVector()).normalize();
                hookedPlayer.setVelocity(pullDirection.multiply(0.4));

                fisher.sendMessage("You pulled " + hookedPlayer.getName() + " towards you!");
            }
        }
    }

    @EventHandler
    public void onPlayerEatFish(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();

        if (!isFisher(event.getPlayer())) {
            return;
        }

        // 檢查玩家是否吃的是生魚
        if (item.getType() == Material.RAW_FISH || item.getType() == XMaterial.SALMON.parseMaterial() || item.getType() == XMaterial.TROPICAL_FISH.parseMaterial() || item.getType() == XMaterial.PUFFERFISH.parseMaterial()) {
            Player player = event.getPlayer();

            // 回復額外的飽食度
            int extraHunger = 4; // 增加的飽食度
            int newHunger = Math.min(player.getFoodLevel() + extraHunger, 20); // 確保不超過最大值

            ActionBar.sendActionBar(player, "§a你回復了額外的飽食度!");
            player.setFoodLevel(newHunger);
            player.setSaturation(player.getSaturation() + 2);
        }
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

        if (!isFisher(player)) {
            return;
        }

        // Skill 2: Water Breathing
        if (job.skill3(player, jobManager.getJobLevel(player, job))) {
            if (blockType == Material.WATER || blockType == Material.STATIONARY_WATER) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 20 * 10, 0));
            }
        }
    }

    @EventHandler
    public void onRodHit(EntityDamageByEntityEvent event){

        if(event.getDamager() instanceof FishHook && event.getEntity() instanceof LivingEntity){
            FishHook hook = (FishHook) event.getDamager();
            Player player = ((Player) hook.getShooter());

            if (event.getEntity().hasMetadata("NPC") || (event.getEntity() instanceof Player && clanManager.sameClan(player, (Player) event.getEntity()))) {
                return;
            }

            if (job.skill3(player, jobManager.getJobLevel(player, job))) {
                ((LivingEntity) event.getEntity()).damage(2);
                player.sendMessage("§a你的技能讓你的釣竿造成了傷害!");
            }
        }
    }

    private boolean isFisher(Player player) {
        return jobManager.hasCurrentJob(player) && jobManager.getCurrentJob(player).getID().equals("fisher");
    }
}
