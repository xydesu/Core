package me.xydesu.core.Item.Items.Accessories;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TestNecklace extends Item {

    @Override
    public String getID() {
        return "TEST_NECKLACE";
    }

    @Override
    public String getName() {
        return "測試項鍊";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_NUGGET;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.NECKLACE;
    }

    @Override
    public double getHealthRegen() {
        return 0.1;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }
}
