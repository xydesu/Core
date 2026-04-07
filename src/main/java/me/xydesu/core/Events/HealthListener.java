package me.xydesu.core.Events;

import me.xydesu.core.Player.Player;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class HealthListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof org.bukkit.entity.Player bukkitPlayer)) return;

        Player player = Player.get(bukkitPlayer);
        double currentHealth = getCurrentHealth(event, player);
        player.setCurrentHealth(currentHealth);

        // Handle Death
        if (currentHealth <= 0) {
            // Allow death
            bukkitPlayer.setHealth(0);
        } else {
            // Prevent vanilla damage but update visual health
            event.setDamage(0);
            player.updateVanillaHealth();
        }
    }

    private static double getCurrentHealth(EntityDamageEvent event, Player player) {
        double damage = event.getFinalDamage();

        // Apply Defense (Simple flat reduction for now, can be improved)
        double defense = player.getDefense();
        // Or use a percentage formula: Damage * (100 / (100 + Defense))

        // Let's use a percentage reduction formula common in RPGs
        // DamageMultiplier = 1 - (Defense / (Defense + 100))
        // If Defense is 0, Mult is 1. If Defense is 100, Mult is 0.5.
        if (defense > 0) {
            damage = damage * (100.0 / (100.0 + defense));
        }

        double currentHealth = player.getCurrentHealth();
        currentHealth -= damage;
        return currentHealth;
    }

    @EventHandler
    public void onRegen(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof org.bukkit.entity.Player bukkitPlayer)) return;
        
        Player player = Player.get(bukkitPlayer);
        double amount = event.getAmount();
        
        // Apply to custom health
        player.setCurrentHealth(player.getCurrentHealth() + amount);
        
        // Update visual
        player.updateVanillaHealth();
        
        // Cancel vanilla regen
        event.setCancelled(true);
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = Player.get(event.getPlayer());
        // Ensure max health is set
        if (event.getPlayer().getAttribute(Attribute.MAX_HEALTH) != null) {
            event.getPlayer().getAttribute(Attribute.MAX_HEALTH).setBaseValue(20);
        }
        event.getPlayer().setHealthScale(20);
        player.updateVanillaHealth();
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = Player.get(event.getPlayer());
        player.setCurrentHealth(player.getMaxHealth());
        event.getPlayer().setHealthScale(20);
        player.updateVanillaHealth();
    }
}
