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
                // Non-linear stamina consumption: drain accelerates as stamina depletes.
                // Base rate is 0.25 per 4 ticks. A quadratic curve on the depletion ratio
                // keeps drain low while stamina is plentiful and ramps it up as the player
                // tires, giving a more natural "fatigue" feel rather than a flat countdown.
                // Formula: consumption = BASE * (1 + (1 - ratio)^2)
                //   ratio=1.0 (full)  → 0.25 * 1.00 = 0.25
                //   ratio=0.5 (half)  → 0.25 * 1.25 = 0.3125
                //   ratio=0.0 (empty) → 0.25 * 2.00 = 0.50
                final double BASE_CONSUMPTION = 0.25;
                double ratio = (maxStamina > 0) ? currentStamina / maxStamina : 0;
                double consumption = BASE_CONSUMPTION * (1.0 + Math.pow(1.0 - ratio, 2.0));

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
