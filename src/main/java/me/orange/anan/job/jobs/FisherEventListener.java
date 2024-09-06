package me.orange.anan.job.jobs;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.orange.anan.craft.CraftManager;
import me.orange.anan.events.PlayerLevelUpEvent;
import me.orange.anan.job.Job;
import me.orange.anan.job.JobManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@InjectableComponent
@RegisterAsListener
public class FisherEventListener implements Listener {
    private final JobManager jobManager;
    private final CraftManager craftManager;

    public FisherEventListener(JobManager jobManager, CraftManager craftManager) {
        this.jobManager = jobManager;
        this.craftManager = craftManager;
    }

    @EventHandler
    public void onHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Job job = jobManager.getJobByID("fisher");

        if (jobManager.hasJob(player) && jobManager.getPlayerCurrentJob(player) == job) {
            ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
            ItemStack previousItem = player.getInventory().getItem(event.getPreviousSlot());

            if (newItem != null && newItem.getType() == Material.FISHING_ROD) {
                int currentLureLevel = newItem.getEnchantmentLevel(Enchantment.LURE);
                newItem.addUnsafeEnchantment(Enchantment.LURE, currentLureLevel + 1);
            }

            if (previousItem != null && previousItem.getType() == Material.FISHING_ROD) {
                int currentLureLevel = previousItem.getEnchantmentLevel(Enchantment.LURE);
                if (currentLureLevel > 1) {
                    previousItem.addUnsafeEnchantment(Enchantment.LURE, currentLureLevel - 1);
                } else {
                    previousItem.removeEnchantment(Enchantment.LURE);
                }
            }
        }
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
            Fisher fisher = (Fisher) job;
            ItemStack itemStack = ((Item) event.getCaught()).getItemStack().clone();
            int level = jobManager.getPlayerJobLevel(player, job);
            boolean upgrade = fisher.upgradeSKill(level);
            boolean skill2 = fisher.skill2(player);
            ((Item) event.getCaught()).setItemStack(getFish(player, itemStack, upgrade, skill2));
        }
    }

    private ItemStack getFish(Player player, ItemStack itemStack, boolean upgrade, boolean skill2) {
        List<ItemStack> fishList = new ArrayList<>();
        fishList.add(craftManager.getItemStack(craftManager.getConfigItemWithID("fish"),player));
        fishList.add(craftManager.getItemStack(craftManager.getConfigItemWithID("salmon"),player));
        fishList.add(craftManager.getItemStack(craftManager.getConfigItemWithID("pufferfish"),player));
        fishList.add(craftManager.getItemStack(craftManager.getConfigItemWithID("tropicalFish"),player));

        if (skill2) {
            itemStack.setType(XMaterial.EMERALD.parseMaterial());
            player.sendMessage("§a你的技能讓你捕到了一個寶物!");
        }

        ItemStack newItem = craftManager.getItemStack(itemStack, player);

        if (upgrade) {
            player.sendMessage("§a雙倍捕魚讓你捕到了兩倍的物品!");
            newItem.setAmount(2);
        }

        return newItem;
    }

    @EventHandler
    public void onPlayerInWater(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Job job = jobManager.getJobByID("fisher");

        if (!jobManager.hasJob(player) || jobManager.getPlayerCurrentJob(player) != job) {
            return;
        }
        Fisher fisher = (Fisher) job;
        if(fisher.skill3(player)) {
            player.sendMessage("§a你的技能讓你在水中移動時不會受到阻礙!");
            player.setVelocity(player.getLocation().getDirection().multiply(0.5));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 20 * 60 * 5, 0));
        }
    }
}
