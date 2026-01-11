package me.xydesu.core.Item.Items.Misc;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TEST_MISC extends Item {

    @Override
    public String getID() {
        return "TEST_MISC";
    }

    @Override
    public String getName() {
        return "測試雜項";
    }

    @Override
    public Material getMaterial() {
        return Material.STICK;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.MISC;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }
}
