package me.orange.anan.craft.crafting;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import io.fairyproject.scheduler.response.TaskResponse;
import me.orange.anan.craft.Craft;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.events.CraftTimerCountDownEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@InjectableComponent
public class CraftTimerManager {
    private final CraftManager craftManager;

    private List<CraftTimer> craftTimerList = new ArrayList<>();

    public CraftTimerManager(CraftManager craftManager) {
        this.craftManager = craftManager;
    }

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
        Craft craft = craftTimer.getCraft();
        CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            if (craftTimer.isFailed()) {
                return TaskResponse.failure("cancelled");
            }
            Bukkit.getPluginManager().callEvent(new CraftTimerCountDownEvent(player, craftTimer));
            craftTimer.setTime(craftTimer.getTime() - 1);

            return TaskResponse.continueTask();
        }, 0, 20, RepeatPredicate.length(Duration.ofSeconds(craft.getTime() - 1))).getFuture();

        future.thenRun(() -> {
            player.getInventory().addItem(craftManager.getItemStack(craft, player));

            if (craftTimer.getAmount() > 1) {
                craftTimer.setAmount(craftTimer.getAmount() - 1);
                craftTimer.setTime(craft.getTime());
                craftingCountDown(player, craftTimer);
            } else {
                removeCraftTimer(craftTimer);
                player.sendMessage("§a你的 " + craft.getName() + " 完成製作了!");
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
        for (ItemStack item : craftManager.getRecipeList(craft.getRecipe(), player)) {
            int totalAmount = item.getAmount() * craftCount;
            ItemStack returnItem = item.clone();
            returnItem.setAmount(totalAmount);
            player.getInventory().addItem(returnItem);
        }
    }
}
