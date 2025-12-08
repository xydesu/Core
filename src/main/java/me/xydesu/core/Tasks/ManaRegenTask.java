package me.xydesu.core.Tasks;

import me.xydesu.core.Player.Player;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ManaRegenTask extends BukkitRunnable {

    @Override
    public void run() {
        for (org.bukkit.entity.Player bukkitPlayer : Bukkit.getOnlinePlayers()) {
            Player customPlayer = Player.get(bukkitPlayer);
            
            if (customPlayer.getCurrentMana() < customPlayer.getMaxMana()) {
                // Base regen is 2% of max mana
                double baseRegen = customPlayer.getMaxMana() * 0.02;
                // Apply Mana Regen stat as percentage bonus
                double totalRegen = baseRegen * (1 + (customPlayer.getManaRegen() / 100.0));
                
                double newMana = customPlayer.getCurrentMana() + totalRegen;
                customPlayer.setCurrentMana(newMana); // Setter handles capping at maxMana
            }
        }
    }
}
