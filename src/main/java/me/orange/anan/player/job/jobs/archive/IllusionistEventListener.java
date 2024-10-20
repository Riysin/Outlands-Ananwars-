package me.orange.anan.player.job.jobs.archive;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.player.job.Job;
import me.orange.anan.player.job.JobManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Random;

@InjectableComponent
@RegisterAsListener
public class IllusionistEventListener implements Listener {
    private final JobManager jobManager;
    private final Job job;

    public IllusionistEventListener(JobManager jobManager) {
        this.jobManager = jobManager;
        this.job = jobManager.getJobByID("illusionist");
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (!isIllusionist(player)) {
                return;
            }

            Random random = new Random();
            int level = jobManager.getJobLevel(player, job);

            // 幻影操控：隨等級提升機率創造假身
            double phantomChance = level * 2; // 每級增加2%的機率
            if (random.nextInt(100) < phantomChance) {
                createPhantom(player);
                event.setCancelled(true); // 假身讓攻擊無效
                player.sendMessage("§a幻影操控觸發，敵人攻擊了你的幻影！");
            }

            // 虛無屏障：低血量時自動觸發護盾
            if (player.getHealth() <= 4) { // 假設低於2顆心
                if (random.nextInt(100) < 25) { // 25%的機率觸發
                    player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 1));
                    player.sendMessage("§a虛無屏障觸發，護盾阻擋了下一次傷害！");
                }
            }

            // 幻覺反射：有機率反彈傷害
            if (random.nextInt(100) < 20) { // 20%的機率反彈部分傷害
                event.setDamage(event.getDamage() * 0.7); // 反彈30%的傷害
                player.sendMessage("§a幻覺反射觸發，反彈了部分傷害！");
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!isIllusionist(player)) {
            return;
        }

        // 迷惑光環：降低敵人攻擊準確度並且有小機率暈眩
        for (Entity nearby : player.getNearbyEntities(5, 5, 5)) {
            if (nearby instanceof Player) {
                Player enemy = (Player) nearby;
                enemy.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60, 0)); // 攻擊準確度降低

                Random random = new Random();
                if (random.nextInt(100) < 5) { // 5%的暈眩機率
                    enemy.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1)); // 暈眩效果
                    enemy.sendMessage("§c你被迷惑光環暈眩了！");
                }
            }
        }
    }

    // 虛影步主動技能的實現
    public void shadowStep(Player player) {
        if (!isIllusionist(player)) {
            return;
        }

        // 隨機瞬移並留下幻影
        Vector teleportDirection = player.getLocation().getDirection().multiply(10); // 隨機位置
        player.teleport(player.getLocation().add(teleportDirection));
        createPhantom(player);
        player.sendMessage("§a你使用了虛影步，並留下了幻影！");
    }

    // 使用Citizens API來創建幻影NPC
    private void createPhantom(Player player) {
        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        NPC phantom = registry.createNPC(player.getType(), player.getName() + "'s Phantom");

        // 將NPC設置在玩家當前的位置
        Location location = player.getLocation();
        phantom.spawn(location);

        // 設置NPC的外觀和行為
        phantom.setProtected(true); // NPC免受傷害
        phantom.getNavigator().cancelNavigation(); // 防止NPC移動
        phantom.getEntity().setCustomName(player.getName() + "'s Phantom");
        phantom.getEntity().setCustomNameVisible(true);

        // 設定幻影在幾秒後消失
        int duration = 100; // 幻影持續時間，這裡設為5秒 (100 ticks)
        phantom.getEntity().getServer().getScheduler().scheduleSyncDelayedTask(
                player.getServer().getPluginManager().getPlugin("YourPluginName"),
                phantom::destroy,
                duration
        );
    }

    // 檢查玩家是否是幻術師職業
    private boolean isIllusionist(Player player) {
        return false;//jobManager.hasCurrentJob(player) && jobManager.getCurrentJob(player).getID().equals("illusionist");
    }
}