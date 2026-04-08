package me.xydesu.core.Item.Items.Misc;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TestConsumable extends Item {

    @Override
    public String getID() {
        return "TEST_CONSUMABLE";
    }

    @Override
    public String getName() {
        return "測試消耗品";
    }

    @Override
    public Material getMaterial() {
        return Material.APPLE;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.CONSUMABLE;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public List<String> getLore() {
        return List.of(
                "右鍵點擊使用。",
                "效果：速度 II (30秒)、生命提升 I (30秒)"
        );
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        org.bukkit.event.block.Action action = event.getAction();
        if (action != org.bukkit.event.block.Action.RIGHT_CLICK_AIR
                && action != org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();

        // Apply temporary potion buffs (600 ticks = 30 seconds)
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 1, false, true, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 600, 0, false, true, true));

        // Consume one item
        org.bukkit.inventory.ItemStack hand = player.getInventory().getItemInMainHand();
        hand.setAmount(hand.getAmount() - 1);

        player.sendMessage("§a你使用了 §e測試消耗品§a！速度 II 與生命提升 I 持續 30 秒。");
        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1f, 1f);

        event.setCancelled(true);
    }
}
