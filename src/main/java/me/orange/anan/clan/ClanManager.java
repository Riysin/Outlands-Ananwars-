package me.orange.anan.clan;

import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.clan.config.ClanConfig;
import me.orange.anan.clan.config.ClanConfigElement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@InjectableComponent
public class ClanManager {
    private Map<String, Clan> clanMap = new HashMap<>();
    private final ClanConfig clanConfig;

    public ClanManager(ClanConfig clanConfig) {
        this.clanConfig = clanConfig;

        loadClan();
    }

    public void loadClan() {
        clanConfig.getClanElementMap().forEach((clanName, element) -> {
            Clan clan = new Clan(clanName);

            clan.setPlayers(element.getPlayers());
            clan.setDisplayName(clanName);
            clan.setOwner(element.getOwner());
            clan.setPrefix("§2[" + clanName + "]§r ");
            clan.setSuffix("");

            clanMap.put(clanName, clan);
        });
    }

    public void createClan(String name, Player player) {
        clanConfig.addClan(name, player);
        Clan clan = new Clan(name);

        updateClan(clan);
        clanMap.put(name, clan);
    }

    public void removeClan(Player player){
        clanConfig.removeClan(getClanName(player));
        updateClan(getPlayerClan(player));
    }

    public void updateClan(Clan clan) {
        String clanName = clan.getDisplayName();
        ClanConfigElement element = clanConfig.getClanElementMap().get(clanName);

        clan.setPlayers(element.getPlayers());
        clan.setDisplayName(clanName);
        clan.setOwner(element.getOwner());
        clan.setPrefix("§2[" + clanName + "]§r ");
        clan.setSuffix("");
    }

    public void addPlayerToClan(Player clanPlayer, Player player) {
        getPlayerClanConfigElement(clanPlayer).addPlayer(player);
        updateClan(getPlayerClan(clanPlayer));
    }

    public void removePlayerFromClan(Player player) {
        getPlayerClanConfigElement(player).removePlayer(player);
        updateClan(getPlayerClan(player));
    }

    public Map<String, Clan> getClanMap() {
        return clanMap;
    }

    public Clan getClanByTeamName(String teamname) {
        return clanMap.get(teamname);
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

    public ClanConfigElement getPlayerClanConfigElement(Player player) {
        return clanConfig.getClanElementMap().get(getPlayerClan(player).getDisplayName());
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
        Clan clan1 = getPlayerClan(player1);
        Clan clan2 = getPlayerClan(player2);

        if (clan1 == null || clan2 == null) {
            return false;
        }

        return clan1.getDisplayName().equals(clan2.getDisplayName());
    }

    public boolean isOwner(Player player) {
        return getPlayerClan(player).getOwnerUUID().equals(player.getUniqueId());
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

    public void transferOwner(Player player){
        getPlayerClanConfigElement(player).setOwner(player.getUniqueId());
        updateClan(getPlayerClan(player));
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
