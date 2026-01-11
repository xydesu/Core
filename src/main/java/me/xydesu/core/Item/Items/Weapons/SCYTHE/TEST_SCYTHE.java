package me.xydesu.core.Item.Items.Weapons.SCYTHE;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TEST_SCYTHE extends Item {

    @Override
    public String getID() {
        return "TEST_SCYTHE";
    }

    @Override
    public String getName() {
        return "測試鐮刀";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_HOE;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.SCYTHE;
    }

    @Override
    public double getDamage() {
        return 7;
    }

    @Override
    public boolean hasAOE() {
        return true;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }
}
