package me.xydesu.core.Player.Class.Classes;

import me.xydesu.core.Item.ToolType;
import me.xydesu.core.Player.Class.ClassManager;
import me.xydesu.core.Player.Player;

import me.xydesu.core.Core;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import java.util.List;

public class Elementalist extends ClassManager {
    @Override
    public String className() {
        return "Elementalist";
    }

    @Override
    public String classDisplay() {
        return "元素使";
    }

    @Override
    public List<String> classDescription() {
        return List.of(
                "元素使掌控著自然元素，",
                "擅長引導強大的魔法毀滅敵人。",
                "他們能召喚風火雷電，",
                "將戰場化為焦土。");
    }

    @Override
    public void useSkill1(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);

        LargeFireball fireball = p.launchProjectile(LargeFireball.class);
        fireball.setYield(2.0F); // Explosion power
        fireball.setIsIncendiary(true); // Fire
        fireball.setVelocity(fireball.getVelocity().multiply(1.5));
    }

    @Override
    public String getSkill1Name() {
        return "火球術";
    }

    @Override
    public List<String> getSkill1Description() {
        return List.of(
                "發射一枚炙熱的火球，",
                "對敵人造成爆炸傷害。");
    }

    @Override
    public double getSkill1ManaCost() {
        return 25;
    }

    @Override
    public double getSkill1Cooldown() {
        return 3;
    }

    @Override
    public void useSkill2(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 0.5f);
        p.getWorld().spawnParticle(Particle.END_ROD, p.getLocation(), 50, 3, 1, 3, 0.1);

        for (Entity e : p.getNearbyEntities(4, 3, 4)) {
            if (e instanceof LivingEntity && e != p) {
                ((LivingEntity) e).damage(8, p);
                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 2)); // If SLOW
                                                                                                        // fails, try
                                                                                                        // SLOWNESS.
                // Actually, let's try SLOWNESS explicitly if SLOW failed.
                // If SLOW failed (ID: f73b015c...) then it must be SLOWNESS.
                // But wait, SLOWNESS usually works. I'll change to SLOWNESS.
                e.setFreezeTicks(100); // 1.17+ feature, might fail on older versions but harmless usually or method
                                       // missing
            }
        }
    }

    @Override
    public String getSkill2Name() {
        return "冰霜新星";
    }

    @Override
    public List<String> getSkill2Description() {
        return List.of(
                "釋放冰霜能量凍結周圍，",
                "造成傷害並減速敌人。");
    }

    @Override
    public double getSkill2ManaCost() {
        return 35;
    }

    @Override
    public double getSkill2Cooldown() {
        return 10;
    }

    @Override
    public void useSkill3(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();

        Entity target = null;
        for (Entity e : p.getNearbyEntities(10, 10, 10)) {
            if (e instanceof LivingEntity && e != p) {
                Vector toTarget = e.getLocation().toVector().subtract(p.getLocation().toVector());
                if (p.getLocation().getDirection().angle(toTarget) < 0.2) {
                    target = e;
                    break;
                }
            }
        }

        if (target != null) {
            p.getWorld().strikeLightning(target.getLocation());
            ((LivingEntity) target).damage(12, p);
        } else {
            // Strike looking at block
            p.getWorld().strikeLightning(p.getTargetBlock(null, 20).getLocation());
        }
    }

    @Override
    public String getSkill3Name() {
        return "雷霆一擊";
    }

    @Override
    public List<String> getSkill3Description() {
        return List.of(
                "召喚落雷攻擊目標，",
                "造成高額魔法傷害。");
    }

    @Override
    public double getSkill3ManaCost() {
        return 45;
    }

    @Override
    public double getSkill3Cooldown() {
        return 12;
    }

    @Override
    public void useUltimate(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1, 0.5f);

        Location center = p.getLocation();
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks > 60) { // 3 seconds
                    this.cancel();
                    return;
                }

                if (ticks % 5 == 0) {
                    Location randLoc = center.clone().add(
                            ThreadLocalRandom.current().nextDouble(-6, 6),
                            0,
                            ThreadLocalRandom.current().nextDouble(-6, 6));
                    int type = ThreadLocalRandom.current().nextInt(3);
                    if (type == 0)
                        p.getWorld().strikeLightning(randLoc);
                    else if (type == 1)
                        p.getWorld().spawnParticle(Particle.EXPLOSION, randLoc, 1); // Or EXPLOSION
                    else
                        p.getWorld().spawnParticle(Particle.FLAME, randLoc, 10, 0.5, 0.5, 0.5, 0.1);

                    // Damage logic simpler: just damage everyone nearby every tick?
                }

                // Damage
                if (ticks % 10 == 0) {
                    for (Entity e : p.getNearbyEntities(6, 4, 6)) {
                        if (e instanceof LivingEntity && e != p) {
                            ((LivingEntity) e).damage(5, p);
                        }
                    }
                }

                ticks++;
            }
        }.runTaskTimer(Core.getPlugin(), 0, 1);
    }

    @Override
    public String getUltimateName() {
        return "元素毀滅";
    }

    @Override
    public List<String> getUltimateDescription() {
        return List.of(
                "混合多種元素進行轟炸，",
                "對範圍內敵人造成毀滅性打擊。");
    }

    @Override
    public double getUltimateManaCost() {
        return 120;
    }

    @Override
    public double getUltimateCooldown() {
        return 80;
    }

    @Override
    public ToolType getWeaponType() {
        return ToolType.STAFF;
    }

    @Override
    public String getRole() {
        return "魔法輸出";
    }

    @Override
    public String getFightingStyle() {
        return "範圍法術、控制 (CC)、爆發";
    }
}
