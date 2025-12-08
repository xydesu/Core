package me.xydesu.core.Item.Items.Weapons.Swords;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class Hyperion extends Item {

    @Override
    public String getID() {
        return "HYPERION";
    }

    @Override
    public String getName() {
        return "海伯利昂";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_SWORD;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.SWORD;
    }

    @Override
    public double getDamage() {
        return 260;
    }

    @Override
    public double getRange() {
        return 6;
    }

    @Override
    public double getStrength() { return 50; }

    @Override
    public double getDefense() { return 50; }

    @Override
    public double getCritChance() { return 50; }

    @Override
    public double getCritDamage() { return 50; }

    @Override
    public double getMaxMana() { return 50; }

    @Override
    public double getManaRegen() { return 50; }

    @Override
    public double getMaxHealth() { return 50; }

    @Override
    public Rarity getRarity() {
        return Rarity.MYTHIC;
    }

    @Override
    public double getStatVariance() {
        return 1; // +/- 10%
    }


}
