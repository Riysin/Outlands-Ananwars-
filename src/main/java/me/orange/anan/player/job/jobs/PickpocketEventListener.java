package me.orange.anan.player.job.jobs;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.events.player.PlayerDamageByPlayerEvent;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.craft.CraftType;
import me.orange.anan.player.job.Job;
import me.orange.anan.player.job.JobManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import teammt.villagerguiapi.events.VillagerTradeCompleteEvent;

import java.util.HashMap;
import java.util.UUID;

@InjectableComponent
@RegisterAsListener
public class PickpocketEventListener implements Listener {
    private final JobManager jobManager;
    private final CraftManager craftManager;
    private final Job job;
    private final long COOLDOWN_TIME = 60 * 1000; // 60 seconds in milliseconds
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    public PickpocketEventListener(JobManager jobManager, CraftManager craftManager) {
        this.jobManager = jobManager;
        this.craftManager = craftManager;
        this.job = jobManager.getJobByID("pickpocket");
    }

    private boolean isPickpocket(Player player) {
        return jobManager.getCurrentJob(player) != null && jobManager.getCurrentJob(player).getID().equals("pickpocket");
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerDamageByPlayerEvent event) {
        Player player = event.getDamager();
        Player target = event.getPlayer();

        if (!isPickpocket(player) ) {
            return;
        }

        // 偷竊
        if (job.upgradeSKill(player, jobManager.getJobLevel(player, job))) {
            ItemStack stolenItem = stealRandomItem(target);
            if (stolenItem != null) {
                stolenItem = craftManager.getItemStack(stolenItem, player);
                player.getInventory().addItem(stolenItem);
                player.sendMessage("§a你偷竊了 " + target.getName() + " 的 " + stolenItem.getItemMeta().getDisplayName() + "!");
                target.sendMessage("§c你的 " + stolenItem.getItemMeta().getDisplayName() + " 被偷走了!");
                player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
                target.playSound(target.getLocation(), Sound.GHAST_CHARGE, 1, 1);
            }
        }

        // 腳底抹油
        if (job.skill2(player, jobManager.getJobLevel(player, job))) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 3, 1));
        }
    }

    private ItemStack stealRandomItem(Player target) {
        PlayerInventory inventory = target.getInventory();
        for (ItemStack item : inventory.getContents()) {
            if (item != null && craftManager.getCraft(item).getType().equals(CraftType.RESOURCE)) {
                inventory.remove(item);
                return item;
            }
        }
        return null;
    }

    @EventHandler
    public void onPlayerTrade(VillagerTradeCompleteEvent event) {
        Player player = event.getPlayer();

        if (!isPickpocket(player)) {
            return;
        }

        if (job.skill1(player, jobManager.getJobLevel(player, job))) {
            ItemStack stolenItem = craftManager.getItemStack(event.getTrade().getResult(), player);
            player.getInventory().addItem(stolenItem);
            player.sendMessage("§a你成功偷到了額外的一個 " + stolenItem.getItemMeta().getDisplayName() + "!");
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            Player target = (Player) event.getEntity();

            if (!isPickpocket(attacker)) {
                return;
            }

            // 精準偷襲
            if (isBehindTarget(attacker, target)) {
                event.setDamage(event.getDamage() * 1.5); // 50% 傷害提升
                if (Math.random() < 0.15) {
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 3, 1)); // 15% 機率暈眩
                }
            }
        }
    }

    private boolean isBehindTarget(Player attacker, Player target) {
        Vector targetDirection = target.getLocation().getDirection();
        Vector attackerDirection = attacker.getLocation().toVector().subtract(target.getLocation().toVector()).normalize();
        return targetDirection.angle(attackerDirection) < Math.PI / 2; // 檢查是否從背後攻擊
    }

    @EventHandler
    public void onPlayerUseSkill(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Check if the player is a Thief (implement this method based on your job system)
        if (!isPickpocket(player)) return;

        // Check if the player is using the skill (you can use an item or a specific action)
        if (player.getItemInHand().getType().equals(XMaterial.EMERALD.parseMaterial()) && event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            long currentTime = System.currentTimeMillis();

            // Check if the player is on cooldown
            if (cooldowns.containsKey(player.getUniqueId())) {
                long lastUsed = cooldowns.get(player.getUniqueId());
                if (currentTime - lastUsed < COOLDOWN_TIME) {
                    player.sendMessage("§c技能還在冷卻中，請稍後再試。");
                    return; // Exit if still on cooldown
                }
            }

            // Activate the skill
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 5, 0)); // 5 seconds invisibility
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 1)); // 5 seconds speed

            // Set the cooldown for the player
            cooldowns.put(player.getUniqueId(), currentTime);

            // Send feedback to the player
            player.sendMessage("§a你已進入隱身狀態!");
        }
    }
}
