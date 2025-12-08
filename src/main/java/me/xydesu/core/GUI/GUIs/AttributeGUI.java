package me.xydesu.core.GUI.GUIs;

import me.xydesu.core.GUI.GUI;
import me.xydesu.core.Utils.Keys;
import me.xydesu.core.Utils.PDC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class AttributeGUI extends GUI {

    public AttributeGUI(Player player) {
        super(54, Component.text("屬性配點"));

        // Background
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.displayName(Component.empty());
        filler.setItemMeta(fillerMeta);

        for (int i = 0; i < getInventory().getSize(); i++) {
            getInventory().setItem(i, filler);
        }

        me.xydesu.core.Player.Player customPlayer = me.xydesu.core.Player.Player.get(player);
        int points = customPlayer.getAttributePoints();

        // 13. Attribute Points Indicator
        List<Component> pointsLore = new ArrayList<>();
        pointsLore.add(Component.text("✦ 可用點數: " + points, NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        getInventory().setItem(13, createItem(Material.NETHER_STAR, "屬性點數", pointsLore, NamedTextColor.GOLD));

        // 20. Strength (力量) - RED
        List<Component> strengthLore = new ArrayList<>();
        strengthLore.add(Component.text("已分配: ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text((int)customPlayer.getAllocatedStrength(), NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true)));
        strengthLore.add(Component.text("----------------", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        strengthLore.add(Component.text("每點增益:", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, true));
        strengthLore.add(Component.text(" +1% 物理傷害", NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false)); // 使用 AQUA 凸顯增益數值
        strengthLore.add(Component.text("----------------", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        strengthLore.add(Component.text("當前加成:", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        strengthLore.add(Component.text(" +" + (int)customPlayer.getAllocatedStrength() + "% 物理傷害", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        strengthLore.add(Component.empty());
        strengthLore.add(Component.text("左鍵增加 / 右鍵減少", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
        strengthLore.add(Component.text("Shift + 點擊 +/- 10", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        strengthLore.add(Component.text("花費: 1 點", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        ItemStack strengthItem = createItem(Material.BLAZE_POWDER, "力量", strengthLore, NamedTextColor.RED); // 名稱顏色保持 RED
        PDC.set(strengthItem, Keys.ID, PersistentDataType.STRING, "ATTR_STRENGTH");
        getInventory().setItem(20, strengthItem);

        // 21. Vitality (體力) - GREEN
        List<Component> vitLore = new ArrayList<>();
        vitLore.add(Component.text("已分配: ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text((int)customPlayer.getAllocatedVitality(), NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true)));
        vitLore.add(Component.text("----------------", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        vitLore.add(Component.text("每點增益:", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, true));
        vitLore.add(Component.text(" +5 最大生命", NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
        vitLore.add(Component.text(" +0.1 生命回復", NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
        vitLore.add(Component.text("----------------", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        vitLore.add(Component.text("當前加成:", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        vitLore.add(Component.text(" +" + (int)(customPlayer.getAllocatedVitality() * 5) + " 最大生命", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        vitLore.add(Component.text(" +" + String.format("%.1f", customPlayer.getAllocatedVitality() * 0.1) + " 生命回復", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        vitLore.add(Component.empty());
        vitLore.add(Component.text("左鍵增加 / 右鍵減少", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
        vitLore.add(Component.text("Shift + 點擊 +/- 10", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        vitLore.add(Component.text("花費: 1 點", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        ItemStack vitItem = createItem(Material.APPLE, "體力", vitLore, NamedTextColor.GREEN); // 名稱顏色保持 GREEN
        PDC.set(vitItem, Keys.ID, PersistentDataType.STRING, "ATTR_VITALITY");
        getInventory().setItem(21, vitItem);

        // 22. Dexterity (靈巧) - GOLD
        List<Component> dexLore = new ArrayList<>();
        dexLore.add(Component.text("已分配: ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text((int)customPlayer.getAllocatedDexterity(), NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true)));
        dexLore.add(Component.text("----------------", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        dexLore.add(Component.text("每點增益:", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, true));
        dexLore.add(Component.text(" +0.1% 暴擊率", NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
        dexLore.add(Component.text(" +0.5% 暴擊傷害", NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
        dexLore.add(Component.text("----------------", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        dexLore.add(Component.text("當前加成:", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        dexLore.add(Component.text(" +" + String.format("%.1f", customPlayer.getAllocatedDexterity() * 0.1) + "% 暴擊率", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        dexLore.add(Component.text(" +" + String.format("%.1f", customPlayer.getAllocatedDexterity() * 0.5) + "% 暴擊傷害", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        dexLore.add(Component.empty());
        dexLore.add(Component.text("左鍵增加 / 右鍵減少", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
        dexLore.add(Component.text("Shift + 點擊 +/- 10", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        dexLore.add(Component.text("花費: 1 點", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        ItemStack dexItem = createItem(Material.FLINT, "靈巧", dexLore, NamedTextColor.GOLD); // 名稱顏色保持 GOLD
        PDC.set(dexItem, Keys.ID, PersistentDataType.STRING, "ATTR_DEXTERITY");
        getInventory().setItem(22, dexItem);

        // 23. Intelligence (智力) - AQUA
        List<Component> intelLore = new ArrayList<>();
        intelLore.add(Component.text("已分配: ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text((int)customPlayer.getAllocatedIntelligence(), NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true)));
        intelLore.add(Component.text("----------------", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        intelLore.add(Component.text("每點增益:", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, true));
        intelLore.add(Component.text(" +5 最大魔力", NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
        intelLore.add(Component.text(" +0.1 魔力回復", NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
        intelLore.add(Component.text("----------------", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        intelLore.add(Component.text("當前加成:", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        intelLore.add(Component.text(" +" + (int)(customPlayer.getAllocatedIntelligence() * 5) + " 最大魔力", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        intelLore.add(Component.text(" +" + String.format("%.1f", customPlayer.getAllocatedIntelligence() * 0.1) + " 魔力回復", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        intelLore.add(Component.empty());
        intelLore.add(Component.text("左鍵增加 / 右鍵減少", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
        intelLore.add(Component.text("Shift + 點擊 +/- 10", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        intelLore.add(Component.text("花費: 1 點", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        ItemStack intelItem = createItem(Material.ENCHANTED_BOOK, "智力", intelLore, NamedTextColor.AQUA); // 名稱顏色保持 AQUA
        PDC.set(intelItem, Keys.ID, PersistentDataType.STRING, "ATTR_INTELLIGENCE");
        getInventory().setItem(23, intelItem);

        // 24. Agility (敏捷) - WHITE
        List<Component> agilityLore = new ArrayList<>();
        agilityLore.add(Component.text("已分配: ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text((int)customPlayer.getAllocatedAgility(), NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true)));
        agilityLore.add(Component.text("----------------", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        agilityLore.add(Component.text("每點增益:", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, true));
        agilityLore.add(Component.text(" +0.05% 移動速度", NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
        agilityLore.add(Component.text("----------------", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        agilityLore.add(Component.text("當前加成:", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        agilityLore.add(Component.text(" +" + String.format("%.2f", customPlayer.getAllocatedAgility() * 0.05) + "% 移動速度", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        agilityLore.add(Component.empty());
        agilityLore.add(Component.text("左鍵增加 / 右鍵減少", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
        agilityLore.add(Component.text("Shift + 點擊 +/- 10", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        agilityLore.add(Component.text("花費: 1 點", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        ItemStack agilityItem = createItem(Material.FEATHER, "敏捷", agilityLore, NamedTextColor.WHITE); // 名稱顏色保持 WHITE
        PDC.set(agilityItem, Keys.ID, PersistentDataType.STRING, "ATTR_AGILITY");
        getInventory().setItem(24, agilityItem);

        // Reset Button (50)
        List<Component> resetLore = new ArrayList<>();
        resetLore.add(Component.text("點擊重置所有屬性點", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        resetLore.add(Component.empty());
        resetLore.add(Component.text("警告: 此操作無法撤銷!", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        ItemStack resetItem = createItem(Material.TNT, "重置屬性", resetLore, NamedTextColor.RED);
        PDC.set(resetItem, Keys.ID, PersistentDataType.STRING, "RESET_ATTRIBUTES");
        getInventory().setItem(50, resetItem);

        // Back Button (49)
        List<Component> backLore = new ArrayList<>();
        backLore.add(Component.text("點擊返回上一頁", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        ItemStack backItem = createItem(Material.ARROW, "返回", backLore, NamedTextColor.RED);
        PDC.set(backItem, Keys.ID, PersistentDataType.STRING, "BACK");
        getInventory().setItem(49, backItem);
    }

    private ItemStack createItem(Material material, String name, List<Component> lore, NamedTextColor color) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name, color).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;

        String id = PDC.get(item, Keys.ID, PersistentDataType.STRING, null);
        if (id == null) return;

        if (id.equals("BACK")) {
            new MainMenu(player).open(player);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            return;
        }

        if (id.equals("RESET_ATTRIBUTES")) {
            me.xydesu.core.Player.Player customPlayer = me.xydesu.core.Player.Player.get(player);
            customPlayer.setAttributePoints(customPlayer.getAttributePoints() 
                + (int)customPlayer.getAllocatedStrength()
                + (int)customPlayer.getAllocatedVitality()
                + (int)customPlayer.getAllocatedDexterity()
                + (int)customPlayer.getAllocatedIntelligence()
                + (int)customPlayer.getAllocatedAgility()
            );
            customPlayer.setAllocatedStrength(0);
            customPlayer.setAllocatedVitality(0);
            customPlayer.setAllocatedDexterity(0);
            customPlayer.setAllocatedIntelligence(0);
            customPlayer.setAllocatedAgility(0);
            
            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
            new AttributeGUI(player).open(player);
            return;
        }

        if (!id.startsWith("ATTR_")) return;

        me.xydesu.core.Player.Player customPlayer = me.xydesu.core.Player.Player.get(player);
        
        int amount = 0;
        if (event.isLeftClick()) {
            amount = event.isShiftClick() ? 10 : 1;
        } else if (event.isRightClick()) {
            amount = event.isShiftClick() ? -10 : -1;
        }

        if (amount == 0) return;

        // Check if adding points
        if (amount > 0) {
            if (customPlayer.getAttributePoints() < amount) {
                // Try to add as many as possible
                amount = customPlayer.getAttributePoints();
                if (amount == 0) {
                    player.sendMessage(Component.text("你的屬性點數不足！", NamedTextColor.RED));
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return;
                }
            }
        }

        boolean success = false;
        double currentAllocated = 0;

        switch (id) {
            case "ATTR_STRENGTH":
                currentAllocated = customPlayer.getAllocatedStrength();
                if (amount < 0 && currentAllocated + amount < 0) amount = -(int)currentAllocated;
                if (amount != 0) {
                    customPlayer.setAllocatedStrength(currentAllocated + amount);
                    success = true;
                }
                break;
            case "ATTR_AGILITY":
                currentAllocated = customPlayer.getAllocatedAgility();
                if (amount < 0 && currentAllocated + amount < 0) amount = -(int)currentAllocated;
                if (amount != 0) {
                    customPlayer.setAllocatedAgility(currentAllocated + amount);
                    success = true;
                }
                break;
            case "ATTR_INTELLIGENCE":
                currentAllocated = customPlayer.getAllocatedIntelligence();
                if (amount < 0 && currentAllocated + amount < 0) amount = -(int)currentAllocated;
                if (amount != 0) {
                    customPlayer.setAllocatedIntelligence(currentAllocated + amount);
                    success = true;
                }
                break;
            case "ATTR_VITALITY":
                currentAllocated = customPlayer.getAllocatedVitality();
                if (amount < 0 && currentAllocated + amount < 0) amount = -(int)currentAllocated;
                if (amount != 0) {
                    customPlayer.setAllocatedVitality(currentAllocated + amount);
                    success = true;
                }
                break;
            case "ATTR_DEXTERITY":
                currentAllocated = customPlayer.getAllocatedDexterity();
                if (amount < 0 && currentAllocated + amount < 0) amount = -(int)currentAllocated;
                if (amount != 0) {
                    customPlayer.setAllocatedDexterity(currentAllocated + amount);
                    success = true;
                }
                break;
        }

        if (success) {
            customPlayer.setAttributePoints(customPlayer.getAttributePoints() - amount);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            // Re-open to update values
            new AttributeGUI(player).open(player);
        }
    }
}
