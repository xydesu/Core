package me.xydesu.core.Tasks;

import me.xydesu.core.Player.Player;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class StatUpdateTask extends BukkitRunnable {

    @Override
    public void run() {
        for (org.bukkit.entity.Player bukkitPlayer : Bukkit.getOnlinePlayers()) {
            Player customPlayer = Player.get(bukkitPlayer);
            customPlayer.updateStats();
            
            // Ensure current health/mana doesn't exceed max
            if (customPlayer.getCurrentHealth() > customPlayer.getMaxHealth()) {
                customPlayer.setCurrentHealth(customPlayer.getMaxHealth());
            }
            if (customPlayer.getCurrentMana() > customPlayer.getMaxMana()) {
                customPlayer.setCurrentMana(customPlayer.getMaxMana());
            }
        }
    }
}
