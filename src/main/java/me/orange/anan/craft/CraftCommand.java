package me.orange.anan.craft;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.bukkit.command.presence.DefaultPresenceProvider;
import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.command.annotation.CommandPresence;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.hologram.Hologram;
import io.fairyproject.mc.hologram.line.HologramLine;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@InjectableComponent
@Command(value = "craft")
@CommandPresence(DefaultPresenceProvider.class)
public class CraftCommand extends BaseCommand {
    private final CraftMenu craftMenu;
    private final OngoingCraftMenu ongoingCraftMenu;
    private final CraftManager craftManager;

    public CraftCommand(CraftMenu craftMenu, OngoingCraftMenu ongoingCraftMenu, CraftManager craftManager) {
        this.craftMenu = craftMenu;
        this.ongoingCraftMenu = ongoingCraftMenu;
        this.craftManager = craftManager;
    }

    @Command("menu")
    public void openCraftMenu(BukkitCommandContext ctx) {
        craftMenu.open(ctx.getPlayer());
    }

    @Command("ongoing")
    public void openOngoingCraftMenu(BukkitCommandContext ctx) {
        ongoingCraftMenu.open(ctx.getPlayer());
    }

    @Command("give")
    public void giveItem(BukkitCommandContext ctx, @Arg("player") Player player, @Arg("craft") Craft craft) {
        ItemStack item = craft.getItemStack();
        player.getInventory().addItem(item);
    }
}
