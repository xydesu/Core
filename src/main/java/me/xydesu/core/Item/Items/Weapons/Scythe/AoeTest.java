package me.xydesu.core.Item.Items.Weapons.Scythe;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class AoeTest extends Item {


    @Override
    public String getID() {
        return "AOE_TEST";
    }

    @Override
    public String getName() {
        return "範圍攻擊測試";
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_HOE;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.SCYTHE;
    }

    @Override
    public boolean hasAOE() {
        return true;
    }

    @Override
    public double getDamage() {
        return 5;
    }

    @Override
    public double getRange() {
        return 4;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.UNIQUE;
    }

}
