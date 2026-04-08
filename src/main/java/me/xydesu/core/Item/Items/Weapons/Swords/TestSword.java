package me.xydesu.core.Item.Items.Weapons.Swords;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TestSword extends Item {


    @Override
    public String getID() {
        return "TEST_SWORD";
    }

    @Override
    public String getName() {
        return "測試之劍";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_SWORD;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.SWORD;
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
        return Rarity.LEGENDARY;
    }


}
