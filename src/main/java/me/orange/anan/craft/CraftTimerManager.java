package me.orange.anan.craft;

import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import io.fairyproject.scheduler.response.TaskResponse;
import me.orange.anan.events.CraftTimerCountDownEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@InjectableComponent
public class CraftTimerManager {
    private List<CraftTimer> craftTimerList = new ArrayList<>();

    public List<CraftTimer> getCraftTimerList() {
        return craftTimerList;
    }

    public void setCraftTimerList(List<CraftTimer> craftTimerList) {
        this.craftTimerList = craftTimerList;
    }

    public List<CraftTimer> getPlayerCraftTimerList(Player player) {
        List<CraftTimer> craftingList = new ArrayList<>();
        craftTimerList.forEach(craftTimer -> {
            if (craftTimer.getUuid() == player.getUniqueId())
                craftingList.add(craftTimer);
        });
        return craftingList;
    }

    public CraftTimer getPlayerFirstCraftTimer(Player player) {
        for (CraftTimer craftTimer : getPlayerCraftTimerList(player)) {
            if (craftTimer.getPlayer() == player)
                return craftTimer;
        }
        return null;
    }

    public void addCraftTimer(Player player, Craft craft, int amount) {
        CraftTimer craftTimer = new CraftTimer(player, craft, craft.getTime(), amount);
        craftTimerList.add(craftTimer);
        if (craftTimer == getPlayerFirstCraftTimer(player))
            craftingCountDown(player, craftTimer);
    }

    public void craftingCountDown(Player player, CraftTimer craftTimer) {
        CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            if (craftTimer.isFailed()) {
                return TaskResponse.failure("cancelled");
            }
            Bukkit.getPluginManager().callEvent(new CraftTimerCountDownEvent(player, craftTimer));
            craftTimer.setTime(craftTimer.getTime() - 1);

            return TaskResponse.continueTask();
        }, 0, 20, RepeatPredicate.length(Duration.ofSeconds(craftTimer.getCraft().getTime()))).getFuture();

        future.thenRun(() -> {
            if (craftTimer.getAmount() > 1) {
                craftTimer.setAmount(craftTimer.getAmount() - 1);
                craftTimer.setTime(craftTimer.getCraft().getTime());
                player.getInventory().addItem(craftTimer.getCraft().getItemStack());
                craftingCountDown(player, craftTimer);
            } else {
                removeCraftTimer(craftTimer);
                player.getInventory().addItem(craftTimer.getCraft().getItemStack());
                player.sendMessage("crafting finished");
                Bukkit.getPluginManager().callEvent(new CraftTimerCountDownEvent(player, craftTimer));
                craftingCountDown(player, getPlayerFirstCraftTimer(player));
            }
        });
    }

    public boolean isCrafting(Player player) {
        for (CraftTimer craftTimer : getPlayerCraftTimerList(player)) {
            if (craftTimer.getPlayer() == player)
                return true;
        }
        return false;
    }

    public void removeCraftTimer(CraftTimer craftTimer) {
        craftTimerList.remove(craftTimer);
    }

    public void craftingFailed(Player player, CraftTimer craftTimer) {
        craftTimer.setFailed(true);
        player.sendMessage("stoped the crafting of " + craftTimer.getCraft().getName() + "x" + craftTimer.getAmount());
        returnItems(player, craftTimer.getCraft(), craftTimer.getAmount());
        removeCraftTimer(craftTimer);
        if (getPlayerFirstCraftTimer(player) != null)
            craftingCountDown(player, getPlayerFirstCraftTimer(player));
    }

    //return player items back if crafting failed
    public void returnItems(Player player, Craft craft, Integer craftCount) {
        for (ItemStack item : craft.getRecipe()) {
            int totalAmount = item.getAmount() * craftCount;
            ItemStack returnItem = item.clone();
            returnItem.setAmount(totalAmount);
            player.getInventory().addItem(returnItem);
        }
    }
}
