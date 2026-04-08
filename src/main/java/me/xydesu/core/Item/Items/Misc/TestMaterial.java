package me.xydesu.core.Item.Items.Misc;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TestMaterial extends Item {

    @Override
    public String getID() {
        return "TEST_MATERIAL";
    }

    @Override
    public String getName() {
        return "測試材料";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_INGOT;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.MATERIAL;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }
}
