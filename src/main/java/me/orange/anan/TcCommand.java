package me.orange.anan;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import io.fairyproject.scheduler.response.TaskResponse;
import me.orange.anan.player.PlayerStatsMenu;
import me.orange.anan.player.config.PlayerConfig;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@InjectableComponent
@Command(value = "tc", permissionNode = "tc.admin")
public class TcCommand extends BaseCommand {
    private final PlayerStatsMenu playerStatsMenu;

    private final PlayerConfig playerConfig;

    public TcCommand(PlayerStatsMenu playerStatsMenu, PlayerConfig playerConfig) {
        this.playerStatsMenu = playerStatsMenu;
        this.playerConfig = playerConfig;
    }

    @Command("menu")
    public void openMenu(BukkitCommandContext ctx, @Arg("name") Player player) {
        playerStatsMenu.open(ctx.getPlayer(), player);
    }

    @Command("kills")
    public void showKills(BukkitCommandContext ctx) {
        int killCount = playerConfig.getPlayerElement(ctx.getPlayer().getName()).getKills();
        ctx.getPlayer().sendMessage(killCount + "");
    }

    @Command("deaths")
    public void showDeaths(BukkitCommandContext ctx) {
        int deathCount = playerConfig.getPlayerElement(ctx.getPlayer().getName()).getDeaths();
        ctx.getPlayer().sendMessage(deathCount + "");
    }

    @Command("test")
    public void test(BukkitCommandContext ctx) {
        CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            if(ctx.getPlayer().getItemInHand()!=null){
                return TaskResponse.success("");
            }
            ctx.getPlayer().sendMessage("test");
            return TaskResponse.continueTask();
        }, 0, 20, RepeatPredicate.length(Duration.ofSeconds(5))).getFuture();

        future.thenRun(()->{
            ctx.getPlayer().sendMessage("finished");
        });
    }
}
