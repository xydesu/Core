# Core

[![Nightly Build](https://github.com/xydesu/Core/actions/workflows/nightly.yml/badge.svg)](https://github.com/xydesu/Core/actions/workflows/nightly.yml)
[![Java CI](https://github.com/xydesu/Core/actions/workflows/maven.yml/badge.svg)](https://github.com/xydesu/Core/actions/workflows/maven.yml)

> ⚠️ **This project is in early, heavy development.** APIs and features may change without notice.

**Core** is a PaperMC plugin that provides a full RPG backbone for a Minecraft server — custom mobs, stat-driven items, a combo-based skill system, player progression, and more.

📖 **繁體中文說明請見 → [README_zh-TW.md](README_zh-TW.md)**

---

## ✨ Features

| Feature | Status |
|---|---|
| Custom Mobs (with name displays & custom drops) | ✅ |
| Custom Items (rarity, quality variance, per-stat PDC storage) | ✅ |
| RPG Stats (STR / AGI / INT / VIT / DEX) | ✅ |
| Health / Mana / Stamina system | ✅ |
| Combo-based Skill Trigger (L/R click patterns) | ✅ |
| Class system (Warrior, Mage, Rogue, … SoulReaper) | ✅ |
| Inventory GUI (stats, class picker, item sandbox) | ✅ |
| Level & EXP system | ✅ |
| MariaDB persistence (HikariCP connection pool) | ✅ |
| Action Bar HUD (HP / MP / SP) | ✅ |
| Attribute Points allocation | ✅ |
| Resource Pack integration | 🚧 WIP |
| NPC / Quest system | 🚧 WIP |
| Party system | 🚧 WIP |

---

## 📋 Requirements

| Requirement | Version |
|---|---|
| Java | 21+ |
| PaperMC | 1.21.x |
| [LibsDisguises](https://www.spigotmc.org/resources/libs-disguises-free.81/) | 11.x |
| MariaDB / MySQL | Any modern version |

---

## ⚙️ Configuration

After the first server start, a `config.yml` is generated in the plugin folder. Edit the database section before starting:

```yaml
database:
  host: 127.0.0.1
  port: 3306
  database: minecraft
  username: core
  # Change this to your actual database password before deploying
  password: CHANGE_ME
```

---

## 🔧 Commands

| Command | Description |
|---|---|
| `/class` | View / set / unset your class |
| `/stats <attribute> <value> [player]` | Set a player's RPG stat (OP) |
| `/ap give <player> <amount>` | Give attribute points (OP) |
| `/item [id]` | Get a custom item (or open sandbox) |
| `/iteminfo` | Show info for the held item |
| `/menu` | Open the player menu |
| `/spawnmob` | Spawn a custom mob |
| `/loop <times> <delay> <command>` | Run a command repeatedly (OP) |
| `/displaytest` | Test TextDisplay rendering |
| `/dialog` | Test dialog UI |

---

## 🏗️ Building from Source

**Prerequisites:** JDK 21, Maven 3.8+

```bash
git clone https://github.com/xydesu/Core.git
cd Core
mvn clean package
```

The shaded plugin jar is at `target/Core-1.0.jar`. Drop it into your server's `plugins/` folder.

> **Nightly builds** are published automatically on every push to `main` and daily at midnight UTC.  
> Download the latest from the [Releases page](https://github.com/xydesu/Core/releases/tag/nightly).

---

## 📁 Project Structure

```
Core/
├── src/main/java/me/xydesu/core/
│   ├── Command/        # Command implementations
│   ├── Database/       # MariaDB + HikariCP persistence
│   ├── Dialog/         # Paper Dialog API wrappers
│   ├── Events/         # Bukkit event listeners
│   ├── GUI/            # Inventory-based GUI system
│   ├── Item/           # Item framework, rarity, tool types
│   ├── Mob/            # Custom mob framework
│   ├── Player/         # Player data model & class system
│   ├── Tasks/          # BukkitRunnable tasks
│   └── Utils/          # Damage calc, PDC helpers, keys
├── src/main/resources/
│   ├── plugin.yml
│   └── config.yml
└── ResourcePack/       # Custom textures & fonts (WIP)
```

---

## 📝 License

This project does not currently ship with a license. All rights reserved by the author.
