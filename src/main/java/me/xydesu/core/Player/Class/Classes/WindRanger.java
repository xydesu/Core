package me.xydesu.core.Player.Class.Classes;

import me.xydesu.core.Item.ToolType;
import me.xydesu.core.Player.Class.ClassManager;
import me.xydesu.core.Player.Player;

import me.xydesu.core.Core;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import java.util.List;

public class WindRanger extends ClassManager {
    @Override
    public String className() {
        return "WindRanger";
    }

    @Override
    public String classDisplay() {
        return "風行遊俠";
    }

    @Override
    public List<String> classDescription() {
        return List.of(
                "風行遊俠是自然的守護者，",
                "擅長在移動中射出致命箭矢。",
                "他們身手矯健，",
                "讓敵人無法捕捉。");
    }

    @Override
    public void useSkill1(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 1);
        p.spawnParticle(Particle.CLOUD, p.getLocation(), 5, 0.2, 0.2, 0.2, 0.1);

        Arrow arrow = p.launchProjectile(Arrow.class);
        arrow.setKnockbackStrength(3); // High knockback
        arrow.setVelocity(arrow.getVelocity().multiply(1.5));
    }

    @Override
    public String getSkill1Name() {
        return "風之箭";
    }

    @Override
    public List<String> getSkill1Description() {
        return List.of(
                "射出一支帶有風之力的箭矢，",
                "擊退敵人並造成傷害。");
    }

    @Override
    public double getSkill1ManaCost() {
        return 15;
    }

    @Override
    public double getSkill1Cooldown() {
        return 2;
    }

    @Override
    public void useSkill2(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_HORSE_JUMP, 1, 1.5f);

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 3)); // Speed IV for 5s? Or III.

        // Trail effect
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks > 25) { // 50 ticks = 2.5s? Ah, Speed is 100 ticks = 5s.
                    // Let's run trail for 5s (100 ticks).
                }
                if (ticks > 50) { // Let's simplify to 2.5s visual
                    this.cancel();
                    return;
                }
                p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 1, 0.2, 0.1, 0.2, 0);
                ticks++;
            }
        }.runTaskTimer(Core.getPlugin(), 0, 2);
    }

    @Override
    public String getSkill2Name() {
        return "疾風步";
    }

    @Override
    public List<String> getSkill2Description() {
        return List.of(
                "獲得短暫的移動速度提升，",
                "並免疫減速效果。");
    }

    @Override
    public double getSkill2ManaCost() {
        return 25;
    }

    @Override
    public double getSkill2Cooldown() {
        return 10;
    }

    @Override
    public void useSkill3(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 0.8f);

        // Fan shot
        Vector dir = p.getLocation().getDirection();
        for (double angle = -0.3; angle <= 0.3; angle += 0.15) {
            Vector rotated = rotateVector(dir.clone(), angle);
            Arrow arrow = p.launchProjectile(Arrow.class);
            arrow.setVelocity(rotated.multiply(1.2));
            arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
        }
    }

    private Vector rotateVector(Vector vector, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = vector.getX() * cos - vector.getZ() * sin;
        double z = vector.getX() * sin + vector.getZ() * cos;
        return vector.setX(x).setZ(z);
    }

    @Override
    public String getSkill3Name() {
        return "多重箭";
    }

    @Override
    public List<String> getSkill3Description() {
        return List.of(
                "同時射出多支箭矢，",
                "攻擊前方的多個敵人。");
    }

    @Override
    public double getSkill3ManaCost() {
        return 40;
    }

    @Override
    public double getSkill3Cooldown() {
        return 12;
    }

    @Override
    public void useUltimate(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.WEATHER_RAIN, 1, 1);

        Location center = p.getTargetBlock(null, 20).getLocation(); // Target location
        p.getWorld().spawnParticle(Particle.CLOUD, center, 50, 3, 1, 3, 0);

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks > 20) { // 2 seconds rain
                    this.cancel();
                    return;
                }
                for (int i = 0; i < 3; i++) {
                    Location spawnLoc = center.clone().add(
                            ThreadLocalRandom.current().nextDouble(-4, 4),
                            10,
                            ThreadLocalRandom.current().nextDouble(-4, 4));
                    Arrow arrow = p.getWorld().spawn(spawnLoc, Arrow.class);
                    arrow.setShooter(p);
                    arrow.setVelocity(new Vector(0, -1.5, 0)); // Downward
                    arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
                }
                ticks++;
            }
        }.runTaskTimer(Core.getPlugin(), 0, 2);
    }

    @Override
    public String getUltimateName() {
        return "颶風箭雨";
    }

    @Override
    public List<String> getUltimateDescription() {
        return List.of(
                "召喚一陣箭雨，",
                "對範圍內的敵人造成毀滅性傷害。");
    }

    @Override
    public double getUltimateManaCost() {
        return 80;
    }

    @Override
    public double getUltimateCooldown() {
        return 50;
    }

    @Override
    public ToolType getWeaponType() {
        return ToolType.BOW;
    }

    @Override
    public String getRole() {
        return "機動射手";
    }

    @Override
    public String getFightingStyle() {
        return "邊跑邊打 (Kiting)、多重射擊";
    }
}
