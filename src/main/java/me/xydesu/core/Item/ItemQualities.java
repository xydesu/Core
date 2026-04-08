package me.xydesu.core.Item;

import me.xydesu.core.Utils.Keys;
import me.xydesu.core.Utils.PDC;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * Holds the per-stat quality rolls for a single item instance and provides
 * helper methods for generating new rolls and persisting them to PDC.
 */
public class ItemQualities {

    private static final double DEV = 0.15;

    public final double overall;
    public final double damage;
    public final double strength;
    public final double defense;
    public final double critChance;
    public final double critDamage;
    public final double maxHealth;
    public final double healthRegen;
    public final double maxMana;
    public final double manaRegen;
    public final double attackSpeed;
    public final double elementalDamage;
    public final double movementSpeed;
    public final double lifeSteal;

    private ItemQualities(double overall, double damage, double strength, double defense,
                          double critChance, double critDamage, double maxHealth, double healthRegen,
                          double maxMana, double manaRegen, double attackSpeed, double elementalDamage,
                          double movementSpeed, double lifeSteal) {
        this.overall = overall;
        this.damage = damage;
        this.strength = strength;
        this.defense = defense;
        this.critChance = critChance;
        this.critDamage = critDamage;
        this.maxHealth = maxHealth;
        this.healthRegen = healthRegen;
        this.maxMana = maxMana;
        this.manaRegen = manaRegen;
        this.attackSpeed = attackSpeed;
        this.elementalDamage = elementalDamage;
        this.movementSpeed = movementSpeed;
        this.lifeSteal = lifeSteal;
    }

    private static double clamp(double v) {
        return Math.max(0, Math.min(1, v));
    }

    private static double deviated(double base) {
        return clamp(base + (Math.random() * 2 - 1) * DEV);
    }

    /** Generates a fresh set of randomised quality rolls. */
    public static ItemQualities generate() {
        double overall = Math.random();
        return new ItemQualities(
                overall,
                deviated(overall),
                deviated(overall),
                deviated(overall),
                deviated(overall),
                deviated(overall),
                deviated(overall),
                deviated(overall),
                deviated(overall),
                deviated(overall),
                deviated(overall),
                deviated(overall),
                deviated(overall),
                deviated(overall)
        );
    }

    /** Writes all quality values into the item's PersistentDataContainer. */
    public void saveToPDC(ItemMeta meta) {
        PDC.set(meta, Keys.QUALITY, PersistentDataType.DOUBLE, overall);
        PDC.set(meta, Keys.QUALITY_DAMAGE, PersistentDataType.DOUBLE, damage);
        PDC.set(meta, Keys.QUALITY_STRENGTH, PersistentDataType.DOUBLE, strength);
        PDC.set(meta, Keys.QUALITY_DEFENSE, PersistentDataType.DOUBLE, defense);
        PDC.set(meta, Keys.QUALITY_CRIT_CHANCE, PersistentDataType.DOUBLE, critChance);
        PDC.set(meta, Keys.QUALITY_CRIT_DAMAGE, PersistentDataType.DOUBLE, critDamage);
        PDC.set(meta, Keys.QUALITY_MAX_HEALTH, PersistentDataType.DOUBLE, maxHealth);
        PDC.set(meta, Keys.QUALITY_HEALTH_REGEN, PersistentDataType.DOUBLE, healthRegen);
        PDC.set(meta, Keys.QUALITY_MAX_MANA, PersistentDataType.DOUBLE, maxMana);
        PDC.set(meta, Keys.QUALITY_MANA_REGEN, PersistentDataType.DOUBLE, manaRegen);
        PDC.set(meta, Keys.QUALITY_ATTACK_SPEED, PersistentDataType.DOUBLE, attackSpeed);
        PDC.set(meta, Keys.QUALITY_ELEMENTAL_DAMAGE, PersistentDataType.DOUBLE, elementalDamage);
        PDC.set(meta, Keys.QUALITY_MOVEMENT_SPEED, PersistentDataType.DOUBLE, movementSpeed);
        PDC.set(meta, Keys.QUALITY_LIFE_STEAL, PersistentDataType.DOUBLE, lifeSteal);
    }

    /**
     * Applies the randomised stat values (based on quality rolls and the item
     * template's base stats) into the item's PersistentDataContainer.
     */
    public void applyStats(ItemMeta meta, Item template) {
        double v = template.getStatVariance();
        PDC.set(meta, Keys.DAMAGE, PersistentDataType.DOUBLE,
                template.getDamage() * (1.0 + (damage - 0.5) * 2 * v));
        PDC.set(meta, Keys.STRENGTH, PersistentDataType.DOUBLE,
                template.getStrength() * (1.0 + (strength - 0.5) * 2 * v));
        PDC.set(meta, Keys.DEFENSE, PersistentDataType.DOUBLE,
                template.getDefense() * (1.0 + (defense - 0.5) * 2 * v));
        PDC.set(meta, Keys.CRIT_CHANCE, PersistentDataType.DOUBLE,
                template.getCritChance() * (1.0 + (critChance - 0.5) * 2 * v));
        PDC.set(meta, Keys.CRIT_DAMAGE, PersistentDataType.DOUBLE,
                template.getCritDamage() * (1.0 + (critDamage - 0.5) * 2 * v));
        PDC.set(meta, Keys.MAX_HEALTH, PersistentDataType.DOUBLE,
                template.getMaxHealth() * (1.0 + (maxHealth - 0.5) * 2 * v));
        PDC.set(meta, Keys.HEALTH_REGEN, PersistentDataType.DOUBLE,
                template.getHealthRegen() * (1.0 + (healthRegen - 0.5) * 2 * v));
        PDC.set(meta, Keys.MAX_MANA, PersistentDataType.DOUBLE,
                template.getMaxMana() * (1.0 + (maxMana - 0.5) * 2 * v));
        PDC.set(meta, Keys.MANA_REGEN, PersistentDataType.DOUBLE,
                template.getManaRegen() * (1.0 + (manaRegen - 0.5) * 2 * v));
        PDC.set(meta, Keys.ATTACK_SPEED, PersistentDataType.DOUBLE,
                template.getAttackSpeed() * (1.0 + (attackSpeed - 0.5) * 2 * v));
        PDC.set(meta, Keys.ELEMENTAL_DAMAGE, PersistentDataType.DOUBLE,
                template.getElementalDamage() * (1.0 + (elementalDamage - 0.5) * 2 * v));
        PDC.set(meta, Keys.MOVEMENT_SPEED, PersistentDataType.DOUBLE,
                template.getMovementSpeed() * (1.0 + (movementSpeed - 0.5) * 2 * v));
        PDC.set(meta, Keys.LIFE_STEAL, PersistentDataType.DOUBLE,
                template.getLifeSteal() * (1.0 + (lifeSteal - 0.5) * 2 * v));
    }
}
