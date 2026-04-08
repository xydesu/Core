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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends GUI {

    public MainMenu(Player player) {
        super(54, Component.text("主選單"));

        // Background
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.displayName(Component.empty());
        filler.setItemMeta(fillerMeta);
        
        for (int i = 0; i < getInventory().getSize(); i++) {
            getInventory().setItem(i, filler);
        }

        // Player Head (Profile) - Slot 21
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        
        meta.setOwningPlayer(player);
        meta.displayName(Component.text(player.getName(), NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));

        // Get Custom Player Stats
        me.xydesu.core.Player.Player customPlayer = me.xydesu.core.Player.Player.get(player);

        // 角色資料顯示 (Profile Item - Slot 21)
        List<Component> lore = new ArrayList<>();
// 1. 分隔線 (統一格式)
        lore.add(Component.text("------------------------", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));

// 2. 等級 (Lv.)
        lore.add(Component.text(" 等級 ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text("Lv." + customPlayer.getLevel(), NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true)));

// 3. 經驗條計算 (保持原邏輯，優化顏色)
        double exp = customPlayer.getExp();
        double reqExp = customPlayer.getRequiredExp();
        double percentage = reqExp > 0 ? Math.min(1.0, Math.max(0.0, exp / reqExp)) : 0;
        int totalBars = 20;
        int filledBars = (int) (percentage * totalBars);

        Component progressBar = Component.text("[", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false);
        for (int i = 0; i < totalBars; i++) {
            if (i < filledBars) {
                // 填充部分使用金色
                progressBar = progressBar.append(Component.text("|", NamedTextColor.GOLD));
            } else {
                // 未填充部分使用淺灰色
                progressBar = progressBar.append(Component.text("|", NamedTextColor.GRAY));
            }
        }
        progressBar = progressBar.append(Component.text("]", NamedTextColor.DARK_GRAY));

        lore.add(progressBar);
// 4. 經驗值數值
        lore.add(Component.text(" 經驗值: ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(String.format("%.0f", exp) + " / " + String.format("%.0f", reqExp), NamedTextColor.YELLOW)));
        lore.add(Component.text(" (" + String.format("%.1f", percentage * 100) + "%)", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.empty());

// 5. 核心屬性總覽 (移除 Emoji，將名稱賦予顏色)
        lore.add(Component.text(" ❤ 生命值: ", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false) // 使用顏色來強調屬性
                .append(Component.text((int)customPlayer.getCurrentHealth() + "/" + (int)customPlayer.getMaxHealth(), NamedTextColor.WHITE).decoration(TextDecoration.BOLD, true)));
        lore.add(Component.text(" ✎ 魔力: ", NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false)
                .append(Component.text((int)customPlayer.getCurrentMana() + "/" + (int)customPlayer.getMaxMana(), NamedTextColor.WHITE).decoration(TextDecoration.BOLD, true)));
        lore.add(Component.empty());
        lore.add(Component.text(" ⚔ 力量: ", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)
                .append(Component.text((int)customPlayer.getStrength(), NamedTextColor.WHITE).decoration(TextDecoration.BOLD, true)));
        lore.add(Component.text(" 🛡 防禦: ", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)
                .append(Component.text((int)customPlayer.getDefense(), NamedTextColor.WHITE).decoration(TextDecoration.BOLD, true)));

// 6. 分隔線 (統一格式)
        lore.add(Component.text("------------------------", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.empty());

// 7. 點擊提示
        lore.add(Component.text("點擊查看完整個人資料", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        
        meta.lore(lore);
        PDC.set(meta, Keys.ID, PersistentDataType.STRING, "PROFILE");
        head.setItemMeta(meta);
        getInventory().setItem(21, head);

        // Attribute Allocation Button (屬性配點按鈕 - Slot 23)
        ItemStack attrItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta attrMeta = attrItem.getItemMeta();
        attrMeta.displayName(Component.text("✦ 屬性配點", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true)); // 加入一個非 Emoji 符號強調
        List<Component> attrLore = new ArrayList<>();
        attrLore.add(Component.text("點擊分配屬性點數。", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, true));
        attrLore.add(Component.empty());
// 可用點數使用金色粗體
        attrLore.add(Component.text("可用點數: ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(customPlayer.getAttributePoints(), NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true)));
        attrMeta.lore(attrLore);
        PDC.set(attrMeta, Keys.ID, PersistentDataType.STRING, "ATTRIBUTES");
        attrItem.setItemMeta(attrMeta);
        getInventory().setItem(23, attrItem);

        // Reforge Button (重鑄台 - Slot 25)
        ItemStack reforgeItem = new ItemStack(Material.ANVIL);
        ItemMeta reforgeMeta = reforgeItem.getItemMeta();
        reforgeMeta.displayName(Component.text("⚒ 重鑄台", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        List<Component> reforgeLore = new ArrayList<>();
        reforgeLore.add(Component.text("點擊開啟重鑄介面。", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, true));
        reforgeLore.add(Component.empty());
        reforgeLore.add(Component.text("消耗材料重新隨機物品品質。", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
        reforgeMeta.lore(reforgeLore);
        PDC.set(reforgeMeta, Keys.ID, PersistentDataType.STRING, "REFORGE");
        reforgeItem.setItemMeta(reforgeMeta);
        getInventory().setItem(25, reforgeItem);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;

        String id = PDC.get(item, Keys.ID, PersistentDataType.STRING, null);
        if (id == null) return;

        Player player = (Player) event.getWhoClicked();

        if (id.equals("PROFILE")) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            new PlayerProfileGUI(player).open(player);
        } else if (id.equals("ATTRIBUTES")) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            new AttributeGUI(player).open(player);
        } else if (id.equals("REFORGE")) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            new ReforgeMenu(player).open(player);
        }
    }
}
