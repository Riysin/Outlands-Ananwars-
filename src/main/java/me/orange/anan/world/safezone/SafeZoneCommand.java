package me.orange.anan.world.safezone;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;

@InjectableComponent
@Command(value = {"sz", "safezone"})
public class SafeZoneCommand extends BaseCommand {
    private final SafeZoneManager safeZoneManager;

    public SafeZoneCommand(SafeZoneManager safeZoneManager) {
        this.safeZoneManager = safeZoneManager;
    }

    @Command("save")
    public void save(BukkitCommandContext ctx, @Arg("schematic name") String name) {
        ctx.getPlayer().performCommand("schematic save " + name);
    }

    @Command("paste")
    public void load(BukkitCommandContext ctx, @Arg("schematic name") String name) {
        safeZoneManager.pasteSchematicWithSafeZone(ctx.getPlayer(), name);
    }

    @Command("list")
    public void list(BukkitCommandContext ctx) {
        safeZoneManager.listSafeZones(ctx.getPlayer());
    }

    @Command("remove")
    public void remove(BukkitCommandContext ctx, @Arg("safe zone name") String name) {
        safeZoneManager.removeSafeZone(ctx.getPlayer(), name);
    }

    @Command("setTp")
    public void setTp(BukkitCommandContext ctx, @Arg("safe zone name") String name) {
        safeZoneManager.setSafeZoneTeleport(ctx.getPlayer(), name);
    }

    @Command("teleport")
    public void teleport(BukkitCommandContext ctx, @Arg("safe zone name") String name) {
        safeZoneManager.teleportToSafeZone(ctx.getPlayer(), name);
    }
}
