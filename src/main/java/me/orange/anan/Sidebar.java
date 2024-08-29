package me.orange.anan;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.sidebar.SidebarAdapter;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.craft.crafting.CraftTimer;
import me.orange.anan.craft.crafting.CraftTimerManager;
import me.orange.anan.job.JobManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class Sidebar implements SidebarAdapter {
    private final ClanManager clanManager;
    private final CraftTimerManager craftTimerManager;
    private final JobManager jobManager;

    public Sidebar(ClanManager clanManager, CraftTimerManager craftTimerManager, JobManager jobManager) {
        this.clanManager = clanManager;
        this.craftTimerManager = craftTimerManager;
        this.jobManager = jobManager;
    }

    @Override
    public Component getTitle(MCPlayer player) {
        return Component.text("§b§lAnan");
    }

    @Override
    public List<Component> getLines(MCPlayer mcPlayer) {
        String clanName = "No Clan";
        String jobName = "No Job";
        int members = 0;
        int onlineMembers = 0;
        Player player = mcPlayer.as(Player.class);
        if(jobManager.hasJob(player)) {
            jobName = jobManager.getPlayerCurrentJob(player.getUniqueId()).getName();
        }

        if (clanManager.inClan(player)) {
            clanName = clanManager.getPlayerClan(player.getUniqueId()).getDisplayName();
            members = clanManager.getClanSize(player);
            onlineMembers = clanManager.getOnlineClanSize(player);
        }
        List<Component> sidebar = new ArrayList<>();
        sidebar.add(Component.text("§7§m-----------------"));
        sidebar.add(Component.text("§fClan"));
        sidebar.add(Component.text("§3» §bName§7: §f" + clanName));
        sidebar.add(Component.text("§3» §bMembers§7: §f" + onlineMembers + "§7/§f" + members));
        sidebar.add(Component.text(""));
        sidebar.add(Component.text("§fPlayer"));
        sidebar.add(Component.text("§3» §bJob§7: §f" + jobName));

        if (craftTimerManager.isCrafting(player)) {
            sidebar.add(Component.text(""));
            sidebar.add(Component.text("§3» §bCrafting§7:"));
            int i = 4;
            for (CraftTimer craftTimer : craftTimerManager.getPlayerCraftTimerList(player)) {
                sidebar.add(Component.text("   §f" + craftTimer.getCraft().getName() + "x" + craftTimer.getAmount() + " - §6" + craftTimer.getTime() + "s"));
                i--;
                if (i == 0)
                    break;
            }
        }
        sidebar.add(Component.text(""));
        sidebar.add(Component.text("§f134tc.ddns.net"));
        sidebar.add(Component.text("§7§m-----------------"));
        return sidebar;
    }
}
