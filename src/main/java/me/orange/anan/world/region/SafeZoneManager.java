package me.orange.anan.world.region;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.WorldData;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.log.Log;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class SafeZoneManager {
    private int zoneCounter = 0;

    public boolean isInSafeZone(Player player) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(player.getLocation());

        return set.queryValue(localPlayer, DefaultFlag.PVP) != null;
    }

    private void configureSafeZone(String regionName) {
        RegionManager regions = getRegionManager();
        ProtectedRegion region = regions.getRegion(regionName);

        if (region != null) {
            // Deny various actions in the region
            region.setFlag(DefaultFlag.BLOCK_BREAK, StateFlag.State.DENY);
            region.setFlag(DefaultFlag.BLOCK_PLACE, StateFlag.State.DENY);
            region.setFlag(DefaultFlag.CHEST_ACCESS, StateFlag.State.DENY);
            region.setFlag(DefaultFlag.DAMAGE_ANIMALS, StateFlag.State.DENY);
            region.setFlag(DefaultFlag.ENDER_BUILD, StateFlag.State.DENY);
            region.setFlag(DefaultFlag.ENTITY_ITEM_FRAME_DESTROY, StateFlag.State.DENY);
            region.setFlag(DefaultFlag.ENTITY_PAINTING_DESTROY, StateFlag.State.DENY);
            region.setFlag(DefaultFlag.MOB_SPAWNING, StateFlag.State.DENY);
            region.setFlag(DefaultFlag.MOB_DAMAGE, StateFlag.State.DENY);
            region.setFlag(DefaultFlag.PVP, StateFlag.State.DENY);
            region.setFlag(DefaultFlag.PLACE_VEHICLE, StateFlag.State.DENY);
            region.setFlag(DefaultFlag.GREET_MESSAGE, "§eYou are in a safe zone");
            region.setFlag(DefaultFlag.FAREWELL_MESSAGE, "§eYou are leaving the safe zone");

            // Allow entry into the region
            region.setFlag(DefaultFlag.ENTRY, StateFlag.State.ALLOW);
        }
    }

    public void pasteSchematicWithSafeZone(Player player, String schematicName) {
        File file = new File(Bukkit.getPluginManager().getPlugin("WorldEdit").getDataFolder(), "schematics/" + schematicName + ".schematic");
        if (!file.exists() || !isSchematicFormatSupported(file, player, schematicName)) return;

        ClipboardReader reader = null;
        try {
            reader = ClipboardFormat.findByFile(file).getReader(Files.newInputStream(file.toPath()));
            Clipboard clipboard = reader.read(getWorldData(player));

            pasteClipboardToWorld(clipboard, player);

            List<BlockVector2D> points = generatePoints(getVector(player.getLocation()), calculateRadius(clipboard));
            createPolygonalSafeZone(points);

            player.sendMessage("Schematic pasted successfully and safe zone created!");
        } catch (IOException e) {
            player.sendMessage("§cError pasting schematic: " + e.getMessage());
            Log.error(e);
        }
    }

    private List<BlockVector2D> generatePoints(Vector location, double radius) {
        List<BlockVector2D> points = new ArrayList<>();
        for (int angle = 0; angle < 360; angle += 10) {
            double rad = Math.toRadians(angle);
            int x = (int) (location.getBlockX() + radius * Math.cos(rad));
            int z = (int) (location.getBlockZ() + radius * Math.sin(rad));
            points.add(new BlockVector2D(x, z));
        }
        return points;
    }

    private double calculateRadius(Clipboard clipboard) {
        int width = clipboard.getDimensions().getBlockX();
        int length = clipboard.getDimensions().getBlockZ();
        return Math.max(width, length) / 2.0 + 50;  // Use half of the largest dimension as the radius
    }

    private void createPolygonalSafeZone(List<BlockVector2D> points) {
        RegionManager regions = getRegionManager();
        ProtectedRegion region = new ProtectedPolygonalRegion("safe_zone_" + zoneCounter++, points, 0, 300);
        regions.addRegion(region);
        configureSafeZone(region.getId());
    }

    private void pasteClipboardToWorld(Clipboard clipboard, Player player) {
        EditSession editSession = null;
        try {
            editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(getWorld(player), -1);
            Operation operation = new ClipboardHolder(clipboard, getWorldData(player))
                    .createPaste(editSession, getWorldData(player))
                    .to(getVector(player.getLocation()))
                    .ignoreAirBlocks(true)
                    .build();
            Operations.complete(operation);
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }
    }

    private World getWorld(Player player) {
        return BukkitUtil.getLocalWorld(player.getWorld());
    }

    private WorldData getWorldData(Player player) {
        return getWorld(player).getWorldData();
    }

    private RegionManager getRegionManager() {
        RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
        return container.get(Bukkit.getWorld("world"));
    }

    private Vector getVector(Location location) {
        return new Vector(location.getX(), location.getY(), location.getZ());
    }

    private boolean isSchematicFormatSupported(File file, Player player, String schematicName) {
        if (ClipboardFormat.findByFile(file) == null) {
            player.sendMessage("Unsupported schematic format for: " + schematicName);
            return false;
        }
        return true;
    }
}
