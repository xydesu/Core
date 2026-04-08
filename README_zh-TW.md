# Core

[![Nightly Build](https://github.com/xydesu/Core/actions/workflows/nightly.yml/badge.svg)](https://github.com/xydesu/Core/actions/workflows/nightly.yml)
[![Java CI](https://github.com/xydesu/Core/actions/workflows/maven.yml/badge.svg)](https://github.com/xydesu/Core/actions/workflows/maven.yml)

> ⚠️ **本專案目前處於早期且大量開發階段。** API 與功能可能隨時變更，不另行通知。

**Core** 是一款 PaperMC 插件，為 Minecraft 伺服器提供完整的 RPG 骨架——包括自訂生物、屬性驅動的物品系統、連段技能機制、玩家成長系統等。

📖 **English documentation → [README.md](README.md)**

---

## ✨ 功能一覽

| 功能 | 狀態 |
|---|---|
| 自訂生物（名稱顯示、自訂掉落物） | ✅ |
| 自訂物品（稀有度、品質隨機浮動、PDC 屬性儲存） | ✅ |
| RPG 屬性（力量 / 敏捷 / 智力 / 體力 / 靈巧） | ✅ |
| 生命值 / 魔力 / 耐力系統 | ✅ |
| 連段技能觸發（左鍵/右鍵點擊組合） | ✅ |
| 職業系統（戰士、法師、刺客……死神收割者） | ✅ |
| 物品欄 GUI（屬性面板、職業選擇、物品沙盒） | ✅ |
| 等級與經驗值系統 | ✅ |
| MariaDB 資料持久化（HikariCP 連接池） | ✅ |
| Action Bar HUD（HP / MP / SP 顯示） | ✅ |
| 屬性點分配系統 | ✅ |
| 資源包整合 | 🚧 開發中 |
| NPC / 任務系統 | 🚧 開發中 |
| 隊伍系統 | 🚧 開發中 |

---

## 📋 執行需求

| 需求 | 版本 |
|---|---|
| Java | 21+ |
| PaperMC | 1.21.x |
| [LibsDisguises](https://www.spigotmc.org/resources/libs-disguises-free.81/) | 11.x |
| MariaDB / MySQL | 任何現代版本 |

---

## ⚙️ 設定

第一次啟動伺服器後，插件資料夾內會自動生成 `config.yml`。請在啟動前修改資料庫設定：

```yaml
database:
  host: 127.0.0.1
  port: 3306
  database: minecraft
  username: core
  # 部署前請將此處改為實際的資料庫密碼
  password: CHANGE_ME
```

---

## 🔧 指令列表

| 指令 | 說明 |
|---|---|
| `/class` | 查看 / 設定 / 取消設定職業 |
| `/stats <屬性> <數值> [玩家]` | 設定玩家 RPG 屬性（需 OP）|
| `/ap give <玩家> <數量>` | 給予屬性點數（需 OP）|
| `/item [id]` | 取得自訂物品（或開啟沙盒介面）|
| `/iteminfo` | 顯示手持物品的詳細資訊 |
| `/menu` | 開啟玩家選單 |
| `/spawnmob` | 生成自訂生物 |
| `/loop <次數> <延遲> <指令>` | 重複執行指令（需 OP）|
| `/displaytest` | 測試 TextDisplay 渲染 |
| `/dialog` | 測試對話框 UI |

---

## 🏗️ 從原始碼建置

**前置需求：** JDK 21、Maven 3.8+

```bash
git clone https://github.com/xydesu/Core.git
cd Core
mvn clean package
```

帶有 Shade 的插件 Jar 位於 `target/Core-1.0.jar`。將其放入伺服器的 `plugins/` 資料夾即可使用。

> **每日 Nightly 版本** 會在每次推送至 `main` 分支或每天午夜（UTC）自動建置並發布。  
> 請至 [Releases 頁面](https://github.com/xydesu/Core/releases/tag/nightly) 下載最新版本。

---

## 📁 專案結構

```
Core/
├── src/main/java/me/xydesu/core/
│   ├── Command/        # 指令實作
│   ├── Database/       # MariaDB + HikariCP 資料持久化
│   ├── Dialog/         # Paper Dialog API 封裝
│   ├── Events/         # Bukkit 事件監聽器
│   ├── GUI/            # 物品欄 GUI 系統
│   ├── Item/           # 物品框架、稀有度、工具類型
│   ├── Mob/            # 自訂生物框架
│   ├── Player/         # 玩家資料模型與職業系統
│   ├── Tasks/          # BukkitRunnable 定時任務
│   └── Utils/          # 傷害計算、PDC 工具、Keys
├── src/main/resources/
│   ├── plugin.yml
│   └── config.yml
└── ResourcePack/       # 自訂材質與字型（開發中）
```

---

## 📝 授權

本專案目前未附帶授權條款，所有權利保留給作者。
