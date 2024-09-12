package me.orange.anan;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.sidebar.SidebarAdapter;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.craft.crafting.CraftTimer;
import me.orange.anan.craft.crafting.CraftTimerManager;
import me.orange.anan.job.JobManager;
import me.orange.anan.npc.task.Task;
import me.orange.anan.npc.task.TaskManager;
import me.orange.anan.npc.task.TaskStatus;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class Sidebar implements SidebarAdapter {
    private final ClanManager clanManager;
    private final CraftTimerManager craftTimerManager;
    private final JobManager jobManager;
    private final TaskManager taskManager;

    private static final String SEPARATOR = "§7§m-----------------";
    private static final int MAX_TASKS_DISPLAYED = 3;
    private static final int MAX_CRAFTS_DISPLAYED = 4;

    public Sidebar(ClanManager clanManager, CraftTimerManager craftTimerManager, JobManager jobManager, TaskManager taskManager) {
        this.clanManager = clanManager;
        this.craftTimerManager = craftTimerManager;
        this.jobManager = jobManager;
        this.taskManager = taskManager;
    }

    @Override
    public Component getTitle(MCPlayer player) {
        return Component.text("§b§lAnan");
    }

    @Override
    public List<Component> getLines(MCPlayer mcPlayer) {
        Player player = mcPlayer.as(Player.class);
        List<Component> sidebar = new ArrayList<>();

        String clanName = "NO_CLAN";
        int members = 0, onlineMembers = 0;
        if (clanManager.inClan(player)) {
            clanName = clanManager.getPlayerClan(player.getUniqueId()).getDisplayName();
            members = clanManager.getClanSize(player);
            onlineMembers = clanManager.getOnlineClanSize(player);
        }

        String jobName = "NO_JOB";
        int jobLevel = 0;
        if (jobManager.hasJob(player)) {
            jobName = jobManager.getPlayerCurrentJob(player.getUniqueId()).getName();
            jobLevel = jobManager.getPlayerJobLevel(player, jobManager.getPlayerCurrentJob(player.getUniqueId()));
        }

        sidebar.add(Component.text("§7§m-----------------"));
        sidebar.add(Component.text("§fClan"));
        sidebar.add(Component.text("§3» §bName§7: §f" + clanName));
        sidebar.add(Component.text("§3» §bMembers§7: §f" + onlineMembers + "§7/§f" + members));
        sidebar.add(Component.text(""));

        sidebar.add(Component.text("§fPlayer"));
        sidebar.add(Component.text("§3» §bJob§7: §f" + jobName));
        sidebar.add(Component.text("§3» §bLevel§7: §f" + jobLevel));

        // Task Information
        List<Task> tasks = taskManager.getPlayerTasks(player);
        if (!tasks.isEmpty() && tasks.stream().anyMatch(task -> task.getStatus() == TaskStatus.ASSIGNED)) {
            sidebar.add(Component.text(""));
            sidebar.add(Component.text("§3» §bTasks§7:"));
            int taskCount = 0;
            for (Task task : tasks) {
                if (task.getStatus() == TaskStatus.ASSIGNED && taskCount < MAX_TASKS_DISPLAYED) {
                    sidebar.add(Component.text("   §f" + task.getName()));
                    taskCount++;
                }
            }
        }

        // Crafting Information
        if (craftTimerManager.isCrafting(player)) {
            sidebar.add(Component.text(""));
            sidebar.add(Component.text("§3» §bCrafting§7:"));
            int craftCount = 0;
            for (CraftTimer craftTimer : craftTimerManager.getPlayerCraftTimerList(player)) {
                if (craftCount >= MAX_CRAFTS_DISPLAYED) break;
                sidebar.add(Component.text("   §f" + craftTimer.getCraft().getName() + "x" + craftTimer.getAmount() + " - §6" + craftTimer.getTime() + "s"));
                craftCount++;
            }
        }

        // Server Info
        sidebar.add(Component.text(""));
        sidebar.add(Component.text("§f134tc.ddns.net"));
        sidebar.add(Component.text(SEPARATOR));

        return sidebar;
    }
}
