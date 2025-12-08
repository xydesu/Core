package me.xydesu.core.Events;

import me.xydesu.core.Player.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class ExpListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player.get(event.getPlayer()).updateVanillaExp();
    }

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent event) {
        // Disable vanilla XP gain
        event.setAmount(0);
        
        // Ensure the bar stays correct
        Player.get(event.getPlayer()).updateVanillaExp();
    }
}
