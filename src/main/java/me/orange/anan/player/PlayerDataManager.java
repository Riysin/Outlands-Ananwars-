package me.orange.anan.player;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.nametag.NameTagService;
import io.fairyproject.mc.tablist.util.Skin;
import me.orange.anan.clan.Clan;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.player.config.PlayerConfig;
import me.orange.anan.player.config.PlayerConfigElement;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@InjectableComponent
public class PlayerDataManager {
    private final PlayerConfig playerConfig;
    private Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public PlayerDataManager(PlayerConfig playerConfig) {
        this.playerConfig = playerConfig;

        loadConfig();
    }

    public void loadConfig() {
        playerConfig.getPlayerElementMap().forEach((playerName, playerElement) -> {
            UUID uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();
            PlayerData playerData = new PlayerData();
            playerData.setKills(playerElement.getKills());
            playerData.setDeaths(playerElement.getDeaths());
            playerData.setLastDeathLocation(playerElement.getLastDeathLocation());
            playerData.setBossBarActive(playerElement.isBossBarActive());

            playerDataMap.put(uuid, playerData);
        });
    }

    public void saveToConfig(Player player) {
        PlayerConfigElement playerConfigElement = getPlayerConfigElement(player);
        PlayerData playerData = getPlayerData(player);

        playerConfigElement.setKills(playerData.getKills());
        playerConfigElement.setDeaths(playerData.getDeaths());
        playerConfigElement.setLastDeathLocation(playerData.getLastDeathLocation());
        playerConfigElement.setBossBarActive(playerData.isBossBarActive());

        playerConfig.save();
    }

    public void saveConfig(){
        playerConfig.getPlayerElementMap().forEach((playerName, playerElement) -> {
            UUID uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();
            PlayerData playerData = playerDataMap.get(uuid);
            playerElement.setKills(playerData.getKills());
            playerElement.setDeaths(playerData.getDeaths());
            playerElement.setLastDeathLocation(playerData.getLastDeathLocation());
            playerElement.setBossBarActive(playerData.isBossBarActive());
        });
    }

    public Map<UUID, PlayerData> getPlayerDataMap() {
        return playerDataMap;
    }

    public void setPlayerDataMap(Map<UUID, PlayerData> playerDataMap) {
        this.playerDataMap = playerDataMap;
    }

    public PlayerData getPlayerData(Player player) {
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
        event.setJoinMessage(player.getName() + "§3 hi");

        if (!playerDataMap.containsKey(uuid)) {
            playerDataMap.put(uuid, new PlayerData());
            playerConfig.addPlayer(player);
        }
        PlayerData playerData = playerDataMap.get(uuid);
        playerData.setSkin(Skin.load(uuid));
    }

    public PlayerConfigElement getPlayerConfigElement(Player player) {
        return playerConfig.getPlayerElementMap().get(player.getUniqueId().toString());
    }
}
