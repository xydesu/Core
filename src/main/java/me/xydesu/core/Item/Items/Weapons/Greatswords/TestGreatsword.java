package me.xydesu.core.Item.Items.Weapons.Greatswords;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TestGreatsword extends Item {

    @Override
    public String getID() {
        return "TEST_GREATSWORD";
    }

    @Override
    public String getName() {
        return "測試巨劍";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_SWORD;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.GREATSWORD;
    }

    @Override
    public double getDamage() {
        return 8;
    }

    @Override
    public double getAttackSpeed() {
        return -0.5; // Slower
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }
}
