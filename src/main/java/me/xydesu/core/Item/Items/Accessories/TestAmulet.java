package me.xydesu.core.Item.Items.Accessories;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TestAmulet extends Item {

    @Override
    public String getID() {
        return "TEST_AMULET";
    }

    @Override
    public String getName() {
        return "測試護符";
    }

    @Override
    public Material getMaterial() {
        return Material.EMERALD;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.AMULET;
    }

    @Override
    public double getMaxMana() {
        return 50;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }
}
