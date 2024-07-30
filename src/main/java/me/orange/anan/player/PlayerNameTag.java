package me.orange.anan.player;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.nametag.NameTag;
import io.fairyproject.mc.nametag.NameTagAdapter;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import me.orange.anan.clan.Clan;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.clan.NametagVisibility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@InjectableComponent
public class PlayerNameTag extends NameTagAdapter {
    private final ClanManager clanManager;

    public PlayerNameTag(ClanManager clanManager) {
        super("nametag", 0);
        this.clanManager = clanManager;
    }

    @Override
    public NameTag fetch(MCPlayer player, MCPlayer target) {
        Player bukkitTarget = target.as(Player.class);//頭上
        Player bukkitPlayer = player.as(Player.class);//看的人
        Clan targetClan = clanManager.getPlayerClan(bukkitTarget);
        Clan playerClan = clanManager.getPlayerClan(bukkitPlayer);
        NameTag noClan = new NameTag(Component.text("§7[NoTeam]§r "), Component.empty(), TextColor.color(255, 255, 255), WrapperPlayServerTeams.NameTagVisibility.ALWAYS);

        //output
        if (clanManager.inClan(bukkitTarget)) {
            Clan clan = clanManager.getPlayerClan(bukkitTarget);
            Component prefix = Component.text(clan.getPrefix());
            Component suffix = Component.text(clan.getSuffix());
            TextColor color = LegacyComponentSerializer.parseChar(clan.getColor().getChar()).color();
            WrapperPlayServerTeams.NameTagVisibility nameTagVisibility = getNameTagVisibility(playerClan, targetClan);
            return new NameTag(prefix, suffix, color, nameTagVisibility);
        }
        return noClan;
    }

    @NotNull
    private static WrapperPlayServerTeams.NameTagVisibility getNameTagVisibility(Clan playerClan, Clan targetClan) {
        WrapperPlayServerTeams.NameTagVisibility nameTagVisibility = WrapperPlayServerTeams.NameTagVisibility.ALWAYS;
        //nametag visibility option
        if (playerClan == targetClan && targetClan.getNametagVisibility() == NametagVisibility.hideForOwnTeams)
            nameTagVisibility = WrapperPlayServerTeams.NameTagVisibility.NEVER;
        else if (playerClan != targetClan && targetClan.getNametagVisibility() == NametagVisibility.hideForOtherTeams)
            nameTagVisibility = WrapperPlayServerTeams.NameTagVisibility.NEVER;
        return nameTagVisibility;
    }
}
