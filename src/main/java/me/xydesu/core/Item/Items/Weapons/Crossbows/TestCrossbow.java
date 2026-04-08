package me.xydesu.core.Item.Items.Weapons.Crossbows;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TestCrossbow extends Item {

    @Override
    public String getID() {
        return "TEST_CROSSBOW";
    }

    @Override
    public String getName() {
        return "測試弩";
    }

    @Override
    public Material getMaterial() {
        return Material.CROSSBOW;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.CROSSBOW;
    }

    @Override
    public double getDamage() {
        return 6;
    }

    @Override
    public double getRange() {
        return 30;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }
}
