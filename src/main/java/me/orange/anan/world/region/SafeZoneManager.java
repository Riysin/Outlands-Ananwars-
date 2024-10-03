package me.orange.anan.world.region;

import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.fairyproject.container.InjectableComponent;

import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class SafeZoneManager {
    public void createSafeZone(){
        RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
        RegionManager regions = container.get(WorldGuardPlugin.inst().getServer().getWorld("world"));

        List<BlockVector2D> points = new ArrayList<>();
        points.add(new BlockVector2D(0, 0));
        points.add(new BlockVector2D(0, 10));
        int minY = 0;
        int maxY = 10;
        ProtectedRegion region = new ProtectedPolygonalRegion("safe_zone", points, minY, maxY);



        if (regions == null) {
            return;
        }
        regions.addRegion(region);
    }

    public void test(){

    }
}
