package me.xydesu.core.Item.Items.Weapons.Daggers;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TestDagger extends Item {

    @Override
    public String getID() {
        return "TEST_DAGGER";
    }

    @Override
    public String getName() {
        return "測試匕首";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_SWORD;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.DAGGER;
    }

    @Override
    public double getDamage() {
        return 3;
    }

    @Override
    public double getAttackSpeed() {
        return 0.5; // Faster
    }

    @Override
    public double getCritChance() {
        return 0.2;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }
}
