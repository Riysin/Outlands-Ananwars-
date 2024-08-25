package me.orange.anan.player.bed;

import io.fairyproject.container.InjectableComponent;
import me.orange.anan.player.config.BedElement;
import me.orange.anan.player.config.PlayerConfig;
import me.orange.anan.player.config.PlayerConfigElement;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

@InjectableComponent
public class BedManager {
    private final PlayerConfig playerConfig;
    private final List<Bed> bedList = new ArrayList<>();

    public BedManager(PlayerConfig playerConfig) {
        this.playerConfig = playerConfig;

        loadConfig();
    }

    public void loadConfig() {
        playerConfig.getPlayerElementMap().forEach((uuid, bedConfigElements) -> {
            bedConfigElements.getBedList().forEach(bedElement -> {
                Bed bed = new Bed();
                bed.setOwner(UUID.fromString(uuid));
                bed.setBedName(bedElement.getBedName());
                bed.setLocation(bedElement.getLocation());

                bedList.add(bed);
            });
        });
    }

    public void saveConfig() {
        bedList.forEach(bed -> {
            String uuid = bed.getOwner().toString();
            playerConfig.addBed(Bukkit.getPlayer(bed.getOwner()), bed.getLocation());
        });

        playerConfig.save();
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

    public Bed getBed(Block block) {
        for (Bed bed : bedList) {
            if (bed.getLocation().equals(block.getLocation())) {
                return bed;
            }
        }
        return null;
    }

    public void addBed(Player player, Location location) {
        Bed bed = new Bed();
        bed.setOwner(player.getUniqueId());
        bed.setBedName("Bed " + (getBeds(player).size() + 1));
        bed.setLocation(location);

        bedList.add(bed);
    }

    public void removeBed(Player player, Location location) {
        bedList.removeIf(bed -> bed.getOwner().equals(player.getUniqueId()) && bed.getLocation().equals(location));
    }


}
