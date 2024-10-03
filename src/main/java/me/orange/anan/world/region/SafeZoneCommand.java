package me.orange.anan.world.region;

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

    @Command("paste")
    public void load(BukkitCommandContext ctx, @Arg("schematic name") String name) {
        safeZoneManager.pasteSchematicWithSafeZone(ctx.getPlayer(), name);
    }
}
