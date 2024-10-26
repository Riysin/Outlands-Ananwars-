package me.orange.anan.player.job.jobs;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.ActionBar;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.player.death.DeathManager;
import me.orange.anan.player.job.Job;
import me.orange.anan.player.job.JobManager;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

@InjectableComponent
@RegisterAsListener
public class HunterEventListener implements Listener {
    private final JobManager jobManager;
    private final CraftManager craftManager;
    private final DeathManager deathManager;
    private final Map<UUID, Long> cooldowns = new HashMap<>(); // 儲存玩家的冷卻時間
    private final Job job;
    private final Set<Player> healingPlayers = new HashSet<>();

    public HunterEventListener(JobManager jobManager, CraftManager craftManager, DeathManager deathManager) {
        this.jobManager = jobManager;
        this.craftManager = craftManager;
        this.job = jobManager.getJobByID("hunter");
        this.deathManager = deathManager;
    }

    private boolean isArcher(Player player) {
        return jobManager.hasCurrentJob(player) && jobManager.getCurrentJob(player).getID().equals("hunter");
    }

    @EventHandler
    public void onPlayerShootBow(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow && event.getEntity().getShooter() instanceof Player)) return;

        Arrow arrow = (Arrow) event.getEntity();
        Player player = (Player) arrow.getShooter();

        // 檢查是否為弓箭手
        if (isArcher(player)) {
            // 技能：箭矢回收
            if (job.upgradeSKill(player, player.getLevel())) {
                arrow.remove();
                player.getInventory().addItem(ItemBuilder.of(XMaterial.ARROW).build());
                player.sendMessage("§a你成功回收了箭矢!");
                player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) return;

        // 檢查獵殺者是否為玩家
        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();

            // 檢查是否為弓箭手
            if (isArcher(player)) {
                // 技能：稀有獵物獵手
                if (job.skill1(player, jobManager.getJobLevel(player, job))) {
                    ItemStack emerald = craftManager.getItemStack("emerald", player);
                    player.getWorld().dropItemNaturally(event.getEntity().getLocation(), emerald);
                    player.sendMessage("§a獵物的身上掉落了綠寶石!");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!isArcher(player)) return;

        // 檢查玩家是否在適當的地形上
        if (job.skill2(player, jobManager.getJobLevel(player, job)) && isInNaturalBiome(player) && !deathManager.isDown(player)) {
            if (event.getFrom().getBlock().equals(event.getTo().getBlock())) {
                // 玩家沒有移動，開始恢復生命值
                if (!healingPlayers.contains(player)) {
                    healingPlayers.add(player);
                    startHealing(player);
                }
            } else {
                // 玩家移動，停止恢復生命值
                healingPlayers.remove(player);
            }
        } else {
            // 玩家不在適當地形上，停止恢復生命值
            healingPlayers.remove(player);
        }
    }

    private boolean isInNaturalBiome(Player player) {
        return player.getWorld().getBiome(player.getLocation().getBlockX(), player.getLocation().getBlockZ()).name().contains("FOREST") ||
                player.getWorld().getBiome(player.getLocation().getBlockX(), player.getLocation().getBlockZ()).name().contains("PLAINS");
    }

    private void startHealing(Player player) {
        MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            if (healingPlayers.contains(player)) {
                if (player.getHealth() < player.getMaxHealth()) {
                    player.setHealth(player.getHealth() + 1);
                    ActionBar.sendActionBar(player, "§a正在恢復生命值...");
                } else {
                    healingPlayers.remove(player);
                }
            } else {
                healingPlayers.remove(player);
                player.sendMessage("§c回復中斷!");
            }
        }, 20 * 5, 20 * 5);
    }

    @EventHandler
    public void onPlayerInteract(EntityDamageByEntityEvent event) {
        // 檢查攻擊者是否箭矢
        if (event.getDamager() instanceof Arrow && event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
            Arrow arrow = (Arrow) event.getDamager();
            Player player = (Player) arrow.getShooter();
            LivingEntity target = (LivingEntity) event.getEntity();

            // 檢查是否為弓箭手
            if (isArcher(player)) {
                // 技能：獵物追蹤
                if (job.skill3(player, jobManager.getJobLevel(player, job))) {
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 1));
                    player.sendMessage("§a你的技能讓你的箭矢緩速了目標!");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (!isArcher(player)) return;

        if (player.getItemInHand().getType().equals(XMaterial.BOW.parseMaterial())
                && (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK))) {

            // Check if player is on cooldown
            long currentTime = System.currentTimeMillis();
            if (cooldowns.containsKey(player.getUniqueId())) {
                long lastUsed = cooldowns.get(player.getUniqueId());
                if (currentTime - lastUsed < 5 * 60 * 1000) {
                    long remainingTime = (5 * 60 * 1000 - (currentTime - lastUsed)) / 1000;
                    player.sendMessage("§c技能還在冷卻中，剩餘時間: " + remainingTime + "秒");
                    return; // Exit if still on cooldown
                }
            }

            // 技能：獵人步法
            if (job.active(player, jobManager.getJobLevel(player, job))) {
                player.setVelocity(player.getLocation().getDirection().multiply(2).setY(1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 1));
                player.playSound(player.getLocation(), Sound.MAGMACUBE_JUMP, 1, 1);

                cooldowns.put(player.getUniqueId(), currentTime);
            }
        }
    }
}
