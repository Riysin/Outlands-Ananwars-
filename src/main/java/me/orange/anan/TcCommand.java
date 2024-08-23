package me.orange.anan;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.nametag.NameTagService;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import io.fairyproject.scheduler.response.TaskResponse;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.blocks.config.BlockConfig;
import me.orange.anan.blocks.config.BuildConfig;
import me.orange.anan.clan.config.ClanConfig;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.blocks.config.NatureBlockConfig;
import me.orange.anan.craft.behaviour.teamCore.config.TeamCoreConfig;
import me.orange.anan.player.config.PlayerConfig;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@InjectableComponent
@Command(value = "tc", permissionNode = "tc.admin")
public class TcCommand extends BaseCommand {
    private final PlayerConfig playerConfig;
    private final ClanConfig clanConfig;
    private final NatureBlockConfig natureBlockConfig;
    private final CraftManager craftManager;
    private final BuildConfig buildConfig;
    private final BlockConfig blockConfig;
    private final TeamCoreConfig teamCoreConfig;
    private final BlockStatsManager blockStatsManager;
    private final NameTagService nameTagService;

    public TcCommand(PlayerConfig playerConfig, ClanConfig clanConfig, NatureBlockConfig natureBlockConfig, CraftManager craftManager, BuildConfig buildConfig, BlockConfig blockConfig, TeamCoreConfig teamCoreConfig, BlockStatsManager blockStatsManager, NameTagService nameTagService) {
        this.clanConfig = clanConfig;
        this.playerConfig = playerConfig;
        this.natureBlockConfig = natureBlockConfig;
        this.craftManager = craftManager;
        this.buildConfig = buildConfig;
        this.blockConfig = blockConfig;
        this.teamCoreConfig = teamCoreConfig;
        this.blockStatsManager = blockStatsManager;
        this.nameTagService = nameTagService;
    }

    @Command("test")
    public void test(BukkitCommandContext ctx) {
        CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            if (ctx.getPlayer().getItemInHand() != null) {
                return TaskResponse.success("");
            }
            ctx.getPlayer().sendMessage("test");
            return TaskResponse.continueTask();
        }, 0, 20, RepeatPredicate.length(Duration.ofSeconds(5))).getFuture();

        future.thenRun(() -> {
            ctx.getPlayer().sendMessage("finished");
        });
    }

    @Command("reloadConfig")
    public void reloadConfig(BukkitCommandContext ctx) {
        craftManager.getCrafts().clear();

        craftManager.loadConfigFile();
        natureBlockConfig.loadAndSave();
        clanConfig.loadAndSave();
        playerConfig.loadAndSave();
        buildConfig.loadAndSave();
        blockConfig.loadAndSave();
        teamCoreConfig.loadAndSave();

        ctx.getPlayer().sendMessage(ChatColor.GREEN + "Config reloaded");
    }

    @Command("deleteBlockData")
    public void deleteBlockData(BukkitCommandContext ctx) {
        blockStatsManager.getBlockStatsMap().clear();
        blockStatsManager.saveConfig();
        ctx.getPlayer().sendMessage(ChatColor.GREEN + "Block data deleted");
    }

    @Command("deleteTcData")
    public void deleteTcData(BukkitCommandContext ctx) {
        teamCoreConfig.getTeamCores().clear();
        teamCoreConfig.save();
        ctx.getPlayer().sendMessage(ChatColor.GREEN + "Team cores deleted");
    }

    @Command("head")
    public void givePlayerHead(BukkitCommandContext ctx, @Arg("name") Player player) {
        ctx.getPlayer().getInventory().addItem(ItemBuilder.of(XMaterial.PLAYER_HEAD).skull(player.getName()).build());
    }

    @Command("nametag")
    public void updateNameTag(BukkitCommandContext ctx) {
        nameTagService.update(MCPlayer.from(ctx.getPlayer()));
    }
}
