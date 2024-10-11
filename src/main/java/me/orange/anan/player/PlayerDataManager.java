package me.orange.anan.player;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.tablist.util.Skin;
import me.orange.anan.player.config.TaskElement;
import me.orange.anan.player.job.Job;
import me.orange.anan.player.config.FriendElement;
import me.orange.anan.player.config.PlayerConfig;
import me.orange.anan.player.config.PlayerConfigElement;
import me.orange.anan.player.task.Task;
import me.orange.anan.player.task.TaskManager;
import me.orange.anan.player.task.TaskRegistry;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;
import java.util.List;

@InjectableComponent
public class PlayerDataManager {
    private final PlayerConfig playerConfig;
    private Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public PlayerDataManager(PlayerConfig playerConfig) {
        this.playerConfig = playerConfig;

        loadConfig();
    }

    public void loadConfig() {
        playerConfig.getPlayerElementMap().forEach((uuid, playerConfigElement) -> {
            PlayerData playerData = new PlayerData();
            playerData.setKills(playerConfigElement.getKills());
            playerData.setDeaths(playerConfigElement.getDeaths());
            playerData.setLastDeathLocation(playerConfigElement.getLastDeathLocation());
            playerData.setBossBarActive(playerConfigElement.isBossBarActive());
            playerConfigElement.getFriendList().forEach(friendElement -> {
                playerData.getFriends().add(friendElement.getUuid());
            });
            playerConfigElement.getTaskElementMap().forEach((taskID, taskElement) -> {
                Task task = TaskRegistry.create(taskID);
                task.setStatus(taskElement.getStatus());
                task.setProgress(taskElement.getProgress());
                playerData.getTasks().add(task);
            });

            playerDataMap.put(UUID.fromString(uuid), playerData);
        });
    }

    public void saveConfig() {
        playerDataMap.forEach((uuid, playerData) -> {
            PlayerConfigElement element = playerConfig.getPlayerConfigElement(uuid);
            element.setKills(playerData.getKills());
            element.setDeaths(playerData.getDeaths());
            element.setLastDeathLocation(playerData.getLastDeathLocation());
            element.setBossBarActive(playerData.isBossBarActive());

            element.getFriendList().clear();
            playerData.getFriends().forEach(friend -> {
                FriendElement friendElement = new FriendElement();
                friendElement.setUuid(friend);
                element.getFriendList().add(friendElement);
            });

            element.getTaskElementMap().clear();
            playerData.getTasks().forEach(task -> {
                TaskElement taskElement = new TaskElement();
                taskElement.setStatus(task.getStatus());
                taskElement.setProgress(task.getProgress());
                element.getTaskElementMap().put(task.getId(), taskElement);
            });
        });
        playerConfig.save();
    }

    public Map<UUID, PlayerData> getPlayerDataMap() {
        return playerDataMap;
    }

    public void setPlayerDataMap(Map<UUID, PlayerData> playerDataMap) {
        this.playerDataMap = playerDataMap;
    }

    public PlayerData getPlayerData(OfflinePlayer player) {
        return playerDataMap.get(player.getUniqueId());
    }

    public PlayerData getPlayerData(UUID uuid) {
        return playerDataMap.get(uuid);
    }


    public void addCanCraft(Player player, String ID) {
        getPlayerData(player).getCanCraftItems().add(ID);
    }

    public void setUpPlayer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!playerDataMap.containsKey(uuid)) {
            playerDataMap.put(uuid, new PlayerData());
        }
        PlayerData playerData = playerDataMap.get(uuid);
        playerData.setSkin(Skin.load(uuid));
    }

    //Friend System
    public List<UUID> getFriends(Player player) {
        return getPlayerData(player).getFriends();
    }

    public boolean isFriend(Player player, OfflinePlayer friend) {
        return getPlayerData(player).getFriends().contains(friend.getUniqueId());
    }

    public void removeFriend(Player player, OfflinePlayer friend) {
        getPlayerData(player).getFriends().remove(friend.getUniqueId());
        getPlayerData(friend).getFriends().remove(player.getUniqueId());
    }

    public void addFriendRequest(Player invitor, OfflinePlayer friend) {
        getPlayerData(invitor).getFriendRequests().add(friend.getUniqueId());
    }

    public boolean hasInvitation(Player invitor, OfflinePlayer friend) {
        return getPlayerData(invitor).getFriendRequests().contains(friend.getUniqueId());
    }

    public void acceptFriendRequest(Player player, OfflinePlayer friend) {
        getPlayerData(player).getFriends().add(friend.getUniqueId());
        getPlayerData(friend).getFriends().add(player.getUniqueId());
        getPlayerData(player).getFriendRequests().remove(friend.getUniqueId());
    }

    public void denyFriendRequest(Player player, OfflinePlayer friend) {
        getPlayerData(player).getFriendRequests().remove(friend.getUniqueId());
    }

    public List<String> getJobStatsLore(int level, Job job) {
        List<String> lore = new ArrayList<>();
        lore.add("§6" + job.getUpgradeName());
        lore.add("§7" + job.getUpgradeDescription());
        lore.add("§fChance: §a" + job.getChancePerLevel() * level + "§f%");
        lore.add("");
        lore.add("§6" + job.getSkill1Name());
        lore.add("§7" + job.getSkill1Description());
        lore.add(level >= 10 ? "§aUnlocked" : "§cLocked");
        lore.add("");
        lore.add("§6" + job.getSkill2Name());
        lore.add("§7" + job.getSkill2Description());
        lore.add(level >= 20 ? "§aUnlocked" : "§cLocked");
        lore.add("");
        lore.add("§6" + job.getSkill3Name());
        lore.add("§7" + job.getSkill3Description());
        lore.add(level >= 30 ? "§aUnlocked" : "§cLocked");
        lore.add("");
        lore.add("§6" + job.getActiveName());
        lore.add("§7" + job.getActiveDescription());
        lore.add(level >= 40 ? "§aUnlocked" : "§cLocked");

        return lore;
    }
}
