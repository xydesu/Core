package me.xydesu.core.Player.Class;

import me.xydesu.core.Item.ToolType;
import me.xydesu.core.Player.Class.Classes.*;
import me.xydesu.core.Player.Player;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class ClassManager {

    private static final Map<String, ClassManager> classes = new HashMap<>();
    // Cooldown Map: PlayerUUID -> (SkillIndex -> TimeStamp)
    private final Map<UUID, Map<Integer, Long>> cooldowns = new HashMap<>();

    static {
        classes.put("BladeMaster", new BladeMaster());
        classes.put("Berserker", new Berserker());
        classes.put("ShadowAssassin", new ShadowAssassin());
        classes.put("WindRanger", new WindRanger());
        classes.put("Arbalestier", new Arbalestier());
        classes.put("Elementalist", new Elementalist());
        classes.put("SoulReaper", new SoulReaper());
    }

    public static ClassManager get(String name) {
        return classes.get(name);
    }

    public static List<ClassManager> getAllClasses() {
        return new ArrayList<>(classes.values());
    }

    public abstract String className();

    public abstract String classDisplay();

    public abstract List<String> classDescription();

    // Skill 1
    public void castSkill1(Player player) {
        if (checkCooldown(player, 1) && checkMana(player, getSkill1ManaCost())) {
            player.getBukkitPlayer().sendMessage("§a[技能] §7你使用了 §f" + getSkill1Name() + "§7！");
            useSkill1(player);
            setCooldown(player, 1, getSkill1Cooldown());
            deductMana(player, getSkill1ManaCost());
        }
    }

    public abstract void useSkill1(Player player);

    public abstract String getSkill1Name();

    public abstract List<String> getSkill1Description();

    public abstract double getSkill1ManaCost();

    public abstract double getSkill1Cooldown();

    // Skill 2
    public void castSkill2(Player player) {
        if (checkCooldown(player, 2) && checkMana(player, getSkill2ManaCost())) {
            player.getBukkitPlayer().sendMessage("§a[技能] §7你使用了 §f" + getSkill2Name() + "§7！");
            useSkill2(player);
            setCooldown(player, 2, getSkill2Cooldown());
            deductMana(player, getSkill2ManaCost());
        }
    }

    public abstract void useSkill2(Player player);

    public abstract String getSkill2Name();

    public abstract List<String> getSkill2Description();

    public abstract double getSkill2ManaCost();

    public abstract double getSkill2Cooldown();

    // Skill 3
    public void castSkill3(Player player) {
        if (checkCooldown(player, 3) && checkMana(player, getSkill3ManaCost())) {
            player.getBukkitPlayer().sendMessage("§a[技能] §7你使用了 §f" + getSkill3Name() + "§7！");
            useSkill3(player);
            setCooldown(player, 3, getSkill3Cooldown());
            deductMana(player, getSkill3ManaCost());
        }
    }

    public abstract void useSkill3(Player player);

    public abstract String getSkill3Name();

    public abstract List<String> getSkill3Description();

    public abstract double getSkill3ManaCost();

    public abstract double getSkill3Cooldown();

    // Ultimate
    public void castUltimate(Player player) {
        if (checkCooldown(player, 4) && checkMana(player, getUltimateManaCost())) {
            player.getBukkitPlayer().sendMessage("§a[技能] §7你使用了 §6" + getUltimateName() + "§7！");
            useUltimate(player);
            setCooldown(player, 4, getUltimateCooldown());
            deductMana(player, getUltimateManaCost());
        }
    }

    public abstract void useUltimate(Player player);

    public abstract String getUltimateName();

    public abstract List<String> getUltimateDescription();

    public abstract double getUltimateManaCost();

    public abstract double getUltimateCooldown();

    // Helper Methods
    private boolean checkCooldown(Player player, int skillIndex) {
        if (!cooldowns.containsKey(player.getUuid()))
            return true;
        Map<Integer, Long> playerCooldowns = cooldowns.get(player.getUuid());
        if (!playerCooldowns.containsKey(skillIndex))
            return true;

        long endTime = playerCooldowns.get(skillIndex);
        if (System.currentTimeMillis() < endTime) {
            double remaining = (endTime - System.currentTimeMillis()) / 1000.0;
            player.getBukkitPlayer().sendActionBar(
                    net.kyori.adventure.text.Component.text("§c技能冷卻中: " + String.format("%.1f", remaining) + "s"));
            return false;
        }
        return true;
    }

    private void setCooldown(Player player, int skillIndex, double seconds) {
        cooldowns.putIfAbsent(player.getUuid(), new HashMap<>());
        cooldowns.get(player.getUuid()).put(skillIndex, System.currentTimeMillis() + (long) (seconds * 1000));
    }

    private boolean checkMana(Player player, double cost) {
        if (player.getCurrentMana() >= cost)
            return true;
        player.getBukkitPlayer().sendActionBar(net.kyori.adventure.text.Component.text("§c魔力不足!"));
        return false;
    }

    private void deductMana(Player player, double cost) {
        player.setCurrentMana(player.getCurrentMana() - cost);
    }

    public abstract ToolType getWeaponType();

    public abstract String getRole();

    public abstract String getFightingStyle();

    public void onAttack(Player player, LivingEntity target, double damage, boolean isCrit) {
    }

    public void onDefend(Player player, Entity attacker, double damage) {
    }

    public List<String> getPassives() {
        return new ArrayList<>();
    }

}
