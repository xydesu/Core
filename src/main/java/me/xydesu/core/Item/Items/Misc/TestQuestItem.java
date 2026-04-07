package me.xydesu.core.Item.Items.Misc;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import org.bukkit.Material;

public class TestQuestItem extends Item {

    @Override
    public String getID() {
        return "TEST_QUEST_ITEM";
    }

    @Override
    public String getName() {
        return "測試任務道具";
    }

    @Override
    public Material getMaterial() {
        return Material.PAPER;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.QUEST_ITEM;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }
}
