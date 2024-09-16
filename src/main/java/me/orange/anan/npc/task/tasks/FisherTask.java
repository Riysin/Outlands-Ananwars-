package me.orange.anan.npc.task.tasks;

import me.orange.anan.events.TaskCompleteEvent;
import me.orange.anan.npc.task.Task;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FisherTask extends Task {

    public FisherTask() {
        super("fisher", "Fisher", "Catch 5 fish", 5, "漁夫職業", "解鎖漁夫職業");
    }

    @Override
    public void onProgress(Player player, Object data) {
        if (data instanceof ItemStack) {
            ItemStack fish = (ItemStack) data;
            setProgress(getProgress() + fish.getAmount());
            player.sendMessage("Task progress: " + getProgress() + "/" + getGoal());

            if (getProgress() >= getGoal()) {
                Bukkit.getPluginManager().callEvent(new TaskCompleteEvent(player, this));
            }
        }
    }
}
