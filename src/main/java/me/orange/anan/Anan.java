package me.orange.anan;

import io.fairyproject.FairyLaunch;
import io.fairyproject.log.Log;
import io.fairyproject.plugin.Plugin;

@FairyLaunch
public class Anan extends Plugin {

    @Override
    public void onPluginEnable() {
        Log.info("Toe Click Enabled.");
    }

}
