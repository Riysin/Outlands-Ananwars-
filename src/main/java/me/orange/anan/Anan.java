package me.orange.anan;

import io.fairyproject.FairyLaunch;
import io.fairyproject.log.Log;
import io.fairyproject.plugin.Plugin;
import me.orange.anan.craft.behaviour.teamCore.TeamCoreManager;
import me.orange.anan.player.PlayerDataManager;

@FairyLaunch
public class Anan extends Plugin {
    @Override
    public void onPluginEnable() {
        Log.info("Toe Click Enabled.");
    }

    @Override
    public void onPluginDisable() {
        Log.info("Toe Click Disabled.");
    }

}
