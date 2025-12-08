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

public class PlayerProfileGUI extends GUI {

    public PlayerProfileGUI(Player player) {
        super(54, Component.text("個人資料"));

        // Background
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.displayName(Component.empty());
        filler.setItemMeta(fillerMeta);
        
        for (int i = 0; i < getInventory().getSize(); i++) {
            getInventory().setItem(i, filler);
        }

        me.xydesu.core.Player.Player customPlayer = me.xydesu.core.Player.Player.get(player);

        // 13. Player Head (Center Top)
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(player);
        meta.displayName(Component.text(player.getName(), NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        List<Component> headLore = new ArrayList<>();
        headLore.add(Component.text("等級: ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(customPlayer.getLevel(), NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true)));
        
        double exp = customPlayer.getExp();
        double reqExp = customPlayer.getRequiredExp();
        double percentage = reqExp > 0 ? Math.min(1.0, Math.max(0.0, exp / reqExp)) : 0;
        int totalBars = 20;
        int filledBars = (int) (percentage * totalBars);
        
        Component progressBar = Component.text("[", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false);
        for (int i = 0; i < totalBars; i++) {
            if (i < filledBars) {
                progressBar = progressBar.append(Component.text("|", NamedTextColor.GREEN));
            } else {
                progressBar = progressBar.append(Component.text("|", NamedTextColor.GRAY));
            }
        }
        progressBar = progressBar.append(Component.text("]", NamedTextColor.DARK_GRAY));
        
        headLore.add(progressBar);
        headLore.add(Component.text("經驗值: ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(String.format("%.0f", exp) + " / " + String.format("%.0f", reqExp), NamedTextColor.YELLOW)));
        headLore.add(Component.text(" (" + String.format("%.1f", percentage * 100) + "%)", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        headLore.add(Component.empty());
        headLore.add(Component.text("屬性點數: ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(customPlayer.getAttributePoints(), NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true)));
        
        meta.lore(headLore);
        head.setItemMeta(meta);
        getInventory().setItem(13, head);

        // 20. Health (生命值)
        List<Component> healthLore = new ArrayList<>();
        healthLore.add(Component.text("當前/最大: ", NamedTextColor.GRAY)
                .append(Component.text((int)customPlayer.getCurrentHealth() + "/" + (int)customPlayer.getMaxHealth(), NamedTextColor.RED).decoration(TextDecoration.BOLD, true)));
        healthLore.add(Component.text("----------------", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        healthLore.add(Component.text("基礎生存能力。", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, true));
        getInventory().setItem(20, createItem(Material.REDSTONE, "生命值", healthLore, NamedTextColor.RED));


// 21. Defense (防禦力)
        List<Component> defenseLore = new ArrayList<>();
        defenseLore.add(Component.text("數值: ", NamedTextColor.GRAY)
                .append(Component.text((int)customPlayer.getDefense(), NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true)));
        defenseLore.add(Component.text("--- 物理減傷 ---", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        defenseLore.add(Component.text("減少受到的物理傷害。", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, true));
        getInventory().setItem(21, createItem(Material.IRON_CHESTPLATE, "防禦力", defenseLore, NamedTextColor.GREEN));


// 22. Strength (力量)
        List<Component> strengthLore = new ArrayList<>();
        strengthLore.add(Component.text("數值: ", NamedTextColor.GRAY)
                .append(Component.text((int)customPlayer.getStrength(), NamedTextColor.RED).decoration(TextDecoration.BOLD, true)));
        strengthLore.add(Component.text("--- 傷害提升 ---", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        strengthLore.add(Component.text("增加造成的物理傷害倍率。", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, true));
        getInventory().setItem(22, createItem(Material.BLAZE_POWDER, "力量", strengthLore, NamedTextColor.RED));


// 23. Mana (魔力)
        List<Component> manaLore = new ArrayList<>();
        manaLore.add(Component.text("當前/最大: ", NamedTextColor.GRAY)
                .append(Component.text((int)customPlayer.getCurrentMana() + "/" + (int)customPlayer.getMaxMana(), NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true)));
        manaLore.add(Component.text("----------------", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        manaLore.add(Component.text("用於施放技能或魔法。", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, true));
        getInventory().setItem(23, createItem(Material.LAPIS_LAZULI, "魔力", manaLore, NamedTextColor.AQUA));


// 24. Health Regen (生命回復)
        List<Component> healthRegenLore = new ArrayList<>();
        healthRegenLore.add(Component.text("數值: ", NamedTextColor.GRAY)
                .append(Component.text(String.format("%.1f%%", customPlayer.getHealthRegen()), NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true)));
        healthRegenLore.add(Component.text("公式: [最大生命值] x [數值]% / 秒", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        healthRegenLore.add(Component.text("每秒回復最大生命值的百分比。", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, true));
        getInventory().setItem(24, createItem(Material.APPLE, "生命回復", healthRegenLore, NamedTextColor.LIGHT_PURPLE));


// 29. Crit Chance (暴擊率)
        List<Component> critChanceLore = new ArrayList<>();
        critChanceLore.add(Component.text("機率: ", NamedTextColor.GRAY)
                .append(Component.text(String.format("%.1f%%", customPlayer.getCritChance() * 100), NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true)));
        critChanceLore.add(Component.text("公式: [最終暴擊率] = [基礎值] + [裝備加成]", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        critChanceLore.add(Component.text("攻擊造成致命一擊的機率。", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, true));
        getInventory().setItem(29, createItem(Material.SKELETON_SKULL, "暴擊率", critChanceLore, NamedTextColor.BLUE));


// 30. Crit Damage (暴擊傷害)
        List<Component> critDamageLore = new ArrayList<>();
        critDamageLore.add(Component.text("乘數: ", NamedTextColor.GRAY)
                .append(Component.text(String.format("%.1f%%", customPlayer.getCritDamage() * 100), NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true)));
        critDamageLore.add(Component.text("公式: [暴擊傷害] = [基礎傷害] x [乘數]%", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        critDamageLore.add(Component.text("暴擊時造成的額外傷害倍率。", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, true));
        getInventory().setItem(30, createItem(Material.IRON_AXE, "暴擊傷害", critDamageLore, NamedTextColor.DARK_AQUA));


// 31. Mana Regen (魔力回復)
        List<Component> manaRegenLore = new ArrayList<>();
        manaRegenLore.add(Component.text("數值: ", NamedTextColor.GRAY)
                .append(Component.text(String.format("%.1f%%", customPlayer.getManaRegen()), NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true)));
        manaRegenLore.add(Component.text("公式: [最大魔力值] x [數值]% / 秒", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        manaRegenLore.add(Component.text("每秒回復最大魔力值的百分比。", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, true));
        getInventory().setItem(31, createItem(Material.GHAST_TEAR, "魔力回復", manaRegenLore, NamedTextColor.AQUA));


// 32. Movement Speed (移動速度)
        List<Component> speedLore = new ArrayList<>();
        double walkSpeed = player.getWalkSpeed() * 500; // 0.2 -> 100%
        speedLore.add(Component.text("數值: ", NamedTextColor.GRAY)
                .append(Component.text(String.format("%.0f%%", walkSpeed), NamedTextColor.WHITE).decoration(TextDecoration.BOLD, true)));
        speedLore.add(Component.text("----------------", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        speedLore.add(Component.text("影響角色在世界中移動的效率。", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, true));
        getInventory().setItem(32, createItem(Material.FEATHER, "移動速度", speedLore, NamedTextColor.WHITE));


// 33. Attack Speed (攻擊速度)
        List<Component> attackSpeedLore = new ArrayList<>();
        double attackSpeed = 4.0;
// Try to get attack speed, fallback to default if attribute not found (older versions)
        if (player.getAttribute(org.bukkit.attribute.Attribute.ATTACK_SPEED) != null) {
            attackSpeed = player.getAttribute(org.bukkit.attribute.Attribute.ATTACK_SPEED).getValue();
        }
        attackSpeedLore.add(Component.text("數值: ", NamedTextColor.GRAY)
                .append(Component.text(String.format("%.1f", attackSpeed), NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true)));
        attackSpeedLore.add(Component.text("公式: 4.0 - [武器延遲] + [加成]", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        attackSpeedLore.add(Component.text("每秒攻擊次數，影響輸出頻率。", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, true));
        getInventory().setItem(33, createItem(Material.IRON_SWORD, "攻擊速度", attackSpeedLore, NamedTextColor.YELLOW));

        // 49. Back Button
        List<Component> backLore = new ArrayList<>();
        backLore.add(Component.text("點擊返回主選單", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
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
        }
    }
}
