package me.orange.anan.npc.player;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

@InjectableComponent
@Command(value = {"pnpc", "pn"})
public class PlayerNPCCommand extends BaseCommand {
    private final PlayerNPCManager playerNPCManager;

    public PlayerNPCCommand(PlayerNPCManager playerNPCManager) {
        this.playerNPCManager = playerNPCManager;
    }

    @Command(value = "inventory")
    public void openInventory(BukkitCommandContext ctx, @Arg("id") int id) {
        NPC npc = CitizensAPI.getNPCRegistry().getById(id);
        if (npc != null) {
            playerNPCManager.getTraitInventory(npc).openInventory(ctx.getPlayer());
        }
    }
}
