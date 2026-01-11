package me.xydesu.core.Item.Items.Weapons.Staffs;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TEST_STAFF extends Item {

    @Override
    public String getID() {
        return "TEST_STAFF";
    }

    @Override
    public String getName() {
        return "測試法杖";
    }

    @Override
    public Material getMaterial() {
        return Material.STICK;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.STAFF;
    }

    @Override
    public double getDamage() {
        return 2;
    }

    @Override
    public double getElementalDamage() {
        return 10;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }
}
