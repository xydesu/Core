package me.xydesu.core.Item.Items.Armor;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TestLeggings extends Item {

    @Override
    public String getID() {
        return "TEST_LEGGINGS";
    }

    @Override
    public String getName() {
        return "測試護腿";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_LEGGINGS;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.LEGGINGS;
    }

    @Override
    public double getDefense() {
        return 12;
    }

    @Override
    public double getMaxHealth() {
        return 25;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }
}
