package me.orange.anan.player.job.jobs;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.player.job.Job;
import me.orange.anan.player.job.JobManager;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@InjectableComponent
@RegisterAsListener
public class ArcherEventListener implements Listener {
    private final JobManager jobManager;
    private final CraftManager craftManager;
    private final Map<Player, Long> cooldowns = new HashMap<>(); // 儲存玩家的冷卻時間

    public ArcherEventListener(JobManager jobManager, CraftManager craftManager) {
        this.jobManager = jobManager;
        this.craftManager = craftManager;
    }

    private boolean isArcher(Player player) {
        Job job = jobManager.getCurrentJob(player);
        return job != null && job.getID().equals("archer");
    }

    @EventHandler
    public void onPlayerShootBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        // 檢查是否為弓箭手
        if (isArcher(player)) {
            // 技能：箭矢回收
            if (jobManager.getCurrentJob(player).upgradeSKill(player, jobManager.getJobLevel(player, jobManager.getCurrentJob(player)))) {
                Arrow arrow = (Arrow) event.getProjectile();
                if (arrow.isOnGround()) {
                    arrow.remove();
                    player.getInventory().addItem(ItemBuilder.of(XMaterial.ARROW).build());
                    player.sendMessage("§a你成功回收了箭矢!");
                    player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
                }
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
                if (jobManager.getCurrentJob(player).skill1(player, jobManager.getJobLevel(player, jobManager.getCurrentJob(player)))) {
                    ItemStack emerald = craftManager.getItemStack("emerald", player);
                    player.getWorld().dropItemNaturally(player.getLocation(), emerald);
                    player.sendMessage("§a你從獵物身上獲得了一顆額外的綠寶石!");
                }

                // 技能：獵物追蹤
                if (jobManager.getCurrentJob(player).skill2(player, jobManager.getJobLevel(player, jobManager.getCurrentJob(player)))) {
                    event.getDrops().add(craftManager.getItemStack("emerald", player)); // 增加掉落物
                    player.sendMessage("§a你獲得了額外的掉落物!");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        if (isArcher(player)) {
            if (jobManager.getCurrentJob(player).skill1(player, jobManager.getJobLevel(player, jobManager.getCurrentJob(player)))) {
                if (event.getItem().getItemStack().getType() == XMaterial.EMERALD.parseMaterial()) {
                    event.getItem().remove();
                    player.sendMessage("§a你悄悄偷到了額外的綠寶石!");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // 檢查是否為弓箭手
        if (isArcher(player)) {
            // 技能：追獵之箭
            if (event.getAction().toString().contains("RIGHT_CLICK") && jobManager.getCurrentJob(player).active(player, jobManager.getJobLevel(player, jobManager.getCurrentJob(player)))) {
                long currentTime = System.currentTimeMillis();
                if (cooldowns.containsKey(player) && (currentTime - cooldowns.get(player)) < 30000) {
                    player.sendMessage("§c技能尚未冷卻，請稍後再試!");
                    return;
                }

                // 發射強化箭矢
                Arrow arrow = player.launchProjectile(Arrow.class);
                arrow.setVelocity(arrow.getVelocity().multiply(2));
                arrow.setCritical(true);
                player.sendMessage("§a你發射了追獵之箭!");

                // 設置冷卻時間
                cooldowns.put(player, currentTime);
            }
        }
    }
}
