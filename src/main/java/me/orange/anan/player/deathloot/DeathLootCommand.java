package me.orange.anan.player.deathloot;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.entity.Player;

import java.util.UUID;

@InjectableComponent
@Command("death")
public class DeathLootCommand extends BaseCommand {
    private final DeathLootManager deathLootManager;

    public DeathLootCommand(DeathLootManager deathLootManager) {
        this.deathLootManager = deathLootManager;
    }

    @Command("loot")
    public void open(BukkitCommandContext ctx, @Arg("uuid") String uuid) {
        ctx.getPlayer().openInventory(deathLootManager.getDeathLoot(uuid).getInventory());
    }
}
