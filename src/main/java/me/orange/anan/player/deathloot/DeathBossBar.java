package me.orange.anan.player.deathloot;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

@InjectableComponent
public class DeathBossBar {
    private @Nullable BossBar activeBar;

    public void showMyBossBar(MCPlayer target) {
        final Component name = Component.text("Awesome BossBar");
        // Creates a red boss bar which has no progress and no notches
        final BossBar emptyBar = BossBar.bossBar(name, 0, BossBar.Color.RED, BossBar.Overlay.PROGRESS);
        // Creates a green boss bar which has 50% progress and 10 notches
        final BossBar halfBar = BossBar.bossBar(name, 0.5f, BossBar.Color.GREEN, BossBar.Overlay.NOTCHED_10);
        // etc..
        final BossBar fullBar = BossBar.bossBar(name, 1, BossBar.Color.BLUE, BossBar.Overlay.NOTCHED_20);

        // Send a bossbar to your audience
        target.showBossBar(fullBar);

        // Store it locally to be able to hide it manually later
        this.activeBar = fullBar;
    }

    public void hideActiveBossBar(MCPlayer target) {
        target.hideBossBar(this.activeBar);
        this.activeBar = null;
    }
}
