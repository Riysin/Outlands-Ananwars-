package me.orange.anan.player;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.nametag.NameTag;
import io.fairyproject.mc.nametag.NameTagAdapter;
import me.orange.anan.clan.Clan;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.clan.NametagVisibility;
import me.orange.anan.player.job.JobManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

@InjectableComponent
public class PlayerNameTag extends NameTagAdapter {
    private final ClanManager clanManager;
    private final JobManager jobManager;

    public PlayerNameTag(ClanManager clanManager, JobManager jobManager) {
        super("nametag", 0);
        this.clanManager = clanManager;
        this.jobManager = jobManager;
    }

    @Override
    public NameTag fetch(MCPlayer player, MCPlayer target) {
        Player bukkitTarget = target.as(Player.class);//頭上
        Player bukkitPlayer = player.as(Player.class);//看的人
        Clan targetClan = clanManager.getPlayerClan(bukkitTarget);
        Clan playerClan = clanManager.getPlayerClan(bukkitPlayer);
        NameTag noJob = new NameTag(Component.empty(), Component.empty(), TextColor.color(255, 255, 255), WrapperPlayServerTeams.NameTagVisibility.ALWAYS);

        //output
        if (jobManager.hasCurrentJob(bukkitTarget)) {
            String jobSuffix = jobManager.geCurrentJob(bukkitTarget).getSuffix();

            Component prefix = Component.text("");
            Component suffix = Component.text(jobSuffix);
            TextColor color = TextColor.color(255, 255, 255);
            WrapperPlayServerTeams.NameTagVisibility nameTagVisibility = getNameTagVisibility(playerClan, targetClan);

            return new NameTag(prefix, suffix, color, nameTagVisibility);
        }
        return noJob;
    }

    private static WrapperPlayServerTeams.NameTagVisibility getNameTagVisibility(Clan playerClan, Clan targetClan) {
        WrapperPlayServerTeams.NameTagVisibility nameTagVisibility = WrapperPlayServerTeams.NameTagVisibility.ALWAYS;
        //nametag visibility option
        if (playerClan == null || targetClan == null) return nameTagVisibility;

        if (playerClan == targetClan && targetClan.getNametagVisibility() == NametagVisibility.hideForOwnTeams)
            nameTagVisibility = WrapperPlayServerTeams.NameTagVisibility.NEVER;
        else if (playerClan != targetClan && targetClan.getNametagVisibility() == NametagVisibility.hideForOtherTeams)
            nameTagVisibility = WrapperPlayServerTeams.NameTagVisibility.NEVER;
        return nameTagVisibility;
    }
}
