package me.xydesu.core.Utils;

import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class DamageCalc {

    public static class DamageResult {
        public double damage;
        public boolean isCrit;

        public DamageResult(double damage, boolean isCrit) {
            this.damage = damage;
            this.isCrit = isCrit;
        }
    }

    public static DamageResult getDamage(Player player, ItemStack item, Entity target) {
        return getDamage(player, item, target, false);
    }

    public static DamageResult getDamage(Player player, ItemStack item, Entity target, boolean silent) {
        return getDamage(player, item, target, silent, -1);
    }

    public static DamageResult getDamage(Player player, ItemStack item, Entity target, boolean silent, float attackStrength) {
        me.xydesu.core.Player.Player customPlayer = me.xydesu.core.Player.Player.get(player);
        double totalDamage = 1;

        double damage = PDC.get(item, Keys.DAMAGE, PersistentDataType.DOUBLE, 0.0);
        totalDamage += damage;

        // Strength Calculation (1 Strength = 1% increase)
        double strengthMultiplier = 1 + (customPlayer.getStrength() / 100.0);
        totalDamage *= strengthMultiplier;

        // Critical Hit Calculation
        boolean isCrit = false;
        if (Math.random() < customPlayer.getCritChance()) {
            totalDamage *= customPlayer.getCritDamage();
            isCrit = true;
        }

        if (attackStrength == -1) {
            attackStrength = player.getCooledAttackStrength(0.0F);
        }
        totalDamage*=attackStrength;

        if (!silent) {
            player.sendMessage("==========傷害資訊==========");
            player.sendMessage("基礎傷害: " + damage);
            player.sendMessage("力量加成: " + (int) customPlayer.getStrength() + "%");
            player.sendMessage("暴擊: " + isCrit);
            player.sendMessage("攻擊強度: " + attackStrength);
            player.sendMessage("總傷害: " + totalDamage);
            player.sendMessage("==============================");
        }

        return new DamageResult(totalDamage, isCrit);
    }

    public static DamageResult getRangedDamage(Player player, ItemStack item, AbstractArrow arrow, Entity target) {
        double totalDamage = 0;

        double damage = PDC.get(item, Keys.DAMAGE, PersistentDataType.DOUBLE, 0.0);
        totalDamage += damage;

        boolean isCrit = arrow.isCritical();
        if (isCrit) {
            totalDamage *= 1.5;
        }

        player.sendMessage("==========傷害資訊==========");
        player.sendMessage("基礎傷害: " + damage);
        player.sendMessage("類型: 遠程");
        player.sendMessage("總傷害: " + totalDamage);
        player.sendMessage("==============================");

        return new DamageResult(totalDamage, isCrit);
    }

}
