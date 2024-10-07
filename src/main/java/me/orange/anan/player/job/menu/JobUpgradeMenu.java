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
import me.orange.anan.craft.CraftManager;
import me.orange.anan.player.job.Job;
import me.orange.anan.player.job.JobManager;
import me.orange.anan.player.PlayerDataManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@InjectableComponent
public class JobUpgradeMenu {
    private final GuiFactory guiFactory;
    private final CraftManager craftManger;
    private final JobManager jobManager;
    private final PlayerDataManager playerDataManager;

    public JobUpgradeMenu(GuiFactory guiFactory, CraftManager craftManger, JobManager jobManager, PlayerDataManager playerDataManager) {
        this.guiFactory = guiFactory;
        this.craftManger = craftManger;
        this.jobManager = jobManager;
        this.playerDataManager = playerDataManager;
    }

    public void open(Player player, Job job) {
        Gui gui = guiFactory.create(Component.text("Job Upgrade Menu"));
        NormalPane pane = Pane.normal(PaneMapping.rectangle(0, 0, 9, 4));

        AtomicInteger scrollAmount = new AtomicInteger();

        gui.onDrawCallback($ -> {
            pane.clear();
            int playerLevel = jobManager.getPlayerJobLevel(player, job);
            for (int level = 1; level <= 35; level++) {
                int clickLevel = level;

                int x = getX(level) - scrollAmount.get();
                if (x > 8 || x < 0) {
                    continue;
                }
                if (scrollAmount.get() == 0) {
                    pane.setSlot(0, GuiSlot.of(ItemBuilder.of(job.getIcon())
                            .name("§6" + job.getName())
                            .lore("§7" + job.getDescription())
                            .build()));
                }

                pane.setSlot(x, getY(level), GuiSlot.of(ItemBuilder.of(getMaterial(level, playerLevel, job))
                        .name("§eLevel " + level)
                        .lore(getLore(level, playerLevel, job))
                        .amount(level)
                        .build(), clicker -> {
                    if (playerLevel + 1 < clickLevel) {
                        player.sendMessage("§eYou need to upgrade to level " + (playerLevel + 1) + " first");
                        player.getWorld().playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                    } else if (playerLevel >= clickLevel) {
                        player.sendMessage("§eYou can't downgrade your level");
                        player.getWorld().playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);

                    } else if (!hasMoney(player, clickLevel)) {
                        player.sendMessage("§eYou need: " + (-calculateRequire(player, clickLevel)) + " more emeralds!");
                        player.getWorld().playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                    } else {
                        player.getWorld().playSound(player.getLocation(), Sound.ANVIL_USE, 0.5f, 1);
                        jobManager.addJobLevel(player.getUniqueId(), job);
                        craftManger.removeItemsFromInventory(player, craftManger.getItemStack(craftManger.getCraft(XMaterial.EMERALD), player), clickLevel * 5);

                        gui.update(player);
                    }
                }));
            }
        });

        NormalPane ui = Pane.normal(PaneMapping.rectangle(0, 4, 9, 2));

        gui.onDrawCallback($ -> {
            int level = jobManager.getPlayerJobLevel(player, job);
            ui.setSlot(4, 0, GuiSlot.of(ItemBuilder.of(XMaterial.PLAYER_HEAD)
                    .skull(player.getName())
                    .name("§e§l" + job.getName())
                    .lore(playerDataManager.getJobStatsLore(level, job))
                    .build()));
        });


        ui.setSlot(3, 1, GuiSlot.of(ItemBuilder.of(XMaterial.ARROW)
                .lore("§eClick to scroll backward")
                .build(), clicker -> {
            if (scrollAmount.get() == 0) {
                player.sendMessage("§eYou are already at the beginning");
                player.getWorld().playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                return;
            }
            scrollAmount.getAndDecrement();
            player.getWorld().playEffect(player.getLocation(), Effect.CLICK1, 0);
            gui.update(player);
        }));
        ui.setSlot(4, 1, GuiSlot.of(ItemBuilder.of(XMaterial.BARRIER)
                .lore("§cClose the menu")
                .build(), HumanEntity::closeInventory));
        ui.setSlot(5, 1, GuiSlot.of(ItemBuilder.of(XMaterial.ARROW)
                .lore("§eClick to scroll forward")
                .build(), clicker -> {
            if (scrollAmount.get() == 6) {
                player.sendMessage("§eYou are already at the end");
                player.getWorld().playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                return;
            }
            scrollAmount.getAndIncrement();
            player.getWorld().playEffect(player.getLocation(), Effect.CLICK1, 0);
            gui.update(player);
        }));

        gui.addPane(pane);
        gui.addPane(ui);
        gui.open(player);
    }

    private int getX(int level) {
        int x = 0;
        for (int i = 1; i <= level; i++) {
            switch (i % 10) {
                case 0:
                case 4:
                case 5:
                case 9:
                    x++;
            }
        }
        return x;
    }

    private int getY(int level) {
        int toTen = level % 10;
        if (toTen == 0) {
            toTen = 10;
        }
        switch (toTen) {
            case 1:
            case 2:
            case 3:
                return toTen;
            case 4:
            case 5:
                return 3;
            case 6:
            case 7:
            case 8:
                return 8 - toTen;
            case 9:
            case 10:
                return 0;
        }
        return toTen;
    }

    private XMaterial getMaterial(int level, int playerLevel, Job job) {
        if (level == 35) {
            return job.getActiveIcon();
        }
        if (level <= playerLevel) {
            if (level % 10 == 0) {
                return XMaterial.EMERALD_BLOCK;
            }
            return XMaterial.GREEN_STAINED_GLASS_PANE;
        } else {
            if (level % 10 == 0) {
                return XMaterial.REDSTONE_BLOCK;
            }
            return XMaterial.RED_STAINED_GLASS_PANE;
        }
    }

    private List<String> getLore(int level, int playerLevel, Job job) {
        List<String> lore = new ArrayList<>();
        lore.add("§6" + job.getUpgradeName());
        lore.add("§7" + job.getUpgradeDescription());
        lore.add("§eChance: §a" + job.getChancePerLevel() * level + "§f%");
        lore.add("");
        if (level == 10) {
            lore.add("§6" + job.getSkill1Name());
            lore.add("§7" + job.getSkill1Description());
            lore.add("");
        } else if (level == 20) {
            lore.add("§6" + job.getSkill2Name());
            lore.add("§7" + job.getSkill2Description());
            lore.add("");
        } else if (level == 30) {
            lore.add("§6" + job.getSkill3Name());
            lore.add("§7" + job.getSkill3Description());
            lore.add("");
        } else if (level == 35) {
            lore.add("§6" + job.getActiveName());
            lore.add("§7" + job.getActiveDescription());
            lore.add("");
        }
        lore.add("§eCost: " + level * 5);
        lore.add("");
        if (level <= playerLevel) {
            lore.add("§aUnlocked");
        } else {
            lore.add("§cLocked");
            lore.add("");
            lore.add("§e§lClick to upgrade");
        }
        return lore;
    }

    private boolean hasMoney(Player player, int level) {
        return getMoney(player) >= level * 5;
    }

    private int calculateRequire(Player player, int level) {
        return getMoney(player) - level * 5;
    }

    private int getMoney(Player player) {
        return craftManger.getPlayerItemAmount(player, craftManger.getConfigItemWithID("emerald"));
    }
}
