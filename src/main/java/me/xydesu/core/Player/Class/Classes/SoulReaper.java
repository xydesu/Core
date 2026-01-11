package me.xydesu.core.Player.Class.Classes;

import me.xydesu.core.Item.ToolType;
import me.xydesu.core.Player.Class.ClassManager;
import me.xydesu.core.Player.Player;

import me.xydesu.core.Core;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class SoulReaper extends ClassManager {
    @Override
    public String className() {
        return "SoulReaper";
    }

    @Override
    public String classDisplay() {
        return "靈魂收割者";
    }

    @Override
    public List<String> classDescription() {
        return List.of(
                "靈魂收割者遊走於生死邊緣，",
                "擅長奪取敵人的生命精華。",
                "他們的鐮刀揮舞之處，",
                "萬物皆將凋零。");
    }

    @Override
    public void useSkill1(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1, 0.5f);
        p.getWorld().spawnParticle(Particle.SWEEP_ATTACK, p.getLocation().add(0, 1, 0), 10, 1, 0.5, 1, 0);

        for (Entity e : p.getNearbyEntities(3.5, 2, 3.5)) {
            if (e instanceof LivingEntity && e != p) {
                Vector toTarget = e.getLocation().toVector().subtract(p.getLocation().toVector());
                if (p.getLocation().getDirection().angle(toTarget) < 1.0) { // Wide cone
                    ((LivingEntity) e).damage(8, p);
                }
            }
        }
    }

    @Override
    public String getSkill1Name() {
        return "靈魂收割";
    }

    @Override
    public List<String> getSkill1Description() {
        return List.of(
                "揮舞鐮刀收割靈魂，",
                "對前方扇形區域造成傷害。");
    }

    @Override
    public double getSkill1ManaCost() {
        return 30;
    }

    @Override
    public double getSkill1Cooldown() {
        return 4;
    }

    @Override
    public void useSkill2(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1, 0.5f);
        p.getWorld().spawnParticle(Particle.PORTAL, p.getLocation(), 20, 3, 1, 3, 0);

        for (Entity e : p.getNearbyEntities(5, 3, 5)) {
            if (e instanceof LivingEntity && e != p) {
                LivingEntity le = (LivingEntity) e;
                le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1));
                le.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, 1));
                le.damage(2, p);
                // Fear mechanic (running away) is complex, let's stick to debuffs for now.
            }
        }
    }

    @Override
    public String getSkill2Name() {
        return "恐懼光環";
    }

    @Override
    public List<String> getSkill2Description() {
        return List.of(
                "散發恐懼氣息，",
                "使周圍敵人陷入恐懼狀態。");
    }

    @Override
    public double getSkill2ManaCost() {
        return 40;
    }

    @Override
    public double getSkill2Cooldown() {
        return 12;
    }

    @Override
    public void useSkill3(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();

        // Find single target
        LivingEntity target = null;
        for (Entity e : p.getNearbyEntities(8, 8, 8)) {
            if (e instanceof LivingEntity && e != p) {
                Vector toTarget = e.getLocation().toVector().subtract(p.getLocation().toVector());
                if (p.getLocation().getDirection().angle(toTarget) < 0.3) {
                    target = (LivingEntity) e;
                    break;
                }
            }
        }

        if (target != null) {
            p.playSound(p.getLocation(), Sound.ENTITY_GUARDIAN_ATTACK, 1, 1);
            final LivingEntity fTarget = target;
            new BukkitRunnable() {
                int ticks = 0;

                @Override
                public void run() {
                    if (ticks > 5 || fTarget.isDead() || p.getLocation().distance(fTarget.getLocation()) > 10) {
                        this.cancel();
                        return;
                    }

                    // Beam effect
                    double dist = p.getLocation().distance(fTarget.getLocation());
                    Vector dir = fTarget.getLocation().toVector().subtract(p.getLocation().toVector()).normalize()
                            .multiply(0.5);
                    for (double i = 0; i < dist; i += 0.5) {
                        p.getWorld().spawnParticle(Particle.FLAME,
                                p.getLocation().add(0, 1, 0).add(dir.clone().multiply(i * 2)), 1);
                    }

                    fTarget.damage(3, p);
                    @SuppressWarnings("deprecation")
                    double maxHealth = p.getMaxHealth(); // Use deprecated method for compatibility if Attribute fails
                    double newHealth = Math.min(maxHealth, p.getHealth() + 2);
                    p.setHealth(newHealth);

                    ticks++;
                }
            }.runTaskTimer(Core.getPlugin(), 0, 4); // Every 0.2s
        }
    }

    @Override
    public String getSkill3Name() {
        return "生命汲取";
    }

    @Override
    public List<String> getSkill3Description() {
        return List.of(
                "連結敵人並汲取生命，",
                "持續造成傷害並恢復自身血量。");
    }

    @Override
    public double getSkill3ManaCost() {
        return 50;
    }

    @Override
    public double getSkill3Cooldown() {
        return 8;
    }

    @Override
    public void useUltimate(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 0.5f);

        // Float up visual
        p.setVelocity(new Vector(0, 0.5, 0));
        p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 20, 1, 1, 1, 0.1);

        new BukkitRunnable() {
            @Override
            public void run() {
                p.getWorld().spawnParticle(Particle.SWEEP_ATTACK, p.getLocation().add(0, 1, 0), 10, 5, 1, 5, 0);
                for (Entity e : p.getNearbyEntities(6, 4, 6)) {
                    if (e instanceof LivingEntity && e != p) {
                        LivingEntity le = (LivingEntity) e;
                        @SuppressWarnings("deprecation")
                        double max = le.getMaxHealth();
                        if (le.getHealth() < max * 0.3) {
                            le.setHealth(0); // Execute
                            p.sendMessage("§c[斬殺] 處決了 " + le.getName() + "！");
                        } else {
                            le.damage(15, p);
                        }
                    }
                }
            }
        }.runTaskLater(Core.getPlugin(), 10);
    }

    @Override
    public String getUltimateName() {
        return "死神降臨";
    }

    @Override
    public List<String> getUltimateDescription() {
        return List.of(
                "化身為死神本尊，",
                "斬殺周圍所有低血量敵人。");
    }

    @Override
    public double getUltimateManaCost() {
        return 100;
    }

    @Override
    public double getUltimateCooldown() {
        return 50;
    }

    @Override
    public ToolType getWeaponType() {
        return ToolType.SCYTHE;
    }

    @Override
    public String getRole() {
        return "異能近戰";
    }

    @Override
    public String getFightingStyle() {
        return "吸血、詛咒、斬殺低血量敵人";
    }
}
