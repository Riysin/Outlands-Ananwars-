package me.orange.anan.player;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.pane.mapping.PaneMapping;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.clan.ClanManager;
import me.orange.anan.player.job.Job;
import me.orange.anan.player.job.JobManager;
import me.orange.anan.player.task.menu.AssignedTaskMenu;
import me.orange.anan.player.task.TaskManager;
import me.orange.anan.player.task.TaskStatus;
import me.orange.anan.player.death.DeathManager;
import me.orange.anan.player.friend.FriendMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class PlayerStatsMenu {
    private final GuiFactory guiFactory;
    private final PlayerDataManager playerDataManager;
    private final JobManager jobManager;
    private final ClanManager clanManager;
    private final FriendMenu friendMenu;
    private final DeathManager deathManager;
    private final TaskManager taskManager;
    private final AssignedTaskMenu assignedTaskMenu;

    public PlayerStatsMenu(GuiFactory guiFactory, PlayerDataManager playerDataManager, JobManager jobManager, ClanManager clanManager, FriendMenu friendMenu, DeathManager deathManager, TaskManager taskManager, AssignedTaskMenu assignedTaskMenu) {
        this.guiFactory = guiFactory;
        this.playerDataManager = playerDataManager;
        this.jobManager = jobManager;
        this.clanManager = clanManager;
        this.friendMenu = friendMenu;
        this.deathManager = deathManager;
        this.taskManager = taskManager;
        this.assignedTaskMenu = assignedTaskMenu;
    }

    public void open(Player ctx, Player player) {
        Gui gui = guiFactory.create(Component.text(player.getName() + "'s Stats"));
        NormalPane pane = Pane.normal(PaneMapping.rectangle(1, 1, 7, 3));
        NormalPane border = Pane.normal(PaneMapping.outline(0, 0, 9, 5));

        List<String> jobLore = new ArrayList<>();
        jobLore.add("§7None");
        if (jobManager.hasCurrentJob(player)) {
            Job job = jobManager.geCurrentJob(player);
            int jobLevel = jobManager.getJobLevel(player, job);
            jobLore.clear();
            jobLore.addAll(playerDataManager.getJobStatsLore(jobLevel, job));
        }

        String clanName = "§7None";
        int clanSize = 0;
        if (clanManager.inClan(player)) {
            clanName = clanManager.getPlayerClan(player).getDisplayName();
            clanSize = clanManager.getClanSize(player);
        }

        pane.setSlot(3, 0, GuiSlot.of(ItemBuilder
                .of(XMaterial.PLAYER_HEAD)
                .skull(player.getName())
                .name("§6§l" + player.getName())
                .lore(getPlayerLore(player))
                .build()));

        pane.setSlot(1, 1, GuiSlot.of(ItemBuilder.of(XMaterial.IRON_SWORD)
                .name("§eJob")
                .lore(jobLore)
                .build()));

        pane.setSlot(2, 1, GuiSlot.of(ItemBuilder.of(XMaterial.DIAMOND)
                .name("§eFriends")
                .lore("§fFriends: §a" + playerDataManager.getFriends(player).size(), "", "§eClick to view friends")
                .build(), clicker -> {
            clicker.playSound(clicker.getLocation(), Sound.CLICK, 1, 1);
            friendMenu.open(player);
        }));

        pane.setSlot(4, 1, GuiSlot.of(ItemBuilder.of(XMaterial.GOLDEN_HELMET).name("§eClan Info")
                .lore("§fClan: §6" + clanName
                        , "§fPlayers: §a" + clanSize)
                .build()));
        pane.setSlot(5, 1, GuiSlot.of(ItemBuilder.of(XMaterial.BOOK)
                .name("§eTasks")
                .lore("§fTasks: §a" + (int) taskManager.getPlayerTasks(player).stream().filter(task -> task.getStatus().equals(TaskStatus.ASSIGNED)).count(), "", "§eClick to view assigned tasks")
                .build(), clicker -> {
            clicker.playSound(clicker.getLocation(), Sound.CLICK, 1, 1);
            assignedTaskMenu.open(player);
        }));

        pane.setSlot(6, 2, GuiSlot.of(ItemBuilder.of(XMaterial.REDSTONE_BLOCK)
                .name("§cRespawn")
                .lore("§7Click to respawn", "", "§e§lWarning:", " §cYou will lose all your items in your inventory!").build(), clicker -> {
            Bukkit.getPluginManager().callEvent(new InventoryCloseEvent(ctx.getPlayer().getOpenInventory()));
            deathManager.addDownPlayer(player);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill " + player.getName());
        }));

        border.setSlot(4, 4, GuiSlot.of(ItemBuilder.of(XMaterial.BARRIER)
                .name("§cClose")
                .build(), clicker -> {
            clicker.playSound(clicker.getLocation(), Sound.CLICK, 1, 1);
            clicker.getPlayer().closeInventory();
        }));

        border.fillEmptySlots(GuiSlot.of(XMaterial.GRAY_STAINED_GLASS_PANE));

        gui.addPane(pane);
        gui.addPane(border);
        gui.open(ctx);
    }

    private List<String> getPlayerLore(Player player) {

        int playtimeTicks = player.getStatistic(Statistic.PLAY_ONE_TICK);

        int playtimeSeconds = playtimeTicks / 20;
        double playtimeHours = playtimeSeconds / 3600.0;

        // 使用 DecimalFormat 格式化小數點後兩位
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedPlaytime = df.format(playtimeHours);

        List<String> lore = new ArrayList<>();
        lore.add("§fKills: §e" + playerDataManager.getPlayerData(player).getKills());
        lore.add("§fDeaths: §c" + playerDataManager.getPlayerData(player).getDeaths());
        lore.add("");
        lore.add("§fPlaytime: §a" + formattedPlaytime + " h");
        return lore;
    }
}
