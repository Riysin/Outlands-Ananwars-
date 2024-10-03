package me.orange.anan.npc;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.events.PlayerDamageNPCResourceEvent;
import me.orange.anan.player.task.*;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@InjectableComponent
@Command(value = {"anpc"})
public class NPCCommand extends BaseCommand {
    private final NPCManager npcManager;
    private final TaskManager taskManager;
    private final TaskAssignMenu taskAssignMenu;
    private final TaskRewardMenu taskRewardMenu;
    private final NPCShopManager npcShopManager;

    public NPCCommand(NPCManager npcManager, TaskManager taskManager, TaskAssignMenu taskAssignMenu, TaskRewardMenu taskRewardMenu, NPCShopManager npcShopManager) {
        this.npcManager = npcManager;
        this.taskManager = taskManager;
        this.taskAssignMenu = taskAssignMenu;
        this.taskRewardMenu = taskRewardMenu;
        this.npcShopManager = npcShopManager;
    }

    @Command(value = "create", permissionNode = "npc.admin")
    public void create(BukkitCommandContext ctx, @Arg("name") String name) {
        Player player = ctx.getPlayer();
        npcManager.createNPC(name, player.getLocation());
        player.sendMessage("NPC created.");
    }

    @Command(value = "merchant", permissionNode = "npc.admin")
    public void setup(BukkitCommandContext ctx, @Arg("merchantID") String merchantID) {
        Player player = ctx.getPlayer();
        npcManager.createMerchantNPC(player, merchantID);
        player.sendMessage("Merchant Setup.");
    }

    @Command(value = "shop")
    public void shop(BukkitCommandContext ctx, @Arg("merchantID") String merchantID) {
        npcShopManager.open(ctx.getPlayer(), merchantID);
    }

    @Command(value = "loot", permissionNode = "npc.admin")
    public void resource(BukkitCommandContext ctx) {
        npcManager.createLootNPC("Resource", ctx.getPlayer().getLocation());
        ctx.getPlayer().sendMessage("Resource NPC setup.");
    }

    @Command(value = "hurt")
    public void hurt(BukkitCommandContext ctx, @Arg("npcID") int id) {
        NPC npc = CitizensAPI.getNPCRegistry().getById(id);
        Player player = ctx.getPlayer();
        Block block = npc.getStoredLocation().getBlock();

        if (npc.getEntity() instanceof LivingEntity) {
            Bukkit.getPluginManager().callEvent(new PlayerDamageNPCResourceEvent(player, npc, block));
        }
    }

    @Command(value = "tasknpc", permissionNode = "npc.admin")
    public void taskNPC(BukkitCommandContext ctx, @Arg("name") String taskID) {
        npcManager.createTaskNPC(taskID, ctx.getPlayer().getLocation());
        ctx.getPlayer().sendMessage("Task NPC setup.");
    }

    @Command(value = "task")
    public void task(BukkitCommandContext ctx, @Arg("taskID") String taskID) {
        Player player = ctx.getPlayer();
        Task task = taskManager.getPlayerTask(player, taskID);
        if (task == null) {
            taskAssignMenu.open(player, taskID);
        } else if (task.getStatus() == TaskStatus.ASSIGNED) {
            player.sendMessage("§eWhat are you waiting for? Go and finish your task!");
        } else if (task.getStatus() == TaskStatus.COMPLETED) {
            taskRewardMenu.open(player, task);
        } else {
            player.sendMessage("§fHope you are happy about your reward.");
        }
    }

    @Command(value = "say", permissionNode = "npc.admin")
    public void say(BukkitCommandContext ctx) {
        ctx.getPlayer().sendMessage("Hello, I am an NPC.");
    }
}
