package me.xydesu.core.Player.Class.Classes;

import me.xydesu.core.Item.ToolType;
import me.xydesu.core.Player.Class.ClassManager;
import me.xydesu.core.Player.Player;

import me.xydesu.core.Core;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class Arbalestier extends ClassManager {
    @Override
    public String className() {
        return "Arbalestier";
    }

    @Override
    public String classDisplay() {
        return "重砲手";
    }

    @Override
    public List<String> classDescription() {
        return List.of(
                "重砲手精通各種戰術，",
                "擅長使用重弩進行遠程狙擊。",
                "他們善於利用陷阱，",
                "在安全距離外消滅敵人。");
    }

    @Override
    public void useSkill1(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 0.5f);
        p.spawnParticle(Particle.CRIT, p.getLocation().add(0, 1, 0), 5);

        Arrow arrow = p.launchProjectile(Arrow.class);
        arrow.setKnockbackStrength(1);
        arrow.setVelocity(arrow.getVelocity().multiply(2));
        try {
            arrow.setPierceLevel(100); // 1.14+
        } catch (NoSuchMethodError e) {
            // Fallback for older versions if needed, or ignore.
        }
    }

    @Override
    public String getSkill1Name() {
        return "穿透射擊";
    }

    @Override
    public List<String> getSkill1Description() {
        return List.of(
                "射出一支強力的箭矢，",
                "穿透一直線上的所有敵人。");
    }

    @Override
    public double getSkill1ManaCost() {
        return 20;
    }

    @Override
    public double getSkill1Cooldown() {
        return 5;
    }

    @Override
    public void useSkill2(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_TNT_PRIMED, 1, 1);

        Location trapLoc = p.getLocation();
        ArmorStand trap = (ArmorStand) p.getWorld().spawnEntity(trapLoc, EntityType.ARMOR_STAND);
        trap.setVisible(false);
        trap.setSmall(true);
        trap.setGravity(true);
        trap.setGravity(true);
        trap.customName(Component.text("Explosive Trap", NamedTextColor.RED));
        trap.setCustomNameVisible(false); // Only visible if looking closely? Or just hidden.

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (trap.isDead() || ticks > 600) { // 30 seconds max
                    trap.remove();
                    this.cancel();
                    return;
                }

                trap.getWorld().spawnParticle(Particle.CLOUD, trap.getLocation().add(0, 0.5, 0), 1, 0, 0, 0, 0);

                // Check activation
                for (Entity e : trap.getNearbyEntities(1.5, 1.5, 1.5)) {
                    if (e instanceof LivingEntity && e != p && e != trap) {
                        trap.getWorld().createExplosion(trap.getLocation(), 2.0F, false, false); // No block break
                        trap.remove();
                        this.cancel();
                        return;
                    }
                }
                ticks++;
            }
        }.runTaskTimer(Core.getPlugin(), 20, 5); // 1s delay activation? Start immediately (20 ticks delay for setup?)
    }

    @Override
    public String getSkill2Name() {
        return "爆破陷阱";
    }

    @Override
    public List<String> getSkill2Description() {
        return List.of(
                "在地面佈置陷阱，",
                "敵人踩中時造成範圍爆炸。");
    }

    @Override
    public double getSkill2ManaCost() {
        return 30;
    }

    @Override
    public double getSkill2Cooldown() {
        return 12;
    }

    @Override
    public void useSkill3(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1, 1);

        // Leap back
        Vector back = p.getLocation().getDirection().multiply(-1).setY(0.5).normalize().multiply(1.5);
        p.setVelocity(back);

        // Shoot forward
        p.launchProjectile(Arrow.class);
    }

    @Override
    public String getSkill3Name() {
        return "戰術後撤";
    }

    @Override
    public List<String> getSkill3Description() {
        return List.of(
                "向後跳躍並射擊，",
                "拉開與敵人的距離。");
    }

    @Override
    public double getSkill3ManaCost() {
        return 20;
    }

    @Override
    public double getSkill3Cooldown() {
        return 8;
    }

    @Override
    public void useUltimate(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 0.5f);

        Arrow arrow = p.launchProjectile(Arrow.class);
        arrow.setVelocity(arrow.getVelocity().multiply(4.0)); // Super fast
        arrow.setCritical(true);
        try {
            arrow.setGravity(false); // 1.13+
            arrow.setPierceLevel(100);
        } catch (NoSuchMethodError e) {
            // Ignore
        }

        p.spawnParticle(Particle.EXPLOSION, p.getLocation(), 1);
    }

    @Override
    public String getUltimateName() {
        return "致命狙擊";
    }

    @Override
    public List<String> getUltimateDescription() {
        return List.of(
                "瞄準敵人弱點進行狙擊，",
                "造成極高的單體傷害。");
    }

    @Override
    public double getUltimateManaCost() {
        return 100;
    }

    @Override
    public double getUltimateCooldown() {
        return 70;
    }

    @Override
    public ToolType getWeaponType() {
        return ToolType.CROSSBOW;
    }

    @Override
    public String getRole() {
        return "戰術狙擊";
    }

    @Override
    public String getFightingStyle() {
        return "穿透傷害、佈置陷阱、遠程爆發";
    }
}
