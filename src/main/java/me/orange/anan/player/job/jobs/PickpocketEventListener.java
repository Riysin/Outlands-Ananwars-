package me.orange.anan.player.job.jobs;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.player.job.Job;
import me.orange.anan.player.job.JobManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import teammt.villagerguiapi.events.VillagerTradeCompleteEvent;

@InjectableComponent
@RegisterAsListener
public class PickpocketEventListener implements Listener {
    private final JobManager jobManager;
    private final CraftManager craftManager;
    private final Job job;

    public PickpocketEventListener(JobManager jobManager, CraftManager craftManager) {
        this.jobManager = jobManager;
        this.craftManager = craftManager;
        this.job = jobManager.getJobByID("pickpocket");
    }

    @EventHandler
    public void onPlayerInteractEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && !isPickpocket((Player) event.getDamager())) {
            return;
        }

        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (event.getEntity() instanceof Player) {
                Player target = (Player) event.getEntity();
                double chance = 0.025 * jobManager.getJobLevel(player, job); // 偷竊機率
                if (Math.random() < chance) {
                    ItemStack stolenItem = stealRandomItem(target);
                    if (stolenItem != null) {
                        player.getInventory().addItem(stolenItem);
                        player.sendMessage("§a你成功偷取了 " + stolenItem.getType().toString() + "!");
                        target.sendMessage("§c你的物品被偷走了!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerTrade(VillagerTradeCompleteEvent event) {
        Player player = event.getPlayer();
        double chance = 0.1 * jobManager.getJobLevel(player, job); // 動態偷竊機率
        if (Math.random() < chance) {
            ItemStack stolenItem = craftManager.getItemStack(event.getTrade().getResult(), player);
            player.getInventory().addItem(stolenItem);
            player.sendMessage("§a你偷偷獲得了 " + stolenItem.getItemMeta().getDisplayName() + "!");
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player&& event.getEntity() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            Player target = (Player) event.getEntity();

            if (isBehindTarget(attacker, target)) {
                event.setDamage(event.getDamage() * 1.5); // 50% 傷害提升
                if (Math.random() < 0.15) {
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1)); // 15% 機率暈眩
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (isPickpocket(player) && job.skill3(player, jobManager.getJobLevel(player, job))) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 1)); // 靈活身手技能
        }
    }

    private ItemStack stealRandomItem(Player target) {
        PlayerInventory inventory = target.getInventory();
        for (ItemStack item : inventory.getContents()) {
            if (item != null) {
                inventory.remove(item);
                return item;
            }
        }
        return null;
    }

    private boolean isPickpocket(Player player) {
        return jobManager.getCurrentJob(player) != null && jobManager.getCurrentJob(player).equals(job);
    }

    private boolean isBehindTarget(Player attacker, Player target) {
        Vector targetDirection = target.getLocation().getDirection();
        Vector attackerDirection = attacker.getLocation().toVector().subtract(target.getLocation().toVector()).normalize();
        return targetDirection.angle(attackerDirection) < Math.PI / 2; // 檢查是否從背後攻擊
    }
}
