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

public class Berserker extends ClassManager {
    @Override
    public String className() {
        return "Berserker";
    }

    @Override
    public String classDisplay() {
        return "狂戰士";
    }

    @Override
    public List<String> classDescription() {
        return List.of(
                "狂戰士擁有無與倫比的破壞力，",
                "擅長使用重型武器碾壓對手。",
                "他們的攻擊勢大力沉，",
                "並且越戰越勇。");
    }

    @Override
    public void useSkill1(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_IRON_GOLEM_ATTACK, 1, 0.5f);
        p.getWorld().spawnParticle(Particle.CRIT, p.getLocation().add(p.getLocation().getDirection()), 10);

        // Frontal Attack
        for (Entity e : p.getNearbyEntities(3, 3, 3)) {
            if (e instanceof LivingEntity && e != p) {
                Vector toTarget = e.getLocation().toVector().subtract(p.getLocation().toVector());
                if (p.getLocation().getDirection().angle(toTarget) < 0.8) {
                    ((LivingEntity) e).damage(8, p);
                    e.setVelocity(p.getLocation().getDirection().normalize().multiply(1.2));
                }
            }
        }
    }

    @Override
    public String getSkill1Name() {
        return "蓄力斬";
    }

    @Override
    public List<String> getSkill1Description() {
        return List.of(
                "蓄力後猛力揮舞巨劍，",
                "造成巨大的單次傷害。");
    }

    @Override
    public double getSkill1ManaCost() {
        return 20;
    }

    @Override
    public double getSkill1Cooldown() {
        return 4;
    }

    @Override
    public void useSkill2(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        p.getWorld().spawnParticle(Particle.EXPLOSION, p.getLocation(), 1);

        for (Entity e : p.getNearbyEntities(4, 2, 4)) {
            if (e instanceof LivingEntity && e != p) {
                ((LivingEntity) e).damage(6, p);
                e.setVelocity(e.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().setY(0.5));
            }
        }
    }

    @Override
    public String getSkill2Name() {
        return "震地猛擊";
    }

    @Override
    public List<String> getSkill2Description() {
        return List.of(
                "重擊地面產生震波，",
                "擊飛周圍的敵人。");
    }

    @Override
    public double getSkill2ManaCost() {
        return 35;
    }

    @Override
    public double getSkill2Cooldown() {
        return 12;
    }

    @Override
    public void useSkill3(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
        p.getWorld().spawnParticle(Particle.CRIT, p.getLocation().add(0, 2, 0), 5);

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1)); // 5 seconds Speed II
        p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 100, 1)); // 5 seconds Strength II
    }

    @Override
    public String getSkill3Name() {
        return "狂暴之怒";
    }

    @Override
    public List<String> getSkill3Description() {
        return List.of(
                "進入狂暴狀態，",
                "大幅提升攻擊力和攻速。");
    }

    @Override
    public double getSkill3ManaCost() {
        return 60;
    }

    @Override
    public double getSkill3Cooldown() {
        return 20;
    }

    @Override
    public void useUltimate(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_RAVAGER_ROAR, 1, 1);

        Vector jump = p.getLocation().getDirection().multiply(1.5).setY(1.2);
        p.setVelocity(jump);

        new BukkitRunnable() {
            @Override
            public void run() {
                @SuppressWarnings("deprecation")
                boolean onGround = p.isOnGround();
                if (onGround) {
                    p.getWorld().spawnParticle(Particle.EXPLOSION, p.getLocation(), 1);
                    p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 0.5f);
                    for (Entity e : p.getNearbyEntities(5, 3, 5)) {
                        if (e instanceof LivingEntity && e != p) {
                            ((LivingEntity) e).damage(20, p);
                            e.setVelocity(new Vector(0, 1, 0));
                        }
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(Core.getPlugin(), 5, 2); // Start checking after 0.25s
    }

    @Override
    public String getUltimateName() {
        return "毀滅打擊";
    }

    @Override
    public List<String> getUltimateDescription() {
        return List.of(
                "躍向空中後猛力下墜，",
                "對大範圍敵人造成毀滅性打擊。");
    }

    @Override
    public double getUltimateManaCost() {
        return 90;
    }

    @Override
    public double getUltimateCooldown() {
        return 55;
    }

    @Override
    public ToolType getWeaponType() {
        return ToolType.GREATSWORD;
    }

    @Override
    public String getRole() {
        return "重型爆發";
    }

    @Override
    public String getFightingStyle() {
        return "蓄力攻擊、範圍傷害、霸體";
    }
}
