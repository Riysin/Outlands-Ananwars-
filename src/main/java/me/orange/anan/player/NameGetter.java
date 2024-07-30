package me.orange.anan.player;

import me.orange.anan.clan.Clan;
import me.orange.anan.clan.ClanManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class NameGetter {
    private final ClanManager clanManager;

    public NameGetter(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    public String getChatName(Player player) {
        if (clanManager.inClan(player)) {
            Clan clan= clanManager.getPlayerClan(player);
            return clan.getPrefix() + clan.getColor() + player.getName() + clan.getSuffix() + ChatColor.RESET;
        }
        return "NoTeam " + player.getName();
    }

    public String getNameWithTeamColor(Player player) {
        if (clanManager.inClan(player)) {
            Clan team = clanManager.getPlayerClan(player);
            return team.getColor() + player.getName() + ChatColor.RESET;
        }
        return player.getName();
    }
}
