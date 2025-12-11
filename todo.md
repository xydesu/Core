# Project Core - 開發待辦事項 (Development Roadmap)

## 🛠️ 核心機制 (Core Mechanics)
- [ ] **職業與技能系統 (Class & Skill System)**
    - [ ] 建立 `Skill` 抽象類別 (定義 ID, Name, ManaCost, Cooldown)。
    - [ ] 在 `Player` 類別中新增 `activeSkills` 列表。
    - [ ] 實作施法機制 (監聽 `PlayerInteractEvent` 或 `SwapHandEvent`)。
    - [ ] 製作技能冷卻 (Cooldown) 管理器。

- [ ] **屬性系統擴充 (Stats Expansion)**
    - [ ] **套裝效果 (Set Bonuses)**: 偵測玩家裝備，若穿著整套裝備則給予額外屬性。
    - [ ] **屬性點重置**: 製作道具或指令讓玩家重置 `str`, `int`, `def` 等配點。

## ⚔️ 物品與裝備 (Items & Gear)
- [ ] **重鑄/鑑定系統 (Reforging)**
    - [ ] 製作 `ReforgeMenu` GUI。
    - [ ] 實作邏輯：消耗特定材料，重新隨機物品的 `quality` (品質) 與數值。
- [ ] **更多自訂物品**
    - [ ] 新增飾品 (Accessories) 欄位或邏輯。
    - [ ] 實作消耗品 (藥水、食物) 給予暫時性 Buff。

## 🌍 世界與生物 (World & Mobs)
- [ ] **區域生成系統 (Region Spawning)**
    - [ ] 建立 `RegionManager` 定義怪物生成區。
    - [ ] 製作 `MobSpawnTask` 自動在區域內生成 `CustomMob`。
- [ ] **任務與 NPC (Quests & NPCs)**
    - [ ] 製作 NPC 對話 GUI (利用現有的 Menu 系統)。
    - [ ] 建立簡單的任務目標 (例如：殺死 10 隻 SuperZombie)。

## 🎨 視覺與介面 (Visuals & UI)
- [x] **傷害顯示 (Damage Indicators)**
    - [x] 攻擊時生成短暫的 `TextDisplay` 顯示傷害數值。
    - [x] 加入暴擊 (Crit) 的特殊顏色或動畫效果。
- [ ] **隊伍系統 (Party System)**
    - [ ] 建立隊伍管理器 (邀請、踢出、解散)。
    - [ ] 修改 `Attack` 事件防止隊友傷害。
    - [ ] 在 HUD 顯示隊友血量。

## ⚙️ 技術債與優化 (Technical & Optimization)
- [ ] **資料儲存優化**
    - [ ] 確保玩家資料儲存 (SaveData) 是非同步 (Async) 執行的，避免卡頓主執行緒。