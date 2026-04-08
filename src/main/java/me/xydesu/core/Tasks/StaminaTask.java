package me.xydesu.core.Tasks;

import me.xydesu.core.Player.Player;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class StaminaTask extends BukkitRunnable {

    @Override
    public void run() {
        for (org.bukkit.entity.Player bukkitPlayer : Bukkit.getOnlinePlayers()) {
            Player customPlayer = Player.get(bukkitPlayer);
            
            double maxStamina = customPlayer.getMaxStamina();
            double currentStamina = customPlayer.getCurrentStamina();
            
            if (bukkitPlayer.isSprinting()) {
                // Consume stamina
                // Consume 0.25 stamina every 4 ticks (1.25 per second)
                // With 20 base stamina, that's 16 seconds of sprinting.
                // With vitality bonuses, it will be longer.
                double consumption = 0.25; 
                
                currentStamina -= consumption;
                if (currentStamina <= 0) {
                    currentStamina = 0;
                    bukkitPlayer.setSprinting(false);
                }
            } else {
                // Regenerate stamina
                // Base regen: 1 stamina per second (0.2 per 4 ticks)
                double regen = 0.2;
                
                // Bonus from Vitality?
                // Let's say 1% of max stamina per tick check
                regen += maxStamina * 0.01;
                
                if (currentStamina < maxStamina) {
                    currentStamina += regen;
                    if (currentStamina > maxStamina) currentStamina = maxStamina;
                }
            }
            
            customPlayer.setCurrentStamina(currentStamina);
        }
    }
}
