package me.orange.anan.world;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.world.WorldManager;
import org.bukkit.World;

@InjectableComponent
@Command(value = "world", permissionNode = "world.admin")
public class WorldCommand extends BaseCommand {
    private final WorldManager worldManager;

    public WorldCommand(WorldManager worldManager) {
        this.worldManager = worldManager;
    }

    @Command("create")
    public void createWorld(BukkitCommandContext ctx, @Arg("name") String world) {
        worldManager.bukkitCreateWorld(world);
    }

    @Command("goto")
    public void goToWorld(BukkitCommandContext ctx, @Arg("world") World world){
        ctx.getPlayer().teleport(world.getSpawnLocation());
    }

    @Command("delete")
    public void deleteWorld(BukkitCommandContext ctx, @Arg("world") String world){
        ctx.getPlayer().sendMessage("未製作");
    }
}
