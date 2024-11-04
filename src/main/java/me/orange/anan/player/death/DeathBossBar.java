package me.orange.anan.player.death;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import me.orange.anan.player.PlayerData;
import me.orange.anan.player.PlayerDataManager;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@InjectableComponent
public class DeathBossBar {
    private final PlayerDataManager playerDataManager;

    public DeathBossBar(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    public void showBossBar(Player player) {
        MCPlayer target = MCPlayer.from(player);
        PlayerData playerData = playerDataManager.getPlayerData(player);
        final BossBar bossBar = BossBar.bossBar(Component.text(""), 1, BossBar.Color.BLUE, BossBar.Overlay.NOTCHED_20);

        playerData.setBossBar(bossBar);
        playerData.setBossBarActive(true);
        playerData.setLastDeathLocation(player.getLocation());
        target.showBossBar(bossBar);
    }

    public void hideBossBar(Player player) {
        MCPlayer target = MCPlayer.from(player);
        BossBar bossBar = playerDataManager.getPlayerData(player).getBossBar();
        playerDataManager.getPlayerData(player).setBossBarActive(false);
        target.hideBossBar(bossBar);
    }

    public void updateBossBar(Player player) {
        PlayerData playerData = playerDataManager.getPlayerData(player);

        BossBar bossBar = playerData.getBossBar();
        float yawDifference = getYawDifference(player, playerData);

        int bossBarLength = 70;
        TextComponent.Builder barTextBuilder = Component.text();
        boolean isInFOV = yawDifference > -45 && yawDifference < 45;

        for (int i = 0; i < bossBarLength; i++) {
            if (isInFOV && i == (int) ((yawDifference + 45) / 90.0 * bossBarLength)) {
                barTextBuilder.append(Component.text('✖', NamedTextColor.RED, TextDecoration.BOLD));
            } else {
                barTextBuilder.append(Component.text('-', NamedTextColor.WHITE, TextDecoration.STRIKETHROUGH));
            }
        }

        bossBar.name(barTextBuilder.build());
    }

    private static float getYawDifference(Player player, PlayerData playerData) {
        Location lastDeathLocation = playerData.getLastDeathLocation();
        Location playerLocation = player.getLocation();

        double dx = lastDeathLocation.getX() - playerLocation.getX();
        double dz = lastDeathLocation.getZ() - playerLocation.getZ();
        double targetYaw = Math.toDegrees(Math.atan2(dz, dx)) - 90;
        float playerYaw = (playerLocation.getYaw() + 360) % 360;
        float yawDifference = (float) ((targetYaw + 360) % 360 - playerYaw + 360) % 360;

        if (yawDifference > 180) {
            yawDifference -= 360;
        }
        return yawDifference;
    }
}
