package me.orange.anan.clan;

import io.fairyproject.container.InjectableComponent;
import me.orange.anan.clan.config.ClanConfig;
import me.orange.anan.clan.config.ClanConfigElement;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

@InjectableComponent
public class ClanManager {
    private final Map<String, Clan> clanMap = new HashMap<>();
    private final ClanConfig clanConfig;

    public ClanManager(ClanConfig clanConfig) {
        this.clanConfig = clanConfig;

        loadConfig();
    }

    public void loadConfig() {
        clanConfig.getClanElementMap().forEach((clanName, element) -> {
            Clan clan = new Clan(clanName);
            clan.setOwner(element.getOwner());
            clan.setPlayers(element.getPlayers());
            clan.setDisplayName(clanName);
            clan.setPrefix(clanName);
            clanMap.put(clanName, clan);
        });
    }

    public void saveConfig() {
        clanConfig.getClanElementMap().clear();
        clanMap.forEach((clanName, clan) -> {
            ClanConfigElement element = new ClanConfigElement();
            element.setPlayers(clan.getPlayers());
            element.setOwner(clan.getOwnerUUID());
            element.setClanName(clanName);
            clanConfig.getClanElementMap().put(clanName, element);
        });
        clanConfig.save();
    }

    public Map<String, Clan> getClanMap() {
        return clanMap;
    }

    public void createClan(String name, Player player) {
        Clan clan = new Clan(name);
        clan.setOwner(player);
        clan.addPlayer(player);
        clan.setPrefix("§2[" + name + "]§f ");
        clan.setSuffix("");
        clanMap.put(name, clan);
    }

    public void removeClan(Player player) {
        String clanName = getClanName(player);
        if (clanName != null) {
            clanMap.remove(clanName);
        }
    }

    public void removePlayerFromClan(Player player) {
        Clan clan = getPlayerClan(player);
        if (clan != null) {
            clan.getPlayers().remove(player.getUniqueId());
        }
    }


    public Clan getPlayerClan(Player player) {
        return getPlayerClan(player.getUniqueId());
    }

    public Clan getPlayerClan(UUID uuid) {
        return clanMap.values().stream()
                .filter(clan -> clan.getPlayers().contains(uuid))
                .findFirst().orElse(null);
    }

    public boolean inClan(Player player) {
        return inClan(player.getUniqueId());
    }

    public boolean inClan(UUID uuid) {
        return clanMap.values().stream().anyMatch(clan -> clan.getPlayers().contains(uuid));
    }

    public boolean sameClan(OfflinePlayer player1, OfflinePlayer player2) {
        if(player1.equals(player2)) return true;

        Clan clan1 = getPlayerClan(player1.getUniqueId());
        Clan clan2 = getPlayerClan(player2.getUniqueId());
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

    public boolean hasInvitation(Player ctx, Player player) {
        Clan clan = getPlayerClan(ctx);
        return clan != null && clan.getInvitations().contains(player.getUniqueId());
    }

    public String getClanName(Player player) {
        Clan clan = getPlayerClan(player);
        return clan != null ? clan.getDisplayName() : null;
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
}
