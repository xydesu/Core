package me.xydesu.core.Item.Items.Armor;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TEST_BOOTS extends Item {

    @Override
    public String getID() {
        return "TEST_BOOTS";
    }

    @Override
    public String getName() {
        return "測試靴子";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_BOOTS;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.BOOTS;
    }

    @Override
    public double getDefense() {
        return 8;
    }

    @Override
    public double getMovementSpeed() {
        return 0.1; // +10% Speed
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }
}
