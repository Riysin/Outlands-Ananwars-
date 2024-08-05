package me.orange.anan;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.sidebar.SidebarAdapter;
import me.orange.anan.clan.Clan;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.craft.CraftTimer;
import me.orange.anan.craft.CraftTimerManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@InjectableComponent
public class Sidebar implements SidebarAdapter {
    private final ClanManager clanManager;
    private final CraftTimerManager craftTimerManager;

    public Sidebar(ClanManager clanManager, CraftTimerManager craftTimerManager) {
        this.clanManager = clanManager;
        this.craftTimerManager = craftTimerManager;
    }

    @Override
    public Component getTitle(MCPlayer player) {
        return Component.text("§e§lTest");
    }

    @Override
    public List<Component> getLines(MCPlayer mcPlayer) {
        String clanName = "無";
        Player player = mcPlayer.as(Player.class);

        if (clanManager.inClan(player.getUniqueId())) {
            clanName = clanManager.getPlayerClan(player.getUniqueId()).getDisplayName();
        }
        List<Component> sidebar = new ArrayList<>();
        sidebar.add(Component.text("§7§m--------------------"));
        sidebar.add(Component.text("§f玩家: §6" + player.getName()));
        sidebar.add(Component.text("§f隊伍: " + clanName));
        sidebar.add(Component.text(""));
        sidebar.add(Component.text("§f職業: §6" + "test"));

        if (craftTimerManager.isCrafting(player)) {
            sidebar.add(Component.text(""));
            sidebar.add(Component.text("§f正在製作:"));
            int i = 4;
            for (CraftTimer craftTimer : craftTimerManager.getPlayerCraftTimerList(player)) {
                sidebar.add(Component.text("   §f" + craftTimer.getCraft().getName() + "x" + craftTimer.getAmount() + " - §6" + craftTimer.getTime() + "s"));
                i--;
                if (i == 0)
                    break;
            }
        }

        sidebar.add(Component.text("§7§m--------------------"));
        return sidebar;
    }
}
