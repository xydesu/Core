package me.xydesu.core.Item.Items.Armor;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TEST_CLOAK extends Item {

    @Override
    public String getID() {
        return "TEST_CLOAK";
    }

    @Override
    public String getName() {
        return "測試披風";
    }

    @Override
    public Material getMaterial() {
        return Material.LEATHER; // No cloak item in MC, usually Elytra or custom model
    }

    @Override
    public ToolType getToolType() {
        return ToolType.CLOAK;
    }

    @Override
    public double getMaxHealth() {
        return 50;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }
}
