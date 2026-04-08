package me.xydesu.core.Events;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.ToolType;
import me.xydesu.core.Utils.Keys;
import me.xydesu.core.Utils.PDC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

/**
 * Handles right-click usage of {@link ToolType#CONSUMABLE} custom items by
 * delegating to the item's {@link Item#onInteract(PlayerInteractEvent)} method.
 */
public class ConsumableListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(PlayerInteractEvent event) {
        ItemStack hand = event.getPlayer().getInventory().getItemInMainHand();
        if (hand == null || hand.getType().isAir()) return;

        String id = PDC.get(hand, Keys.ID, PersistentDataType.STRING, null);
        if (id == null) return;

        for (Item registered : Item.getRegisteredItems()) {
            if (!registered.getID().equals(id)) continue;
            if (registered.getToolType() != ToolType.CONSUMABLE) continue;
            registered.onInteract(event);
            break;
        }
    }
}
