package me.orange.anan.player.job.menu;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.pane.mapping.PaneMapping;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.player.job.Job;
import me.orange.anan.player.job.JobManager;
import me.orange.anan.player.job.JobRegistry;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@InjectableComponent
public class JobChooseMenu {
    private final GuiFactory guiFactory;
    private final JobUpgradeMenu jobUpgradeMenu;
    private final JobManager jobManager;

    public JobChooseMenu(GuiFactory guiFactory, JobUpgradeMenu jobUpgradeMenu, JobManager jobManager) {
        this.guiFactory = guiFactory;
        this.jobUpgradeMenu = jobUpgradeMenu;
        this.jobManager = jobManager;
    }

    public void open(Player player) {
        Gui gui = guiFactory.create(Component.text("Job Choose Menu"));
        NormalPane pane = Pane.normal(PaneMapping.rectangle(1, 1, 7, 4));

        //for Job in JobRegister
        JobRegistry jobRegistry = new JobRegistry();
        int slot = 0;
        for (Job job : jobRegistry.getJobs()) {
            pane.setSlot(slot, GuiSlot.of(ItemBuilder.of(job.getIcon())
                    .name("§e" + job.getName())
                    .lore(getLore(player, job))
                    .build(), clicker -> {
                clicker.playSound(clicker.getLocation(), Sound.CLICK, 1, 1);

                if (jobManager.getJobLevelMap(player).get(job.getID()) == null) {
                    player.sendMessage("§cYou haven't unlocked this job yet!");
                    return;
                }
                jobManager.addPlayer(player, job);
                jobUpgradeMenu.open(player, job);
            }));
            slot++;
        }

        pane.setSlot(6, 3, GuiSlot.of(ItemBuilder.of(XMaterial.BARRIER).name("§cResign").lore("§eClick to have no job!").build(), clicker -> {
            jobManager.setCurrentJob(player.getUniqueId(), null);
            player.closeInventory();
        }));

        //border
        NormalPane border = Pane.normal(PaneMapping.outline(9, 6));
        border.fillEmptySlots(GuiSlot.of(ItemBuilder.of(XMaterial.GRAY_STAINED_GLASS_PANE).name(" ").build()));

        gui.addPane(pane);
        gui.addPane(border);
        gui.open(player);
    }

    private List<String> getLore(Player player, Job job) {
        List<String> lore = new ArrayList<>();
        lore.add("§7" + job.getDescription());
        lore.add("");

        if (jobManager.hasUnlockJob(player, job)) {
            lore.add("§6Level: §7" + jobManager.getJobLevel(player, job));
            lore.add("");
            lore.add("§eClick to choose this job!");
        } else {
            lore.add("§cYou haven't unlocked this job yet!");
        }

        return lore;
    }
}
