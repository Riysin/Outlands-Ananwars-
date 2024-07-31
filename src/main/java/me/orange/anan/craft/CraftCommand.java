package me.orange.anan.craft;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.hologram.Hologram;
import io.fairyproject.mc.hologram.line.HologramLine;
import net.kyori.adventure.text.Component;

import java.util.Arrays;

@InjectableComponent
@Command(value = "craft")
public class CraftCommand extends BaseCommand {
    private final CraftMenu craftMenu;
    private final OngoingCraftMenu ongoingCraftMenu;

    public CraftCommand(CraftMenu craftMenu, OngoingCraftMenu ongoingCraftMenu) {
        this.craftMenu = craftMenu;
        this.ongoingCraftMenu = ongoingCraftMenu;
    }

    @Command("menu")
    public void openCraftMenu(BukkitCommandContext ctx) {
        craftMenu.open(ctx.getPlayer());
    }

    @Command("ongoing")
    public void openOngoingCraftMenu(BukkitCommandContext ctx){
        ongoingCraftMenu.open(ctx.getPlayer());
    }
}
