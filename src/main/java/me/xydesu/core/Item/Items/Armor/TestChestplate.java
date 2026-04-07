package me.xydesu.core.Item.Items.Armor;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TestChestplate extends Item {

    @Override
    public String getID() {
        return "TEST_CHESTPLATE";
    }

    @Override
    public String getName() {
        return "測試胸甲";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_CHESTPLATE;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.CHESTPLATE;
    }

    @Override
    public double getDefense() {
        return 15;
    }

    @Override
    public double getMaxHealth() {
        return 30;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }
}
