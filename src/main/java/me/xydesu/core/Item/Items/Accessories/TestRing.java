package me.xydesu.core.Item.Items.Accessories;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TestRing extends Item {

    @Override
    public String getID() {
        return "TEST_RING";
    }

    @Override
    public String getName() {
        return "測試戒指";
    }

    @Override
    public Material getMaterial() {
        return Material.GOLD_NUGGET;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.RING;
    }

    @Override
    public double getCritChance() {
        return 0.05;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }
}
