package me.orange.anan.job.jobTable;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.pane.mapping.OutlinePaneMapping;
import io.fairyproject.bukkit.gui.pane.mapping.PaneMapping;
import io.fairyproject.bukkit.gui.pane.mapping.RectanglePaneMapping;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.job.Job;
import me.orange.anan.job.JobManager;
import me.orange.anan.job.JobRegister;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
        NormalPane pane = Pane.normal(PaneMapping.rectangle(1,1,7,4));

        //for Job in JobRegister
        JobRegister jobRegister = new JobRegister();
        int slot =1;
        for (Job job : jobRegister.getJobs()) {
            pane.setSlot(slot, slot / 7 + 1, GuiSlot.of(ItemBuilder.of(job.getIcon())
                    .name("§f" + job.getName())
                    .lore("§7" + job.getDescription())
                    .build(), clicker -> {
                jobUpgradeMenu.open(player, job);
                jobManager.addPlayer(player,job);
            }));
            slot++;
        }

        //border
        NormalPane border = Pane.normal(PaneMapping.outline(9, 6));
        border.fillEmptySlots(GuiSlot.of(ItemBuilder.of(XMaterial.GRAY_STAINED_GLASS_PANE).name(" ").build()));

        gui.addPane(pane);
        gui.addPane(border);
        gui.open(player);
    }


}
