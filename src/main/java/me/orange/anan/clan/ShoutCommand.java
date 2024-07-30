package me.orange.anan.clan;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.command.annotation.Wildcard;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.Bukkit;

import org.bukkit.entity.Player;
import java.util.List;

@InjectableComponent
@Command(value = {"shout", "all"})
public class ShoutCommand extends BaseCommand {
    @Command("#")
    public void shout(BukkitCommandContext ctx,@Arg("message") @Wildcard() String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("§6[All]§r " + ctx.getPlayer().getName() + " : " + message);
        }
    }
}

