package me.orange.anan.player;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.entity.Player;

@InjectableComponent
@Command(value = {"player", "p"})
public class PlayerCommand extends BaseCommand {
    private final PlayerDataManager playerDataManager;
    private final PlayerStatsMenu playerStatsMenu;

    public PlayerCommand(PlayerDataManager playerDataManager, PlayerStatsMenu playerStatsMenu) {
        this.playerDataManager = playerDataManager;
        this.playerStatsMenu = playerStatsMenu;
    }

    @Command("menu")
    public void openMenu(BukkitCommandContext ctx, @Arg("name") Player player) {
        playerStatsMenu.open(ctx.getPlayer(), player);
    }

    @Command("kills")
    public void showKills(BukkitCommandContext ctx) {
        int killCount = playerDataManager.getPlayerData(ctx.getPlayer()).getKills();
        ctx.getPlayer().sendMessage(killCount + "");
    }

    @Command("deaths")
    public void showDeaths(BukkitCommandContext ctx) {
        int deathCount = playerDataManager.getPlayerData(ctx.getPlayer()).getDeaths();
        ctx.getPlayer().sendMessage(deathCount + "");
    }
}
