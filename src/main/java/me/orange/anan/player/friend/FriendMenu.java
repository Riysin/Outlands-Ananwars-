package me.orange.anan.player.friend;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.gui.Gui;
import io.fairyproject.bukkit.gui.GuiFactory;
import io.fairyproject.bukkit.gui.pane.NormalPane;
import io.fairyproject.bukkit.gui.pane.Pane;
import io.fairyproject.bukkit.gui.pane.mapping.PaneMapping;
import io.fairyproject.bukkit.gui.slot.GuiSlot;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.player.PlayerDataManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@InjectableComponent
public class FriendMenu {
    private final GuiFactory guiFactory;
    private final PlayerDataManager playerDataManager;

    public FriendMenu(GuiFactory guiFactory, PlayerDataManager playerDataManager) {
        this.guiFactory = guiFactory;
        this.playerDataManager = playerDataManager;
    }

    public void open(Player player) {
        Gui gui = guiFactory.create(Component.text("Friends"));
        NormalPane pane = Pane.normal(9, 5);
        NormalPane outline = Pane.normal(PaneMapping.rectangle(0, 5, 9, 1));

        int slot = 0;

        gui.onDrawCallback($ -> {
            pane.clear();
            playerDataManager.getFriends(player).forEach(uuid -> {
                OfflinePlayer friend = Bukkit.getOfflinePlayer(uuid);

                pane.setSlot(slot, GuiSlot.of(ItemBuilder.of(XMaterial.PLAYER_HEAD)
                        .skull(friend.getName())
                        .name(friend.getName())
                        .lore(getLores(friend))
                        .build(), ctx -> {
                    playerDataManager.removeFriend(player, friend);
                    player.sendMessage("§cYou have removed " + friend.getName() + " from your friends list");
                    if (friend.isOnline()) {
                        friend.getPlayer().sendMessage("§c" + player.getName() + " has removed you from their friends list");
                    }
                    gui.update(player);
                }));
            });
        });

        outline.setSlot(4, 0, GuiSlot.of(ItemBuilder.of(XMaterial.OAK_DOOR).name("§cBack").build(), clicker -> {
            clicker.playSound(clicker.getLocation(), Sound.CLICK, 1, 1);
            Bukkit.dispatchCommand(player, "player menu " + player.getName());
        }));

        outline.fillEmptySlots(GuiSlot.of(ItemBuilder.of(XMaterial.GRAY_STAINED_GLASS_PANE).build()));

        gui.addPane(pane);
        gui.addPane(outline);
        gui.open(player);
    }

    private List<String> getLores(OfflinePlayer player) {
        Date lastPlayed = new Date(player.getLastPlayed());

        List<String> lores = new ArrayList<>();
        lores.add(getLastJoinTime(player));
        lores.add("");
        lores.add("§cClick to remove friend");
        return lores;
    }

    private String getLastJoinTime(OfflinePlayer offlinePlayer) {
        if (offlinePlayer.isOnline()) {
            return "§aOnline Now!";
        } else {
            long lastPlayed = offlinePlayer.getLastPlayed();

            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - lastPlayed;

            long seconds = timeDiff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            String timeAgo;
            if (days > 0) {
                timeAgo = days + " days ago";
            } else if (hours > 0) {
                timeAgo = hours + " hours ago";
            } else if (minutes > 0) {
                timeAgo = minutes + " minutes ago";
            } else {
                timeAgo = seconds + " seconds ago";
            }
            return "§eLast joined "+timeAgo;
        }
    }
}
