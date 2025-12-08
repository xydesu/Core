package me.xydesu.core.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class StaminaListener implements Listener {

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            me.xydesu.core.Player.Player customPlayer = me.xydesu.core.Player.Player.get(player);
            
            // Force the food level to match our stamina value
            // This prevents vanilla hunger loss and eating from affecting the bar directly
            // And ensures the bar stays in sync with stamina
            int expectedFoodLevel = (int) ((customPlayer.getCurrentStamina() / customPlayer.getMaxStamina()) * 20);
            event.setFoodLevel(expectedFoodLevel);
        }
    }
}
