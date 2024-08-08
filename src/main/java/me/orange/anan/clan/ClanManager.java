package me.orange.anan.clan;

import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.clan.config.ClanConfig;
import me.orange.anan.clan.config.ClanConfigElement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@InjectableComponent
public class ClanManager {
    private final Map<String, Clan> clanMap = new HashMap<>();
    private final ClanConfig clanConfig;

    public ClanManager(ClanConfig clanConfig) {
        this.clanConfig = clanConfig;
        loadClan();
    }

    public void loadClan() {
        clanConfig.getClanElementMap().forEach((clanName, element) -> {
            Clan clan = new Clan(clanName);
            updateClanFromElement(clan, element);
            clanMap.put(clanName, clan);
        });
    }

    public void createClan(String name, Player player) {
        clanConfig.addClan(name, player);
        Clan clan = new Clan(name);
        updateClan(clan);
        clanMap.put(name, clan);
    }

    public void removeClan(Player player) {
        String clanName = getClanName(player);
        if (clanName != null) {
            clanConfig.removeClan(clanName);
            clanMap.remove(clanName);
        }
    }

    public void updateClan(Clan clan) {
        String clanName = clan.getDisplayName();
        ClanConfigElement element = clanConfig.getClanElementMap().get(clanName);
        updateClanFromElement(clan, element);
    }

    private void updateClanFromElement(Clan clan, ClanConfigElement element) {
        clan.setPlayers(element.getPlayers());
        clan.setOwner(element.getOwner());
        clan.setPrefix("§2[" + clan.getDisplayName() + "]§r ");
        clan.setSuffix("");
    }

    public void addPlayerToClan(Player clanPlayer, Player player) {
        ClanConfigElement element = getPlayerClanConfigElement(clanPlayer);
        element.addPlayer(player);
        updateClan(getPlayerClan(clanPlayer));
        clanConfig.save();
    }

    public void removePlayerFromClan(Player player) {
        Clan clan = getPlayerClan(player);
        if (clan != null) {
            clan.getPlayers().remove(player.getUniqueId());
            getPlayerClanConfigElement(player).removePlayer(player);
            clanConfig.save();
        }
    }

    public Map<String, Clan> getClanMap() {
        return clanMap;
    }

    public boolean hasClan(String teamName) {
        return clanMap.containsKey(teamName);
    }

    public Clan getPlayerClan(Player player) {
        return getPlayerClan(player.getUniqueId());
    }

    public Clan getPlayerClan(UUID uuid) {
        return clanMap.values().stream()
                .filter(clan -> clan.getPlayers().contains(uuid))
                .findFirst().orElse(null);
    }

    public ClanConfigElement getPlayerClanConfigElement(Player player) {
        Clan clan = getPlayerClan(player);
        if (clan != null) {
            return clanConfig.getClanElementMap().get(clan.getDisplayName());
        }
        return null;
    }

    public boolean inClan(Player player) {
        return inClan(player.getUniqueId());
    }

    public boolean inClan(UUID uuid) {
        return clanMap.values().stream().anyMatch(clan -> clan.getPlayers().contains(uuid));
    }

    public boolean sameClan(Player player1, Player player2) {
        Clan clan1 = getPlayerClan(player1);
        Clan clan2 = getPlayerClan(player2);
        return clan1 != null && clan1.equals(clan2);
    }

    public boolean isOwner(Player player) {
        Clan clan = getPlayerClan(player);
        return clan != null && clan.getOwnerUUID().equals(player.getUniqueId());
    }

    public String getOwnerName(Player player) {
        if (inClan(player)) {
            Clan clan = getPlayerClan(player);
            if (clan != null) {
                Player owner = clan.getOwner();
                return owner != null ? owner.getName() : Bukkit.getOfflinePlayer(clan.getOwnerUUID()).getName();
            }
        }
        return "§cYou do not have a clan yet!";
    }

    public void transferOwner(Player player) {
        ClanConfigElement element = getPlayerClanConfigElement(player);
        if (element != null) {
            element.setOwner(player.getUniqueId());
            updateClan(getPlayerClan(player));
        }
    }

    public void sendClanOwner(Player player, String message) {
        Clan clan = getPlayerClan(player);
        if (clan != null) {
            Player owner = clan.getOwner();
            if (owner != null && owner.isOnline()) {
                owner.sendMessage(message);
            }
        }
    }

    public void sendOnlineClanPlayer(Player player, String message) {
        Clan clan = getPlayerClan(player);
        if (clan != null) {
            clan.getOnlineBukkitPlayers().forEach(p -> p.sendMessage(message));
        }
    }

    public boolean hasInvitation(Player ctx, Player player) {
        Clan clan = getPlayerClan(ctx);
        return clan != null && clan.getInvitations().contains(player.getUniqueId());
    }

    public void addInvitation(Player ctx, Player player) {
        Clan clan = getPlayerClan(ctx);
        if (clan != null) {
            clan.getInvitations().add(player.getUniqueId());
        }
    }

    public String getClanName(Player player) {
        Clan clan = getPlayerClan(player);
        return clan != null ? clan.getDisplayName() : null;
    }
}
