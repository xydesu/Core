package me.xydesu.core.Item.Items.Tools;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TestHoe extends Item {

    @Override
    public String getID() {
        return "TEST_HOE";
    }

    @Override
    public String getName() {
        return "測試鋤";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_HOE;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.HOE;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }
}
