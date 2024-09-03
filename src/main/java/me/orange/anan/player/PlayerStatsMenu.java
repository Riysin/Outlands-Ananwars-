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
import me.orange.anan.job.Job;
import me.orange.anan.job.JobManager;
import me.orange.anan.player.friend.FriendMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class PlayerStatsMenu {
    private final GuiFactory guiFactory;
    private final PlayerDataManager playerDataManager;
    private final JobManager jobManager;
    private final ClanManager clanManager;
    private final FriendMenu friendMenu;

    public PlayerStatsMenu(GuiFactory guiFactory, PlayerDataManager playerDataManager, JobManager jobManager, ClanManager clanManager, FriendMenu friendMenu) {
        this.guiFactory = guiFactory;
        this.playerDataManager = playerDataManager;
        this.jobManager = jobManager;
        this.clanManager = clanManager;
        this.friendMenu = friendMenu;
    }

    public void open(Player ctx, Player player) {
        Gui gui = guiFactory.create(Component.text(player.getName() + "'s Stats"));
        NormalPane pane = Pane.normal(PaneMapping.rectangle(1, 1, 7, 3));
        NormalPane border = Pane.normal(PaneMapping.outline(0, 0, 9, 5));

        List<String> jobLore = new ArrayList<>();
        jobLore.add("§7No Job");
        if (jobManager.hasJob(player)) {
            Job job = jobManager.getPlayerCurrentJob(player);
            int jobLevel = jobManager.getPlayerJobLevel(player, job);
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
                .name("§b§l" + player.getName())

                .build()));

        pane.setSlot(2, 1, GuiSlot.of(ItemBuilder.of(XMaterial.IRON_SWORD)
                .name("§eJob")
                .lore("§f Kills: §e" + playerDataManager.getPlayerData(player).getKills()
                        , "§f Deaths: §c" + playerDataManager.getPlayerData(player).getDeaths())
                .build()));

        pane.setSlot(3, 1, GuiSlot.of(ItemBuilder.of(XMaterial.BOOK)
                .name("§eFriends")
                .lore("§fFriends: §a" + playerDataManager.getFriends(player).size(), "", "§eClick to view friends")
                .build(), friendMenu::open));


        pane.setSlot(4, 1, GuiSlot.of(ItemBuilder.of(XMaterial.GOLDEN_HELMET).name("§eClan Info")
                .lore("§fClan: §6" + clanName
                        , "§fPlayers: §a" + clanSize)
                .build()));

        pane.setSlot(6, 2, GuiSlot.of(ItemBuilder.of(XMaterial.REDSTONE_BLOCK)
                .name("§cRespawn")
                .lore("§7Click to respawn", "", "§e§lWarning:", " §cYou will lose all your items in your inventory!").build(), clicker -> {
            Bukkit.getPluginManager().callEvent(new InventoryCloseEvent(ctx.getPlayer().getOpenInventory()));
            playerDataManager.getPlayerData(player).setKnocked(true);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill " + player.getName());
        }));

        border.setSlot(4, 4, GuiSlot.of(ItemBuilder.of(XMaterial.BARRIER).name("§cClose").build(), $ -> {
            ctx.getPlayer().closeInventory();
        }));

        border.fillEmptySlots(GuiSlot.of(XMaterial.GRAY_STAINED_GLASS_PANE));

        gui.addPane(pane);
        gui.addPane(border);
        gui.open(ctx);
    }
}
