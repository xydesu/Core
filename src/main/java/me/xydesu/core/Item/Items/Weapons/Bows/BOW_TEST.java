package me.xydesu.core.Item.Items.Weapons.Bows;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class BOW_TEST extends Item {
    @Override
    public String getID() {
        return "BOW_TEST";
    }

    @Override
    public String getName() {
        return "測試之弓";
    }

    @Override
    public Material getMaterial() {
        return Material.BOW;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.BOW;
    }

    @Override
    public double getDamage() {
        return 5;
    }

}
