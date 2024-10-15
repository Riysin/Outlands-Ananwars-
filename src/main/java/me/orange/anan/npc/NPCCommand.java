package me.orange.anan.npc;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Arg;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.events.PlayerDamageNPCResourceEvent;
import me.orange.anan.npc.outlandsnpc.OutlandsNPC;
import me.orange.anan.player.task.*;
import me.orange.anan.player.task.menu.TaskAssignMenu;
import me.orange.anan.player.task.menu.TaskInfoMenu;
import me.orange.anan.player.task.menu.TaskRewardMenu;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@InjectableComponent
@Command(value = {"anpc"})
public class NPCCommand extends BaseCommand {
    private final NPCManager npcManager;
    private final NPCLootManager npcLootManager;
    private final TaskManager taskManager;
    private final TaskAssignMenu taskAssignMenu;
    private final TaskRewardMenu taskRewardMenu;
    private final TaskInfoMenu taskInfoMenu;
    private final LootConfig lootConfig;

    public NPCCommand(NPCManager npcManager, NPCLootManager npcLootManager, TaskManager taskManager, TaskAssignMenu taskAssignMenu, TaskRewardMenu taskRewardMenu, TaskInfoMenu taskInfoMenu, LootConfig lootConfig) {
        this.npcManager = npcManager;
        this.npcLootManager = npcLootManager;
        this.taskManager = taskManager;
        this.taskAssignMenu = taskAssignMenu;
        this.taskRewardMenu = taskRewardMenu;
        this.taskInfoMenu = taskInfoMenu;
        this.lootConfig = lootConfig;
    }

    @Command(value = "create", permissionNode = "npc.admin")
    public void create(BukkitCommandContext ctx, @Arg("outlands npc") OutlandsNPC outlandsNPC) {
        Player player = ctx.getPlayer();
        npcManager.createNPC(player, outlandsNPC);
        player.sendMessage("NPC Created.");
    }

    @Command(value = "shop")
    public void shop(BukkitCommandContext ctx, @Arg("outlands npc") OutlandsNPC outlandsNPC) {
        Player player = ctx.getPlayer();
        npcManager.openTrade(player, outlandsNPC);
    }

    @Command(value = "hurt")
    public void hurt(BukkitCommandContext ctx, @Arg("npc ID") int id) {
        NPC npc = CitizensAPI.getNPCRegistry().getById(id);
        Player player = ctx.getPlayer();
        Block block = npc.getStoredLocation().getBlock();

        if (npc.getEntity() instanceof LivingEntity) {
            Bukkit.getPluginManager().callEvent(new PlayerDamageNPCResourceEvent(player, npc, block));
        }
    }

    @Command(value = "task")
    public void task(BukkitCommandContext ctx, @Arg("taskID") String taskID) {
        Player player = ctx.getPlayer();
        Task task = taskManager.getPlayerTask(player, taskID);
        if (task == null) {
            taskAssignMenu.open(player, taskID);
        } else if (task.getStatus() == TaskStatus.ASSIGNED) {
            player.sendMessage("§eCheck your task info.");
            taskInfoMenu.open(player, task);
        } else if (task.getStatus() == TaskStatus.COMPLETED) {
            taskRewardMenu.open(player, task);
        } else {
            player.sendMessage("§eHope you are happy about your reward!");
        }
    }

    @Command(value = "addLoot")
    public void addLoot(BukkitCommandContext ctx){
        ItemStack itemStack = ctx.getPlayer().getItemInHand();
        npcLootManager.loadConfig();
        lootConfig.addLoot(itemStack);
        lootConfig.loadAndSave();
    }
}
