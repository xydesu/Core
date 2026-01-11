package me.xydesu.core.Item.Items.Misc;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TEST_CONSUMABLE extends Item {

    @Override
    public String getID() {
        return "TEST_CONSUMABLE";
    }

    @Override
    public String getName() {
        return "測試消耗品";
    }

    @Override
    public Material getMaterial() {
        return Material.APPLE;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.CONSUMABLE;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }
}
