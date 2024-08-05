package me.orange.anan.player;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.nametag.NameTagService;
import io.fairyproject.mc.tablist.util.Skin;
import me.orange.anan.clan.Clan;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.player.config.PlayerConfig;
import org.bukkit.Bukkit;
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
    private NameTagService nameTagService;
    private Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public PlayerDataManager(PlayerConfig playerConfig) {
        this.playerConfig = playerConfig;

        loadConfig();
    }

    public void loadConfig(){
        playerConfig.getPlayerElementMap().forEach((playerName, playerElement) -> {
            UUID uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();
            PlayerData playerData = new PlayerData();
            playerData.setKills(playerElement.getKills());
            playerData.setDeaths(playerElement.getDeaths());

            playerDataMap.put(uuid, playerData);
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
    }

    public void playerSaved(Player player) {
        getPlayerData(player).setKnocked(false);
        player.setSneaking(false);
        player.removePotionEffect(PotionEffectType.SLOW);
        player.removePotionEffect(PotionEffectType.JUMP);
        player.removePotionEffect(PotionEffectType.WITHER);
    }

    public boolean checkSaving(Player player) {
        return getPlayerData(player).isSaving();
    }

    public void setSavingStats(Player player1, Player player2, Boolean bool) {
        getPlayerData(player1).setSaving(bool);
        getPlayerData(player2).setSaving(bool);
    }

    public void addCanCraft(Player player, String ID){
        getPlayerData(player).getCanCraftItems().add(ID);
    }

    public void setUpPlayer(PlayerJoinEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        event.setJoinMessage(player.getName() + "§3 hi");

        if (!playerDataMap.containsKey(uuid)) {
            playerDataMap.put(uuid, new PlayerData());
            playerConfig.addPlayer(player.getName());
        }
        PlayerData playerData = playerDataMap.get(uuid);
        playerData.setSkin(Skin.load(uuid));
    }
}
