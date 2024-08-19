package me.orange.anan;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostDestroy;
import io.fairyproject.log.Log;
import me.orange.anan.blocks.BlockStatsManager;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.craft.behaviour.teamCore.TeamCoreManager;
import me.orange.anan.player.PlayerDataManager;
import me.orange.anan.player.bed.BedManager;

@InjectableComponent
public class ConfigSaver {
    private final BlockStatsManager blockStatsManager;
    private final ClanManager clanManager;
    private final PlayerDataManager playerDataManager;
    private final BedManager bedManager;
    private final TeamCoreManager teamCoreManager;

    public ConfigSaver(BlockStatsManager blockStatsManager, ClanManager clanManager, PlayerDataManager playerDataManager, BedManager bedManager, TeamCoreManager teamCoreManager) {
        this.blockStatsManager = blockStatsManager;
        this.clanManager = clanManager;
        this.playerDataManager = playerDataManager;
        this.bedManager = bedManager;
        this.teamCoreManager = teamCoreManager;
    }

    @PostDestroy
    public void onPreDestroy() {
        bedManager.saveConfig();
        blockStatsManager.saveConfig();
        clanManager.saveConfig();
        playerDataManager.saveConfig();
        teamCoreManager.saveConfig();
        Log.info("Configs saved");
    }
}
