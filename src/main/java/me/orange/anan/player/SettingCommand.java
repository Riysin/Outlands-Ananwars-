package me.orange.anan.player;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;

@InjectableComponent
@Command(value = {"settings", "s"})
public class SettingCommand extends BaseCommand {
    private final PlayerSettingsMenu playerSettingsMenu;

    public SettingCommand(PlayerSettingsMenu playerSettingsMenu) {
        this.playerSettingsMenu = playerSettingsMenu;
    }

    @Command("#")
    public void openMenu(BukkitCommandContext ctx) {
        playerSettingsMenu.open(ctx.getPlayer());
    }
}
