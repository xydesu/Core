package me.xydesu.core.Events;

import me.xydesu.core.Mob.CustomMob;
import me.xydesu.core.Player.Player;
import me.xydesu.core.Utils.Keys;
import me.xydesu.core.Utils.PDC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import me.xydesu.core.Core;

import java.util.List;

public class MobDeathListener implements Listener {

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if (CustomMob.isCustomMob(entity)) {
            // Remove Custom Name display
            TextDisplay nameDisplay = CustomMob.removeNameDisplay(entity);
            if (nameDisplay != null) nameDisplay.remove();

            // Disable vanilla EXP drop
            event.setDroppedExp(0);
            
            // Clear vanilla drops
            event.getDrops().clear();

            org.bukkit.entity.Player killer = entity.getKiller();
            if (killer == null && entity.getLastDamageCause() instanceof EntityDamageByEntityEvent damageEvent) {
                 if (damageEvent.getDamager() instanceof org.bukkit.entity.Player p) {
                     killer = p;
                 }
            }

            // Handle Custom Drops and EXP via registered CustomMob
            String id = PDC.get(entity, Keys.ID, PersistentDataType.STRING);
            CustomMob mob = (id != null) ? CustomMob.get(id) : null;

            if (mob != null) {
                List<ItemStack> drops = mob.getDrops();
                event.getDrops().addAll(drops);
            }

            if (killer != null) {
                // Check for Reaper of Fate Passive
                ItemStack mainHand = killer.getInventory().getItemInMainHand();
                String itemId = PDC.get(mainHand, Keys.ID, PersistentDataType.STRING);
                if ("Reaper_of_Fate".equals(itemId)) {
                    if (Math.random() < 0.25) {
                        Player customPlayer = Player.get(killer);
                        // Heal 5 HP
                        double newHealth = Math.min(customPlayer.getMaxHealth(), customPlayer.getCurrentHealth() + 5);
                        customPlayer.setCurrentHealth(newHealth);
                        
                        // Attack Speed Buff (+10% for 3s)
                        customPlayer.setTempAttackSpeedBonus(0.10);
                        killer.sendMessage(Component.text("亡魂抽取！恢復生命並提升攻速！", NamedTextColor.GREEN));
                        
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                customPlayer.setTempAttackSpeedBonus(0);
                            }
                        }.runTaskLater(Core.getPlugin(), 60L); // 3 seconds
                    }
                }

                double exp = (mob != null) ? mob.getDroppedExp() : 0;

                // Fallback if no registered mob found (e.g. generic custom mob)
                if (exp == 0) {
                    int level = PDC.get(entity, Keys.LEVEL, PersistentDataType.INTEGER, 1);
                    exp = level * 10;
                }

                // Give EXP to player
                Player.get(killer).addExp(exp);
                killer.sendMessage(Component.text("獲得 " + (int)exp + " 經驗值", NamedTextColor.GREEN));
            }
        }
    }
}
