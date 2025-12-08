package me.xydesu.core.Tasks;

import me.xydesu.core.Player.Player;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class HealthRegenTask extends BukkitRunnable {

    @Override
    public void run() {
        for (org.bukkit.entity.Player bukkitPlayer : Bukkit.getOnlinePlayers()) {
            Player customPlayer = Player.get(bukkitPlayer);
            
            if (customPlayer.getCurrentHealth() < customPlayer.getMaxHealth() && customPlayer.getCurrentHealth() > 0) {
                // Base regen is 1% of max health per second
                double baseRegen = customPlayer.getMaxHealth() * 0.01;
                // Apply Health Regen stat as percentage bonus
                double totalRegen = baseRegen * (1 + (customPlayer.getHealthRegen() / 100.0));
                
                double newHealth = customPlayer.getCurrentHealth() + totalRegen;
                customPlayer.setCurrentHealth(newHealth); // Setter handles capping at maxHealth
                
                // Update vanilla health visual
                updateVanillaHealth(bukkitPlayer, customPlayer);
            }
        }
    }

    private void updateVanillaHealth(org.bukkit.entity.Player bukkitPlayer, Player player) {
        double maxHealth = player.getMaxHealth();
        double currentHealth = player.getCurrentHealth();
        
        if (maxHealth <= 0) maxHealth = 100; // Prevent division by zero
        
        // Scale to 20 hearts
        double vanillaHealth = (currentHealth / maxHealth) * 20;
        if (vanillaHealth < 1 && currentHealth > 0) vanillaHealth = 1; // Keep at least half heart if alive
        if (vanillaHealth > 20) vanillaHealth = 20;
        if (vanillaHealth < 0) vanillaHealth = 0;
        
        bukkitPlayer.setHealth(vanillaHealth);
    }
}
