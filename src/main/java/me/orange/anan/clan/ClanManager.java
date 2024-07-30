package me.orange.anan.clan;

import com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest;
import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@InjectableComponent
public class ClanManager {
    private Map<String, Clan> clanMap = new HashMap<>();

    public Map<String, Clan> getClanMap() {
        return clanMap;
    }

    public Clan getClanByTeamName(String teamname) {
        return clanMap.get(teamname);
    }

    public void setClanMap(String teamname, Clan clan) {
        clanMap.put(teamname, clan);
    }

    public boolean hasClan(String teamname) {
        return clanMap.containsKey(teamname);
    }

    public Clan getPlayerClan(Player player) {
        AtomicReference<Clan> clan = new AtomicReference<>(null);
        clanMap.forEach((k, v) -> {
            if (v.getPlayers().contains(player.getUniqueId())) {
                clan.set(v);
            }
        });
        return clan.get();
    }

    public Clan getPlayerClan(UUID uuid) {
        AtomicReference<Clan> clan = new AtomicReference<>(null);
        clanMap.forEach((k, v) -> {
            if (v.getPlayers().contains(uuid)) {
                clan.set(v);
            }
        });
        return clan.get();
    }

    public boolean inClan(Player player) {
        AtomicReference<Boolean> inClan = new AtomicReference<>(false);
        clanMap.forEach((k, v) -> {
            if (v.getPlayers().contains(player.getUniqueId())) {
                inClan.set(true);
            }
        });
        return inClan.get();
    }

    public boolean inClan(UUID uuid) {
        AtomicReference<Boolean> inClan = new AtomicReference<>(false);
        clanMap.forEach((k, v) -> {
            if (v.getPlayers().contains(uuid)) {
                inClan.set(true);
            }
        });
        return inClan.get();
    }


    public boolean sameClan(Player player1, Player player2) {
        if (getPlayerClan(player1) == null || getPlayerClan(player2) == null) {
            return false;
        }
        return getPlayerClan(player1) == getPlayerClan(player2);
    }

    public boolean isOwner(Player player) {
        return getPlayerClan(player).getOwnerUUID() == player.getUniqueId();
    }

    public String getOwnerName(Player player) {
        if (inClan(player)) {
            if (getPlayerClan(player).getOwner() != null) {
                return getPlayerClan(player).getOwner().getName();
            } else {
                return Bukkit.getOfflinePlayer(getPlayerClan(player).getOwnerUUID()).getName();
            }
        } else {
            return "§cYou do no have a clan yet!";
        }
    }

    public void sendClanOwner(Player player, String message) {
        if (Bukkit.getOnlinePlayers().contains(getPlayerClan(player).getOwner()))
            getPlayerClan(player).getOwner().sendMessage(message);
    }

    public void sendOnlineClanPlayer(Player player, String message) {
        getPlayerClan(player).getOnlineBukkitPlayers().forEach(p -> {
            p.sendMessage(message);
        });
    }

    public boolean hasInvitation(Player ctx, Player player) {
        if (getPlayerClan(ctx) == null) {
            return false;
        } else {
            return getPlayerClan(ctx).getInvitations().contains(player.getUniqueId());
        }
    }

    public void addInvitation(Player ctx, Player player) {
        getPlayerClan(ctx).getInvitations().add(player.getUniqueId());
    }

    public String getClanName(Player player) {
        return getPlayerClan(player).getDisplayName();
    }

    public void clanPlayerEvent(Player player, Event event) {
        getPlayerClan(player).getPlayers().forEach(uuid -> {
            Player clanPlayer = Bukkit.getPlayer(uuid);
            if (clanPlayer != null) {
                Bukkit.getPluginManager().callEvent(event);
            }
        });
    }

}
