package me.orange.anan.player;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.tablist.util.Skin;
import me.orange.anan.job.Job;
import me.orange.anan.player.config.FriendElement;
import me.orange.anan.player.config.PlayerConfig;
import me.orange.anan.player.config.PlayerConfigElement;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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

            playerDataMap.put(UUID.fromString(uuid), playerData);
        });
    }

    public void saveConfig() {
        playerDataMap.forEach((uuid, playerData) -> {
            PlayerConfigElement element = playerConfig.getPlayerElementMap().get(uuid.toString());
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

    public boolean checkKnocked(Player player) {
        return getPlayerData(player).isKnocked();
    }

    public void playerKnocked(Player player) {
        getPlayerData(player).setKnocked(true);

        player.setHealth(10);
        player.setSneaking(true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 5, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 250));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 999999, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 999999, 250));
        player.getWorld().spigot().playEffect(player.getLocation().clone().add(0, 1, 0), Effect.STEP_SOUND, Material.REDSTONE_BLOCK.getId(), 0, 0.25f, 0.25f, 0.25f, 0, 30, 32);
    }

    public void playerSaved(Player player) {
        getPlayerData(player).setKnocked(false);
        player.setSneaking(false);
        player.removePotionEffect(PotionEffectType.SLOW);
        player.removePotionEffect(PotionEffectType.JUMP);
        player.removePotionEffect(PotionEffectType.WITHER);
        player.removePotionEffect(PotionEffectType.WEAKNESS);
    }

    public boolean checkSaving(Player player) {
        return getPlayerData(player).isSaving();
    }

    public void setSavingStats(Player player1, Player player2, Boolean bool) {
        getPlayerData(player1).setSaving(bool);
        getPlayerData(player2).setSaving(bool);
    }

    public void savingFailed(Player player1, Player player2) {
        player1.sendTitle("", "§esaving failed");
        player2.sendTitle("", "§esaving failed");
        setSavingStats(player1, player2, false);
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

    public void addFriendRequest(Player player, OfflinePlayer friend) {
        getPlayerData(player).getFriendRequests().add(friend.getUniqueId());
    }

    public boolean hasInvitation(Player player, OfflinePlayer friend) {
        return getPlayerData(player).getFriendRequests().contains(friend.getUniqueId());
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
        if (level >= 10) lore.add("§aUnlocked");
        else lore.add("§cLocked");
        lore.add("");
        lore.add("§6" + job.getSkill2Name());
        lore.add("§7" + job.getSkill2Description());
        if (level >= 20) lore.add("§aUnlocked");
        else lore.add("§cLocked");
        lore.add("");
        lore.add("§6" + job.getSkill3Name());
        lore.add("§7" + job.getSkill3Description());
        if (level >= 30) lore.add("§aUnlocked");
        else lore.add("§cLocked");
        lore.add("");
        lore.add("§6" + job.getActiveName());
        lore.add("§7" + job.getActiveDescription());
        if (level == 35) lore.add("§aUnlocked");
        else lore.add("§cLocked");

        return lore;
    }
}
