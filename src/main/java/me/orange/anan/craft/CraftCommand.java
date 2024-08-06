package me.orange.anan.craft;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.bukkit.command.presence.DefaultPresenceProvider;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.command.annotation.CommandPresence;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.crafting.CraftMenu;
import me.orange.anan.craft.crafting.ongoinCraftMenu.OngoingCraftMenu;
import me.orange.anan.craft.behaviour.upgradeHammer.HammerMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@InjectableComponent
@Command(value = "craft")
@CommandPresence(DefaultPresenceProvider.class)
public class CraftCommand extends BaseCommand {
    private final CraftMenu craftMenu;
    private final OngoingCraftMenu ongoingCraftMenu;
    private final HammerMenu hammerMenu;
    private final CraftManager craftManager;

    public CraftCommand(CraftMenu craftMenu, OngoingCraftMenu ongoingCraftMenu, HammerMenu hammerMenu, CraftManager craftManager) {
        this.craftMenu = craftMenu;
        this.ongoingCraftMenu = ongoingCraftMenu;
        this.hammerMenu = hammerMenu;
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

    @Command(value = "give", permissionNode = "craft.give")
    public void giveItem(BukkitCommandContext ctx, @Arg("player") Player player, @Arg("craft") Craft craft,@Arg("amount") Integer amount) {
        ItemStack item = craft.getItemStack();
        item.setAmount(amount);
        player.getInventory().addItem(item);
    }

    @Command("feature")
    public void feature(BukkitCommandContext ctx, @Arg("feature") String feature) {
        switch (feature) {
            case "hammer":
                hammerMenu.open(ctx.getPlayer());
                break;
            default:
                ctx.getPlayer().sendMessage("§c找不到此功能");
        }

    }
}
