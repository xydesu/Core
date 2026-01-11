package me.xydesu.core.Item.Items.Tools;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TEST_AXE extends Item {

    @Override
    public String getID() {
        return "TEST_AXE";
    }

    @Override
    public String getName() {
        return "測試斧頭";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_AXE;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.AXE;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }
}
