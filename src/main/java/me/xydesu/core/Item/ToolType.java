package me.xydesu.core.Item;

public enum ToolType {
    
    // --- I. 武器 (Weapons) ---
    // 武器使用紅色 (<red>) 或紫色 (<light_purple>)
    SWORD("單手劍", "<red>[武器] - 單手劍</red>"),
    GREATSWORD("雙手巨劍", "<red>[武器] - 雙手巨劍</red>"),
    DAGGER("匕首", "<red>[武器] - 匕首</red>"),
    BOW("弓", "<red>[武器] - 弓</red>"),
    CROSSBOW("弩", "<red>[武器] - 弩</red>"),
    STAFF("法杖", "<light_purple>[武器] - 法杖</light_purple>"), // 法杖使用紫色，區分魔法類
    SCYTHE("鐮刀", "<dark_purple>[武器] - 鐮刀</dark_purple>"),

    // --- II. 工具 (Tools) ---
    // 工具使用黃色 (<yellow>)
    AXE("斧頭", "<yellow>[工具] - 斧頭</yellow>"),
    PICKAXE("鎬", "<yellow>[工具] - 鎬</yellow>"),
    SHOVEL("鏟", "<yellow>[工具] - 鏟</yellow>"),
    HOE("鋤", "<yellow>[工具] - 鋤</yellow>"),
    FISHING_ROD("釣魚竿", "<yellow>[工具] - 釣魚竿</yellow>"),

    // --- III. 護甲 (Armor) ---
    // 護甲使用藍色 (<blue>)
    HELMET("頭盔", "<blue>[護甲] - 頭盔</blue>"),
    CHESTPLATE("胸甲", "<blue>[護甲] - 胸甲</blue>"),
    LEGGINGS("護腿", "<blue>[護甲] - 護腿</blue>"),
    BOOTS("靴子", "<blue>[護甲] - 靴子</blue>"),
    CLOAK("披風", "<blue>[護甲] - 披風</blue>"),

    // --- IV. 飾品 (Accessories) ---
    // 飾品使用淺紫色 (<light_purple>)
    RING("戒指", "<light_purple>[飾品] - 戒指</light_purple>"),
    NECKLACE("項鍊", "<light_purple>[飾品] - 項鍊</light_purple>"),
    AMULET("護符", "<light_purple>[飾品] - 護符</light_purple>"),
    
    // --- V. 其他系統類 (System Types) ---
    // 根據功能使用不同顏色 (消耗品用綠色, 任務用青色, 材料用灰色)
    CONSUMABLE("消耗品", "<green>[消耗品]</green>"),
    QUEST_ITEM("任務道具", "<aqua>[任務道具]</aqua>"),
    MATERIAL("材料", "<gray>[材料]</gray>"),
    MISC("雜項", "<dark_gray>[雜項]</dark_gray>");

    private final String name;
    private final String loreTag; // 新增的 Lore 顯示標籤欄位

    ToolType(String name, String loreTag) {
        this.name = name;
        this.loreTag = loreTag;
    }

    public String getName() {
        return name;
    }

    // 新增的 getter 方法，用於在 Lore 系統中獲取格式化標籤
    public String getLoreTag() {
        return loreTag;
    }
}