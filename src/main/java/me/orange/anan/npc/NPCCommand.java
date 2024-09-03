package me.orange.anan.npc;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.events.NPCResourceDieEvent;
import me.orange.anan.events.PlayerDamageNPCResourceEvent;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@InjectableComponent
@Command(value = {"aNpc"}, permissionNode = "npc.admin")
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
        npcManager.createNPC(name, player.getLocation());
        player.sendMessage("NPC created.");
    }

    @Command(value = "merchant")
    public void setup(BukkitCommandContext ctx, @Arg("id") int id) {
        npcManager.setUpMerchantNPC(id);
        ctx.getPlayer().sendMessage("Merchant Setup.");
    }

    @Command(value = "resource")
    public void resource(BukkitCommandContext ctx) {
        npcManager.createResourceNPC("Resource", ctx.getPlayer().getLocation());
        ctx.getPlayer().sendMessage("Resource NPC setup.");
    }

    @Command(value = "hurt")
    public void hurt(BukkitCommandContext ctx, @Arg("npcID") int id) {
        NPC npc = CitizensAPI.getNPCRegistry().getById(id);
        if (npc.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) npc.getEntity();
            if (entity.getHealth() > 1)
                entity.setHealth(entity.getHealth() - 1);
            else {
                entity.setHealth(0);
                npc.despawn();
                Bukkit.getPluginManager().callEvent(new NPCResourceDieEvent(ctx.getPlayer(), npc));
            }
            Bukkit.getPluginManager().callEvent(new PlayerDamageNPCResourceEvent(ctx.getPlayer(), npc));
        }
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
