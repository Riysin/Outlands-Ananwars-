package me.orange.anan.job.jobTable;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.job.Job;
import me.orange.anan.job.JobManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

@InjectableComponent
public class JobUpgradeMenu {
    private final GuiFactory guiFactory;
    private final CraftManager craftManger;
    private final JobManager jobManager;

    public JobUpgradeMenu(GuiFactory guiFactory, CraftManager craftManger, JobManager jobManager) {
        this.guiFactory = guiFactory;
        this.craftManger = craftManger;
        this.jobManager = jobManager;
    }

    public void open(Player player, Job job) {
        Gui gui = guiFactory.create(Component.text("Job Upgrade Menu"));
        NormalPane pane = Pane.normal(9, 6);

        gui.onDrawCallback($ -> {
            int playerLevel = jobManager.getPlayerJobLevel(player, job);
            for (int level = 1; level <= 21; level++) {
                int finalTotalLevel = level;
                pane.setSlot(getSlot(level), GuiSlot.of(ItemBuilder.of(getMaterial(level))
                        .name("§fLevel: " + level)
                        .lore()
                        .amount(level)
                        .build(), clicker -> {
                    if (playerLevel + 1 != finalTotalLevel) {
                        player.sendMessage("You need to upgrade to level " + (playerLevel + 1) + " first");
                    } else if (!hasMoney(player, finalTotalLevel)) {
                        player.sendMessage("You need:" + calculateRequire(player, finalTotalLevel) + " more");
                    } else if (playerLevel >= finalTotalLevel) {
                        player.sendMessage("You have already upgraded to level " + finalTotalLevel);
                    } else {
                        player.sendMessage("&aYou have upgraded to level " + finalTotalLevel);
                        jobManager.addJobLevel(player.getUniqueId(), job);

                        gui.update(player);
                    }
                }));
            }
        });

        pane.setSlot(4, 4, GuiSlot.of(ItemBuilder.of(XMaterial.PLAYER_HEAD)
                .skull(player.getName())
                .name("§f" + player.getName())
                .lore("§7Job Stats:", "§7Level: 1", "§eClick to view job stats")
                .build(), clicker -> {
            // Open job stats menu
        }));
        pane.setSlot(3, 5, GuiSlot.of(ItemBuilder.of(XMaterial.ARROW)
                .lore("Click to scroll backward")
                .build()));
        pane.setSlot(4, 5, GuiSlot.of(ItemBuilder.of(XMaterial.BARRIER)
                .lore("Close the menu")
                .build(), HumanEntity::closeInventory));
        pane.setSlot(5, 5, GuiSlot.of(ItemBuilder.of(XMaterial.ARROW)
                .lore("Click to scroll forward")
                .build()));

        gui.addPane(pane);
        gui.open(player);
    }

    private int getSlot(int level) {
        return level - 1;
    }

    private XMaterial getMaterial(int level) {
        return XMaterial.RED_STAINED_GLASS_PANE;
    }

    private boolean hasMoney(Player player, int level) {
        return getMoney(player) <= level * 5;
    }

    private int calculateRequire(Player player, int level) {
        return getMoney(player) - level * 5;
    }

    private int getMoney(Player player) {
        return craftManger.getPlayerItemAmount(player, craftManger.getConfigItemWithID("emerald"));
    }
}
