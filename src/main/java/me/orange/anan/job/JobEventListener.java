package me.orange.anan.job;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.CraftManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

@InjectableComponent
@RegisterAsListener
public class JobEventListener implements Listener {
    private final JobManager jobManager;
    private final CraftManager craftManager;

    public JobEventListener(JobManager jobManager, CraftManager craftManager) {
        this.jobManager = jobManager;
        this.craftManager = craftManager;
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        Job job = jobManager.getJobByID("fisher");
        event.setExpToDrop(0);
        if (!jobManager.hasJob(player) || jobManager.getPlayerCurrentJob(player) != job) {
            return;
        }
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            ItemStack itemStack = ((Item) event.getCaught()).getItemStack();
            int level = jobManager.getPlayerJobLevel(player, job);

            itemStack = job.upgradeSKill(itemStack, player, level);
            ItemStack newItem = craftManager.getItemStack(itemStack, player);
            newItem.setAmount(itemStack.getAmount());

            ((Item) event.getCaught()).setItemStack(ItemBuilder.of(newItem).build());
        }
    }
}
