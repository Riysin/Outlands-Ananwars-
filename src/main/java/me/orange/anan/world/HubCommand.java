package me.orange.anan.world;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.Bukkit;

@InjectableComponent
@Command(value={"lobby","l","hub"})
public class HubCommand extends BaseCommand {
    @Command("#")
    public void lobby(BukkitCommandContext ctx){
        ctx.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
    }
}
