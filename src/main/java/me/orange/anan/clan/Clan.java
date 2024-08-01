package me.orange.anan.clan;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Clan {
    private String displayName = null;
    private List<UUID> players = new ArrayList<>();
    private UUID owner;
    private List<UUID> invitations = new ArrayList<>();
    private String prefix = "";
    private String suffix = "";
    private boolean friendlyFire = true;
    private boolean privateChat = false;
    private ChatColor color = ChatColor.RESET;
    private NametagVisibility nametagVisibility = NametagVisibility.always;
    private boolean showNameTagToClicker = false;

    public NametagVisibility getNametagVisibility() {
        return nametagVisibility;
    }

    public void setNametagVisibility(NametagVisibility nametagVisibility) {
        this.nametagVisibility = nametagVisibility;
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public List<UUID> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<UUID> invitations) {
        this.invitations = invitations;
    }

    public Player getOwner() {
        return Bukkit.getPlayer(owner);
    }

    public UUID getOwnerUUID() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public Clan(String displayName) {
        this.displayName = displayName;
    }

    public boolean isShowNameTagToClicker() {
        return showNameTagToClicker;
    }

    public void setShowNameTagToClicker(boolean showNameTagToClicker) {
        this.showNameTagToClicker = showNameTagToClicker;
    }

    public boolean isPrivateChat() {
        return privateChat;
    }

    public void setPrivateChat(boolean privateChat) {
        this.privateChat = privateChat;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public List<UUID> getOnlinePlayers() {
        List<UUID> players = new ArrayList<>();
        this.players.forEach(uuid -> {
            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(uuid)))
                players.add(uuid);
        });
        return players;
    }

    public List<Player> getOnlineBukkitPlayers() {
        List<Player> players = new ArrayList<>();
        this.players.forEach(uuid -> {
            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(uuid)))
                players.add(Bukkit.getPlayer(uuid));
        });
        return players;
    }

    public List<Player> getBukkitPlayers() {
        List<Player> players = new ArrayList<>();
        this.players.forEach(uuid -> {
            players.add(Bukkit.getPlayer(uuid));
        });
        return players;
    }

    public List<OfflinePlayer> getOfflineBukkitPlayers() {
        List<OfflinePlayer> players = new ArrayList<>();
        for (UUID clanPlayer : this.players) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(clanPlayer);
            if (offlinePlayer != null && !offlinePlayer.isOnline()) {
                players.add(offlinePlayer);
            }
        }
        return players;
    }

    public void setPlayers(List<UUID> players) {
        this.players = players;
    }

    public void addPlayer(Player player) {
        players.add(player.getUniqueId());
    }

    public void addPlayer(UUID uuid) {
        players.add(uuid);
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public boolean isFriendlyFire() {
        return friendlyFire;
    }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

}
