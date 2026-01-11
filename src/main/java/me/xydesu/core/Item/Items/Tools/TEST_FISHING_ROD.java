package me.xydesu.core.Item.Items.Tools;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TEST_FISHING_ROD extends Item {

    @Override
    public String getID() {
        return "TEST_FISHING_ROD";
    }

    @Override
    public String getName() {
        return "測試釣魚竿";
    }

    @Override
    public Material getMaterial() {
        return Material.FISHING_ROD;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.FISHING_ROD;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }
}
