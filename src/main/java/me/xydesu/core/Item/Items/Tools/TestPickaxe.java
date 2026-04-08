package me.xydesu.core.Item.Items.Tools;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TestPickaxe extends Item {

    @Override
    public String getID() {
        return "TEST_PICKAXE";
    }

    @Override
    public String getName() {
        return "測試鎬";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_PICKAXE;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.PICKAXE;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }
}
