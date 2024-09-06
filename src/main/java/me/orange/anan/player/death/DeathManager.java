package me.orange.anan.player.death;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.*;

@InjectableComponent
@RegisterAsListener
public class DeathManager implements Listener {
    private List<UUID> downPlayers = new ArrayList<>();
    private Map<UUID,UUID> rescueMap = new HashMap<>();

    public List<UUID> getDownPlayers() {
        return downPlayers;
    }

    public void setDownPlayers(List<UUID> downPlayers) {
        this.downPlayers = downPlayers;
    }

    public void addDownPlayer(Player player) {
        downPlayers.add(player.getUniqueId());
    }

    public void removeDownPlayer(Player player) {
        downPlayers.remove(player.getUniqueId());
    }

    public boolean isDown(Player player) {
        return downPlayers.contains(player.getUniqueId());
    }

    public Map<UUID, UUID> getRescueMap() {
        return rescueMap;
    }

    public void setRescueMap(Map<UUID, UUID> rescueMap) {
        this.rescueMap = rescueMap;
    }

    public void addRescue(Player player, Player rescuer) {
        rescueMap.put(player.getUniqueId(), rescuer.getUniqueId());
    }

    public boolean isBeingRescued(Player player) {
        return rescueMap.containsKey(player.getUniqueId());
    }

    public boolean isRescuing(Player player) {
        return rescueMap.containsValue(player.getUniqueId());
    }

    public void stopRescueByRescued(Player player) {
        rescueMap.remove(player.getUniqueId());
    }

    public void stopRescueByRescuer(Player rescuer) {
        rescueMap.entrySet().removeIf(entry -> entry.getValue().equals(rescuer.getUniqueId()));
    }
}
