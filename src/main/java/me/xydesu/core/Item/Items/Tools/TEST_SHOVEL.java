package me.xydesu.core.Item.Items.Tools;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TEST_SHOVEL extends Item {

    @Override
    public String getID() {
        return "TEST_SHOVEL";
    }

    @Override
    public String getName() {
        return "測試鏟";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_SHOVEL;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.SHOVEL;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }
}
