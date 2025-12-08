package me.xydesu.core.Events;

import me.xydesu.core.Core;
import me.xydesu.core.Player.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDataListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = Player.get(event.getPlayer());
        Core.getDatabaseManager().loadPlayer(player);
        
        // Update stats first to calculate correct max values
        player.updateStats();
        
        // Set to full status
        player.setCurrentHealth(player.getMaxHealth());
        player.setCurrentStamina(player.getMaxStamina());
        player.setCurrentMana(player.getMaxMana());

        player.getBukkitPlayer().setHealth(20);
        player.getBukkitPlayer().setSaturation(20);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = Player.get(event.getPlayer());
        Core.getDatabaseManager().savePlayer(player);
    }
}
