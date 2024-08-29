package me.orange.anan.npc;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.entity.Player;

@InjectableComponent
@Command(value ={"aNpc"},permissionNode = "npc.admin")
public class NPCCommand extends BaseCommand {
    private final NPCManager npcManager;
    private final NPCTaskMenu npcTaskMenu;

    public NPCCommand(NPCManager npcManager, NPCTaskMenu npcTaskMenu) {
        this.npcManager = npcManager;
        this.npcTaskMenu = npcTaskMenu;
    }

    @Command(value = "create")
    public void create(BukkitCommandContext ctx, @Arg("name") String name) {
        Player player = ctx.getPlayer();
        npcManager.createNPC(name , player.getLocation());
        player.sendMessage("NPC created.");
    }

    @Command(value = "setup")
    public void setup(BukkitCommandContext ctx, @Arg("id") int id) {
        npcManager.setUpMerchantNPC(id);
        ctx.getPlayer().sendMessage("NPC setup.");
    }

    @Command(value = "task")
    public void task(BukkitCommandContext ctx) {
        npcTaskMenu.open(ctx.getPlayer());
        ctx.getPlayer().sendMessage("Task menu opened.");
    }

    @Command(value = "say")
    public void say(BukkitCommandContext ctx) {
        ctx.getPlayer().sendMessage("Hello, I am an NPC.");
    }
}
