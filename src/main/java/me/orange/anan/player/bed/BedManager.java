package me.orange.anan.player.bed;

import io.fairyproject.container.InjectableComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

@InjectableComponent
public class BedManager {
    private final BedConfig bedConfig;
    private final List<Bed> bedList = new ArrayList<>();

    public BedManager(BedConfig bedConfig) {
        this.bedConfig = bedConfig;

        loadBedConfig();
    }

    public List<Bed> getBedList() {
        return bedList;
    }

    public List<Bed> getBeds(Player player) {
        List<Bed> beds = new ArrayList<>();

        bedList.forEach(bed -> {
            if (bed.getOwner().equals(player.getUniqueId())) {
                beds.add(bed);
            }
        });

        return beds;
    }

    public void loadBedConfig() {
        bedConfig.getPlayerBedMap().forEach((uuid, bedConfigElements) -> {
            bedConfigElements.getBedList().forEach(bedElement -> {
                Bed bed = new Bed();
                bed.setOwner(UUID.fromString(uuid));
                bed.setBedName(bedElement.getBedName());
                bed.setPosition(bedElement.getPosition());

                bedList.add(bed);
            });
        });
    }

    public void updateBed(Player player) {
        bedList.clear();
        bedConfig.getBedElements(player).forEach(bedElement -> {
            Bed bed = new Bed();
            bed.setOwner(player.getUniqueId());
            bed.setBedName(bedElement.getBedName());
            bed.setPosition(bedElement.getPosition());

            bedList.add(bed);
        });
    }

    public void addBed(Player player, Location location) {
        bedConfig.addBed(player, location);
        updateBed(player);
    }

    public void removeBed(Player player, Location location) {
        bedConfig.getBedElements(player).removeIf(bedElement -> bedElement.getLocation().equals(location));
        updateBed(player);
    }


}
