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

public class ShadowAssassin extends ClassManager {
    @Override
    public String className() {
        return "ShadowAssassin";
    }

    @Override
    public String classDisplay() {
        return "影舞者";
    }

    @Override
    public List<String> classDescription() {
        return List.of(
                "影舞者是黑夜中的幽靈，",
                "擅長隱匿身形並發動致命一擊。",
                "他們如影隨形，",
                "讓人在恐懼中消亡。");
    }

    @Override
    public void useSkill1(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1, 1);
        p.spawnParticle(Particle.CLOUD, p.getLocation(), 10, 0.5, 0.5, 0.5, 0.1);

        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 1)); // 5 seconds Invisible
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1)); // Speed II

        // Crit logic needs a Listener to check for this state, for now this is just the
        // buff application.
    }

    @Override
    public String getSkill1Name() {
        return "隱身術";
    }

    @Override
    public List<String> getSkill1Description() {
        return List.of(
                "進入隱身狀態，",
                "下一擊必定暴擊。");
    }

    @Override
    public double getSkill1ManaCost() {
        return 30;
    }

    @Override
    public double getSkill1Cooldown() {
        return 15;
    }

    @Override
    public void useSkill2(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_SPIDER_HURT, 1, 1);

        p.setVelocity(p.getLocation().getDirection().multiply(1.5));

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks > 10) {
                    this.cancel();
                    return;
                }
                p.getWorld().spawnParticle(Particle.CRIT, p.getLocation(), 2);
                for (Entity e : p.getNearbyEntities(1, 1, 1)) {
                    if (e instanceof LivingEntity && e != p) {
                        ((LivingEntity) e).damage(4, p);
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 1)); // 3s
                                                                                                              // Poison
                                                                                                              // II
                        this.cancel();
                        return;
                    }
                }
                ticks++;
            }
        }.runTaskTimer(Core.getPlugin(), 0, 1);
    }

    @Override
    public String getSkill2Name() {
        return "毒刃突襲";
    }

    @Override
    public List<String> getSkill2Description() {
        return List.of(
                "突襲敵人並施加劇毒，",
                "造成持續傷害。");
    }

    @Override
    public double getSkill2ManaCost() {
        return 25;
    }

    @Override
    public double getSkill2Cooldown() {
        return 8;
    }

    @Override
    public void useSkill3(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0.5f);
        p.getWorld().spawnParticle(Particle.PORTAL, p.getLocation(), 10, 0.5, 0.5, 0.5, 1);

        // Teleport behind target if looking at one
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
            Vector behind = target.getLocation().getDirection().multiply(-1).normalize().multiply(1.5);
            p.teleport(target.getLocation().add(behind));
            p.sendMessage("§a[暗影步] 已移動至目標身後！");
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 2)); // Speed III for 2s
        } else {
            // Teleport forward if no target
            p.teleport(p.getLocation().add(p.getLocation().getDirection().multiply(5)));
        }
    }

    @Override
    public String getSkill3Name() {
        return "暗影步";
    }

    @Override
    public List<String> getSkill3Description() {
        return List.of(
                "瞬間移動到陰影中，",
                "增加迴避率。");
    }

    @Override
    public double getSkill3ManaCost() {
        return 20;
    }

    @Override
    public double getSkill3Cooldown() {
        return 5;
    }

    @Override
    public void useUltimate(Player player) {
        org.bukkit.entity.Player p = player.getBukkitPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1, 1);

        List<Entity> targets = p.getNearbyEntities(10, 5, 10);
        if (targets.isEmpty()) {
            p.sendMessage("§c[瞬獄影殺陣] 附近沒有敵人！");
            return;
        }

        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (count >= 5 || count >= targets.size()) {
                    this.cancel();
                    return;
                }
                Entity target = targets.get(count % targets.size()); // Cycle through targets? Or just pick unique?
                if (target instanceof LivingEntity && target != p) {
                    p.teleport(target.getLocation());
                    ((LivingEntity) target).damage(10, p);
                    p.getWorld().spawnParticle(Particle.SWEEP_ATTACK, target.getLocation().add(0, 1, 0), 1);
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1, 1.5f);
                }
                count++;
            }
        }.runTaskTimer(Core.getPlugin(), 0, 5); // Attack every 0.25s
    }

    @Override
    public String getUltimateName() {
        return "瞬獄影殺陣";
    }

    @Override
    public List<String> getUltimateDescription() {
        return List.of(
                "化身為多道影子，",
                "對場上所有敵人進行斬擊。");
    }

    @Override
    public double getUltimateManaCost() {
        return 85;
    }

    @Override
    public double getUltimateCooldown() {
        return 60;
    }

    @Override
    public ToolType getWeaponType() {
        return ToolType.DAGGER;
    }

    @Override
    public String getRole() {
        return "敏捷刺客";
    }

    @Override
    public String getFightingStyle() {
        return "隱身、背刺暴擊、中毒";
    }
}
