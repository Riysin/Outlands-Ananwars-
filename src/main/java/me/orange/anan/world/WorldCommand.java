package me.orange.anan.world;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.craft.behaviour.teamCore.TeamCore;
import me.orange.anan.craft.behaviour.teamCore.TeamCoreManager;
import me.orange.anan.world.resource.OreClusterPopulator;
import org.bukkit.World;

@InjectableComponent
@Command(value = "world", permissionNode = "world.admin")
public class WorldCommand extends BaseCommand {
    private final WorldManager worldManager;
    private final TeamCoreManager teamCoreManager;
    private final BlockStatsManager blockStatsManager;

    public WorldCommand(WorldManager worldManager, TeamCoreManager teamCoreManager, BlockStatsManager blockStatsManager) {
        this.worldManager = worldManager;
        this.teamCoreManager = teamCoreManager;
        this.blockStatsManager = blockStatsManager;
    }

    @Command("create")
    public void createWorld(BukkitCommandContext ctx, @Arg("name") String world) {
        worldManager.bukkitCreateWorld(world);
    }

    @Command("goto")
    public void goToWorld(BukkitCommandContext ctx, @Arg("world") World world) {
        ctx.getPlayer().teleport(world.getSpawnLocation());
    }

    @Command("delete")
    public void deleteWorld(BukkitCommandContext ctx, @Arg("world") String world) {
        worldManager.bukkitRemoveWorld(world);
    }

    @Command("populate")
    public void populateWorld(BukkitCommandContext ctx, @Arg("world") World world) {
        world.getPopulators().add(new OreClusterPopulator(teamCoreManager, blockStatsManager));
        ctx.getPlayer().sendMessage("Populated" + world.getName());
    }

    @Command("unpopulate")
    public void unpopulateWorld(BukkitCommandContext ctx, @Arg("world") World world) {
        world.getPopulators().removeIf(populator -> populator instanceof OreClusterPopulator);
        ctx.getPlayer().sendMessage("Unpopulated" + world.getName());
    }
}
