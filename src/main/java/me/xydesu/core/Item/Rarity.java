package me.xydesu.core.Item;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public enum Rarity {
    
    // 格式：(純淨名稱, MiniMessage Lore標籤, 主導顏色)
    COMMON("普通", "<gray>普通</gray>", NamedTextColor.GRAY),
    RARE("稀有", "<green>稀有</green>", NamedTextColor.GREEN),
    EPIC("史詩", "<light_purple><b>史詩</light_purple>", NamedTextColor.LIGHT_PURPLE),
    LEGENDARY("傳說", "<gradient:yellow:gold:red><b>★ 傳說遺物 ★</gradient>", NamedTextColor.GOLD),
    // 注意：RAINBOW 並非單一顏色，但在此作為系統級別的標記是可行的。
    MYTHIC("神話", "<rainbow><b>✨ 神話太古 ✨</rainbow>", NamedTextColor.WHITE), 
    UNIQUE("專屬", "<dark_aqua><b>♦ 艾澤拉斯之裔 ♦</dark_aqua>", NamedTextColor.DARK_AQUA);

    private final String displayName; // 新增：純淨名稱，用於系統判斷或日誌
    private final String loreTag;     // 原來的 name 欄位，現更名為 loreTag，用於 Lore 顯示
    private final TextColor color;

    Rarity(String displayName, String loreTag, TextColor color) {
        this.displayName = displayName;
        this.loreTag = loreTag;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    // 新增：獲取帶 MiniMessage 格式的標籤
    public String getLoreTag() {
        return loreTag;
    }

    public TextColor getColor() {
        return color;
    }
}