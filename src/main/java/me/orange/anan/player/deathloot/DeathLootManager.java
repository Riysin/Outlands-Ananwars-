package me.orange.anan.player.deathloot;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import io.fairyproject.scheduler.response.TaskResponse;
import me.orange.anan.player.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@InjectableComponent
public class DeathLootManager {
    Map<UUID, DeathLoot> deathLootMap = new HashMap<>();
    private final PlayerDataManager playerDataManager;

    public DeathLootManager(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    public Map<UUID, DeathLoot> getDeathLootMap() {
        return deathLootMap;
    }

    public void setDeathLootMap(Map<UUID, DeathLoot> deathLootMap) {
        this.deathLootMap = deathLootMap;
    }

    public void addPlayer(Player player, Location location) {
        ArmorStand armorStand = player.getWorld().spawn(location.clone().add(0, -0.3, 0), ArmorStand.class);
        armorStand.setMarker(false);

        armorStand.setHelmet(ItemBuilder.of(XMaterial.PLAYER_HEAD).skull(playerDataManager.getPlayerData(player).getSkin().skinValue).build());
        armorStand.setChestplate(player.getInventory().getChestplate());
        armorStand.setLeggings(player.getInventory().getLeggings());
        armorStand.setBoots(player.getInventory().getBoots());

        armorStand.setArms(true);
        armorStand.setBasePlate(false);
        armorStand.setGravity(false);
        armorStand.setSmall(true);
        // Set the pose
        armorStand.setHeadPose(new EulerAngle(Math.toRadians(24), 0, 0));
        armorStand.setLeftLegPose(new EulerAngle(Math.toRadians(273), 0, 0));
        armorStand.setRightLegPose(new EulerAngle(Math.toRadians(269), 0, 0));
        armorStand.setLeftArmPose(new EulerAngle(Math.toRadians(311), 0, 0));
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(342), 0, 0));

        Inventory inventory = Bukkit.createInventory(null, 9 * 5, "§e§l" + player.getName() + " 的物品");
        // Add inventory contents
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                inventory.addItem(item);
            }
        }
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null) {
                inventory.addItem(armor);
            }
        }

        deathLootMap.put(armorStand.getUniqueId(), new DeathLoot(player.getName(), player.getUniqueId(), inventory));

        MCSchedulers.getGlobalScheduler().schedule(()->{
           deathLootMap.remove(armorStand.getUniqueId());
           armorStand.remove();
        },20*60);
    }

    public DeathLoot getDeathLoot(String uuid) {
        AtomicReference<DeathLoot> loot = new AtomicReference<>(null);
        deathLootMap.forEach((k, v) -> {
            if (Objects.equals(String.valueOf(k), uuid)) {
                loot.set(v);
            }
        });
        return loot.get();
    }
}
