package me.orange.anan.player.job.jobs.archive;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.player.job.Job;
import me.orange.anan.player.job.JobManager;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import teammt.villagerguiapi.events.VillagerTradeCompleteEvent;

@InjectableComponent
@RegisterAsListener
public class BusinessManEventListener implements Listener {
    private final JobManager jobManager;
    private final CraftManager craftManager;
    private final Job job;

    public BusinessManEventListener(JobManager jobManager, CraftManager craftManager) {
        this.jobManager = jobManager;
        this.job = jobManager.getJobByID("merchant");
        this.craftManager = craftManager;
    }

    // 精明交易：與村民交易時，有機會獲得額外的獎勵
    @EventHandler
    public void onVillagerTrade(VillagerTradeCompleteEvent event) {
        Player player = event.getPlayer();
        if (!isMerchant(player)) return;

        double chance = 0.1 + 0.05 * jobManager.getJobLevel(player, job); // 機率隨等級提升
        if (Math.random() < chance) {
            player.getInventory().addItem(event.getTrade().getResult()); // 額外獲得交易的物品
            player.sendMessage("§a你成功獲得了額外的交易獎勵！");
        }
    }

    // 財富磁鐵：有機會撿起物品時自動獲得額外的綠寶石或稀有物品
    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (!isMerchant(player)) return;

        double chance = 0.05 + 0.02 * jobManager.getJobLevel(player, job);
        if (Math.random() < chance) {
            // 給玩家綠寶石或其他稀有物品
            player.getInventory().addItem(/* 新增綠寶石或稀有物品的邏輯 */);
            player.sendMessage("§a財富磁鐵發動！你獲得了額外的物品！");
        }
    }

    // 金幣守護：當玩家持有大量綠寶石時，減少受到的傷害，並自動恢復生命值
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!isMerchant(player)) return;

        int emeraldCount = getEmeraldCount(player);
        if (emeraldCount >= 10) { // 持有10個以上綠寶石
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 1, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 0, true, false)); // 自動恢復生命值
            player.sendMessage("§a金幣守護發動！你感受到綠寶石的力量。");
        }
    }

    // 富商名聲：敵對生物較少會主動攻擊
    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        if (!(event.getTarget() instanceof Player)) return;
        Player player = (Player) event.getTarget();
        if (!isMerchant(player)) return;

        double chance = 0.2 + 0.03 * jobManager.getJobLevel(player, job);
        if (Math.random() < chance) {
            event.setCancelled(true); // 取消敵對生物的鎖定
            player.sendMessage("§a富商名聲發動！敵對生物不再攻擊你。");
        }
    }

    // 賄賂術：主動技能，消耗綠寶石並使敵人暫時無法攻擊
    @EventHandler
    public void onEntityDamageByPlayer(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (!isMerchant(player)) return;

        if (player.getInventory().containsAtLeast(craftManager.getItemStack("emerald", player), 1)) {
            player.getInventory().removeItem(/* 移除綠寶石的邏輯 */); // 消耗綠寶石
            // 對附近敵人施加效果，使其短時間內無法攻擊
            for (Entity entity : player.getNearbyEntities(5, 5, 5)) {
                if (entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1)); // 暫時無法攻擊
                }
            }
            player.sendMessage("§a你使用了賄賂術，周圍的敵人無法攻擊！");
            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1, 1);
        }
    }

    // 檢查玩家是否是商人職業
    private boolean isMerchant(Player player) {
        return false;//jobManager.getCurrentJob(player) != null && jobManager.getCurrentJob(player).getID().equals("merchant");
    }

    // 計算玩家持有的綠寶石數量
    private int getEmeraldCount(Player player) {
        return craftManager.getPlayerItemAmount(player, craftManager.getItemStack("emerald", player));
    }
}
