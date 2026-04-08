package me.xydesu.core.Item.Items.Armor;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TestHelmet extends Item {

    @Override
    public String getID() {
        return "TEST_HELMET";
    }

    @Override
    public String getName() {
        return "測試頭盔";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_HELMET;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.HELMET;
    }

    @Override
    public double getDefense() {
        return 10;
    }

    @Override
    public double getMaxHealth() {
        return 20;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public String getSetID() {
        return "TEST_SET";
    }
}
