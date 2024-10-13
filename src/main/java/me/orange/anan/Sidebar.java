package me.orange.anan;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.sidebar.SidebarAdapter;
import me.orange.anan.craft.behaviour.teamCore.TeamCoreManager;
import me.orange.anan.craft.crafting.CraftTimer;
import me.orange.anan.craft.crafting.CraftTimerManager;
import me.orange.anan.player.job.JobManager;
import me.orange.anan.player.task.Task;
import me.orange.anan.player.task.TaskManager;
import me.orange.anan.player.task.TaskStatus;
import me.orange.anan.player.PlayerDataManager;
import me.orange.anan.world.TimeManager;
import me.orange.anan.world.region.SafeZoneManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class Sidebar implements SidebarAdapter {
    private final CraftTimerManager craftTimerManager;
    private final JobManager jobManager;
    private final TaskManager taskManager;
    private final TimeManager timeManager;
    private final PlayerDataManager playerDataManager;
    private final TeamCoreManager teamCoreManager;
    private final SafeZoneManager safeZoneManager;

    private static final String SEPARATOR = "§7§m-----------------";
    private static final int MAX_TASKS_DISPLAYED = 3;
    private static final int MAX_CRAFTS_DISPLAYED = 3;

    public Sidebar(CraftTimerManager craftTimerManager, JobManager jobManager, TaskManager taskManager, TimeManager timeManager, PlayerDataManager playerDataManager, TeamCoreManager teamCoreManager, SafeZoneManager safeZoneManager) {
        this.craftTimerManager = craftTimerManager;
        this.jobManager = jobManager;
        this.taskManager = taskManager;
        this.timeManager = timeManager;
        this.playerDataManager = playerDataManager;
        this.teamCoreManager = teamCoreManager;
        this.safeZoneManager = safeZoneManager;
    }

    @Override
    public Component getTitle(MCPlayer player) {
        return Component.text("§b§lOutlands");
    }

    @Override
    public List<Component> getLines(MCPlayer mcPlayer) {
        Player player = mcPlayer.as(Player.class);
        List<Component> sidebar = new ArrayList<>();

        String jobName = "None";
        int jobLevel = 0;
        if (jobManager.hasCurrentJob(player)) {
            jobName = jobManager.geCurrentJob(player.getUniqueId()).getName();
            jobLevel = jobManager.getJobLevel(player, jobManager.geCurrentJob(player.getUniqueId()));
        }

        String location = teamCoreManager.isInTerritory(player) ? "§3Territory" : "§2Wilderness";
        if(safeZoneManager.isInSafeZone(player)) {
            location = "§6SafeZone";
        }

        sidebar.add(Component.text(SEPARATOR));
        //server
        sidebar.add(Component.text("§fPlayer"));
        sidebar.add(Component.text("§3» §bTime§7: §f" + timeManager.getTimeState(player.getWorld())));
        sidebar.add(Component.text("§3» §bLoc.§7: §f" + location));
        sidebar.add(Component.text("§3» §bFriends§7: §f" + playerDataManager.getPlayerData(player).getOnlineFriends().size() + "/" + playerDataManager.getPlayerData(player).getFriends().size()));
        sidebar.add(Component.text(""));

        sidebar.add(Component.text("§fJob"));
        sidebar.add(Component.text("§3» §bJob§7: §f" + jobName));
        sidebar.add(Component.text("§3» §bLevel§7: §f" + jobLevel));

        // Task Information
        List<Task> tasks = taskManager.getPlayerTasks(player);
        if (!tasks.isEmpty() && tasks.stream().anyMatch(task -> task.getStatus() == TaskStatus.ASSIGNED)) {
            sidebar.add(Component.text(""));
            sidebar.add(Component.text("§fTasks"));
            int taskCount = 0;
            for (Task task : tasks) {
                if (task.getStatus() == TaskStatus.ASSIGNED && taskCount < MAX_TASKS_DISPLAYED) {
                    sidebar.add(Component.text("§3» §bName§7: §f" + task.getName()));
                    taskCount++;
                }
            }
        }

        // Crafting Information
        if (craftTimerManager.isCrafting(player)) {
            sidebar.add(Component.text(""));
            sidebar.add(Component.text("§fCrafting"));
            int craftCount = 0;
            for (CraftTimer craftTimer : craftTimerManager.getPlayerCraftTimerList(player)) {
                if (craftCount >= MAX_CRAFTS_DISPLAYED) break;
                sidebar.add(Component.text("§3» §bItem§7: §f" + craftTimer.getCraft().getName() + "x" + craftTimer.getAmount() + " - §6" + craftTimer.getTime() + "s"));
                craftCount++;
            }
        }

        // Server Info
        sidebar.add(Component.text(""));
        sidebar.add(Component.text("§fOutlands.ddns.net"));
        sidebar.add(Component.text(SEPARATOR));

        return sidebar;
    }
}
