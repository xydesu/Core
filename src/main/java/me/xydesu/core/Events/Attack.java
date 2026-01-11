package me.xydesu.core.Events;

import me.xydesu.core.Core;
import me.xydesu.core.Mob.CustomMob;
import me.xydesu.core.Utils.DamageCalc;
import me.xydesu.core.Utils.Keys;
import me.xydesu.core.Utils.PDC;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.bukkit.Particle;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.HashMap;

public class Attack implements Listener {

    private Map<Entity, DamageCalc.DamageResult> pendingRangedDamage = new HashMap<>();

    // Disable Damages
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            ItemStack item = player.getInventory().getItemInMainHand();

            if (!me.xydesu.core.Item.Item.canUse(me.xydesu.core.Player.Player.get(player), item)) {
                player.sendMessage(Component.text("§c無法使用此武器 (職業不符)"));
                event.setCancelled(true);
                return;
            }
            double damage;
            boolean isCrit = false;

            if (pendingRangedDamage.containsKey(event.getEntity())) {
                DamageCalc.DamageResult result = pendingRangedDamage.remove(event.getEntity());
                damage = result.damage;
                isCrit = result.isCrit;
                event.setDamage(damage);
            } else {
                if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK))
                    return;
                DamageCalc.DamageResult result = DamageCalc.getDamage(player, item, event.getEntity());
                damage = result.damage;
                isCrit = result.isCrit;
                event.setDamage(damage);

                boolean hasAOE = PDC.get(item, Keys.AOE, PersistentDataType.BOOLEAN, false);
                if (hasAOE) {
                    double range = 3;
                    range += PDC.get(item, Keys.RANGE, PersistentDataType.DOUBLE, 0.0);
                    performAOE(player, item, range, event.getEntity(), player.getCooledAttackStrength(0.0F));
                }
            }

            // Handle Custom Mob Damage
            if (event.getEntity() instanceof LivingEntity livingEntity && CustomMob.isCustomMob(livingEntity)) {
                boolean isDead = CustomMob.damage(livingEntity, damage, false);
                if (isDead) {
                    event.setDamage(livingEntity.getHealth() + 100); // Ensure death via event
                } else {
                    event.setDamage(0); // Disable vanilla damage
                }
            }

            spawnDamageIndicator(event.getEntity(), damage, isCrit);
        } else if (event.getDamager() instanceof AbstractArrow arrow && arrow.getShooter() instanceof Player player) {
            ItemStack item = player.getInventory().getItemInMainHand();

            if (!me.xydesu.core.Item.Item.canUse(me.xydesu.core.Player.Player.get(player), item)) {
                player.sendMessage(Component.text("§c無法使用此武器 (職業不符)"));
                event.setCancelled(true);
                return;
            }

            DamageCalc.DamageResult result = DamageCalc.getRangedDamage(player, item, arrow, event.getEntity());
            double damage = result.damage;
            boolean isCrit = result.isCrit;

            if (damage > 0) {
                event.setDamage(damage);

                // Handle Custom Mob Damage
                if (event.getEntity() instanceof LivingEntity livingEntity && CustomMob.isCustomMob(livingEntity)) {
                    boolean isDead = CustomMob.damage(livingEntity, damage, false);
                    if (isDead) {
                        event.setDamage(livingEntity.getHealth() + 100);
                    } else {
                        event.setDamage(0);
                    }
                }

                spawnDamageIndicator(event.getEntity(), damage, isCrit);
            }
        }
    }

    private void spawnDamageIndicator(Entity entity, double damage, boolean isCrit) {
        World world = entity.getWorld();

        // Random position around the entity
        double angle = Math.random() * Math.PI * 2;
        double radius = 0.8;
        double x = Math.cos(angle) * radius;
        double z = Math.sin(angle) * radius;

        Location location = entity.getLocation().add(x, entity.getHeight() * 0.5 + 0.5, z);

        TextDisplay display = world.spawn(location, TextDisplay.class);

        Component damageText;
        if (isCrit) {
            damageText = Component
                    .text("✧ " + Math.round(damage) + " ✧", net.kyori.adventure.text.format.NamedTextColor.GOLD)
                    .decoration(net.kyori.adventure.text.format.TextDecoration.BOLD, true);
            // Add crit particles
            world.spawnParticle(Particle.CRIT, location, 10, 0.2, 0.2, 0.2, 0.1);
        } else {
            damageText = Component.text(Math.round(damage), net.kyori.adventure.text.format.NamedTextColor.RED)
                    .decoration(net.kyori.adventure.text.format.TextDecoration.BOLD, true);
        }

        display.text(damageText);
        display.setAlignment(TextDisplay.TextAlignment.CENTER);
        display.setBillboard(Display.Billboard.CENTER);
        display.setSeeThrough(false);
        display.setShadowed(true);
        display.setBackgroundColor(org.bukkit.Color.fromARGB(0, 0, 0, 0));

        // Scale up for crit
        if (isCrit) {
            org.bukkit.util.Transformation transformation = display.getTransformation();
            transformation.getScale().set(1.5f, 1.5f, 1.5f);
            display.setTransformation(transformation);
        }

        new org.bukkit.scheduler.BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= 15 || !display.isValid()) {
                    display.remove();
                    this.cancel();
                    return;
                }
                display.teleport(display.getLocation().add(0, 0.08, 0));
                ticks++;
            }
        }.runTaskTimer(Core.getPlugin(), 0L, 1L);
    }

    // Range Attack
    @EventHandler
    public void onAttack(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK)
            return;
        Player player = event.getPlayer();

        if (!me.xydesu.core.Item.Item.canUse(me.xydesu.core.Player.Player.get(player),
                player.getInventory().getItemInMainHand())) {
            player.sendMessage(Component.text("§c無法使用此武器 (職業不符)"));
            return;
        }

        Location start = player.getEyeLocation();
        Vector vector = start.getDirection();

        double range = 3;
        range += PDC.get(player.getInventory().getItemInMainHand(), Keys.RANGE, PersistentDataType.DOUBLE, 0.0);

        World world = player.getWorld();

        boolean hasAOE = PDC.get(player.getInventory().getItemInMainHand(), Keys.AOE, PersistentDataType.BOOLEAN,
                false);

        if (hasAOE) {
            ItemStack item = player.getInventory().getItemInMainHand();
            performAOE(player, item, range, null, player.getCooledAttackStrength(0.0F));
            return;
        }

        @Nullable
        RayTraceResult entityResult = world.rayTraceEntities(start, vector, range, entity -> entity != player);
        @Nullable
        RayTraceResult blockResult = world.rayTraceBlocks(start, vector, range);

        if (entityResult != null && entityResult.getHitEntity() != null) {
            double entityDistance = entityResult.getHitPosition().distance(start.toVector());
            Entity hitEntity = entityResult.getHitEntity();

            if (blockResult != null && blockResult.getHitBlock() != null) {
                // 取得擊中方塊的距離
                double blockDistance = blockResult.getHitPosition().distance(start.toVector());

                // 只有當實體比方塊更近時，才視為有效目標
                if (entityDistance < blockDistance) {
                    // Hit
                    ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

                    DamageCalc.DamageResult result = DamageCalc.getDamage(player, item, hitEntity);
                    double damage = result.damage;

                    LivingEntity hitEntity1 = (LivingEntity) hitEntity;
                    pendingRangedDamage.put(hitEntity1, result);
                    hitEntity1.damage(damage, player);
                }
            } else {
                // Hit
                ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

                DamageCalc.DamageResult result = DamageCalc.getDamage(player, item, hitEntity);
                double damage = result.damage;

                LivingEntity hitEntity1 = (LivingEntity) hitEntity;
                pendingRangedDamage.put(hitEntity1, result);
                hitEntity1.damage(damage, player);
            }
        }
    }

    private void performAOE(Player player, ItemStack item, double range, @Nullable Entity ignoredEntity,
            float attackStrength) {
        World world = player.getWorld();
        Location start = player.getEyeLocation();
        Vector vector = start.getDirection();

        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (entity instanceof LivingEntity target && entity != player && !entity.equals(ignoredEntity)) {
                Vector toTarget = target.getLocation().add(0, 0.5, 0).toVector().subtract(start.toVector());
                if (toTarget.length() <= range && vector.angle(toTarget) <= Math.toRadians(60)) {
                    RayTraceResult wallCheck = world.rayTraceBlocks(start, toTarget.clone().normalize(),
                            toTarget.length());
                    if (wallCheck == null || wallCheck.getHitBlock() == null) {
                        DamageCalc.DamageResult result = DamageCalc.getDamage(player, item, target, true,
                                attackStrength);
                        double damage = result.damage;
                        pendingRangedDamage.put(target, result);
                        target.damage(damage, player);
                    }
                }
            }
        }
    }

}
