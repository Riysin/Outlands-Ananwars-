package me.orange.anan.craft.behaviour.teamCore;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostInitialize;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.scheduler.MCScheduler;
import io.fairyproject.mc.scheduler.MCSchedulers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

@InjectableComponent
public class TeamCoreRunTime {
    private final TeamCoreManager teamCoreManager;

    public TeamCoreRunTime(TeamCoreManager teamCoreManager){
        this.teamCoreManager = teamCoreManager;
    }

    @PostInitialize
    public void init() {
        MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            Set<UUID> uuids = new HashSet<>();
            teamCoreManager.getTeamCores().forEach(teamCore -> {
                uuids.add(teamCore.getTeamCore().getUniqueId());
            });

            // 获取所有世界中的所有生物
            for (LivingEntity entity : Bukkit.getWorlds().stream()
                    .flatMap(world -> world.getLivingEntities().stream())
                    .filter(e -> uuids.contains(e.getUniqueId()))
                    .collect(Collectors.toList())) {

                Player nearestPlayer = getNearestPlayer(entity, 10); // 获取最近10格的玩家

                if (nearestPlayer != null) {
                    //lookAtPlayer(entity, nearestPlayer);
                }
            }
        },0, 1);
    }

    private Player getNearestPlayer(LivingEntity entity, double range) {
        Player nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;

        for (Player player : entity.getWorld().getPlayers()) {
            double distance = entity.getLocation().distance(player.getLocation());

            if (distance < range && distance < nearestDistance) {
                nearestPlayer = player;
                nearestDistance = distance;
            }
        }

        return nearestPlayer;
    }

//    private void lookAtPlayer(LivingEntity entity, Player player) {
//        Location entityLocation = entity.getLocation().clone();
//        Location playerLocation = player.getEyeLocation().clone();
//
//        Vector direction = playerLocation.toVector().subtract(entityLocation.toVector());
//        direction.multiply(-1);
//        entityLocation.setDirection(direction);
//
//        entity.teleport(entityLocation);
//    }
}
