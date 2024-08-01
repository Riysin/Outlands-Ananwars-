package me.orange.anan.craft;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import io.fairyproject.scheduler.response.TaskResponse;
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

    public void addCraftTimer(Player player, Craft craft) {
        CraftTimer craftTimer = new CraftTimer(player, craft, craft.getTime());
        craftTimerList.add(craftTimer);

        CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            if (craftTimer.isFailed()) {
                return TaskResponse.failure("cancelled");
            }
            craftTimer.setTime(craftTimer.getTime() - 1);
            return TaskResponse.continueTask();
        }, 0, 20, RepeatPredicate.length(Duration.ofSeconds(craft.getTime()))).getFuture();

        future.thenRun(() -> {
            player.getInventory().addItem(craft.getItemStack());
            player.sendMessage("crafting finished");
            removeCraftTimer(craftTimer);
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
        removeCraftTimer(craftTimer);
        player.sendMessage("stoped");
        returnItems(player, craftTimer.getCraft());
    }

    //return player items back if crafting failed
    public void returnItems(Player player, Craft craft) {
        for (ItemStack item : craft.getRecipe()) {
            player.getInventory().addItem(item);
        }
    }
}
