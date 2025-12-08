# Core Plugin

> **Note:** This project is currently in early and heavy development.

This project is a custom Minecraft server plugin designed to enhance gameplay with various RPG-style features. It serves as the core system for managing custom content, player statistics, and game mechanics.

## Overview

The Core Plugin integrates several key systems to provide a comprehensive backend for the server. It handles custom entities, item management, graphical user interfaces, and data persistence.

## Features

### Custom Mobs
The plugin implements a system for creating and managing custom mobs with unique attributes and behaviors.

### Custom Items
A framework for custom items and weapons is included, allowing for the creation of equipment with specialized stats and rarities.

### GUI System
An inventory-based GUI system is provided to facilitate user interaction with various plugin features, such as menus and stats displays.

### Player Statistics
The plugin tracks and manages custom player data, including health, mana, stamina, and experience, separate from vanilla Minecraft mechanics.

### Resource Pack Integration (W.I.P)
The project includes a resource pack structure to support custom textures and fonts required by the plugin's features.

## Project Structure

- **src/main/java**: Contains the Java source code for the plugin.
- **src/main/resources**: Contains configuration files and plugin descriptors.
- **ResourcePack**: Contains the assets for the server resource pack.

## Build

This project is built using Maven. To build the plugin, run the standard Maven package command. The output jar will be located in the `target` directory.

---

# Core 插件

> **注意：** 本專案目前處於早期且大量開發階段。

本專案是一個客製化的 Minecraft 伺服器插件，旨在透過各種 RPG 風格的功能來增強遊戲體驗。它作為管理自訂內容、玩家數據和遊戲機制的的核心系統。

## 概述

Core 插件整合了多個關鍵系統，為伺服器提供全面的後端支援。它處理自訂實體、物品管理、圖形使用者介面 (GUI) 以及資料持久化。

## 功能

### 自訂生物
本插件實作了一套系統，用於建立和管理具有獨特屬性和行為的自訂生物。

### 自訂物品
包含自訂物品和武器的框架，允許建立具有特殊屬性和稀有度的裝備。

### GUI 系統
提供基於物品欄的 GUI 系統，以促進使用者與各種插件功能的互動，例如選單和數據顯示。

### 玩家數據
本插件追蹤並管理自訂玩家數據，包括生命值、魔力、耐力和經驗值，這些數據獨立於原版 Minecraft 機制。

### 資源包整合 (W.I.P)
本專案包含資源包結構，以支援插件功能所需的自訂材質和字型。

## 專案結構

- **src/main/java**: 包含插件的 Java 原始碼。
- **src/main/resources**: 包含設定檔和插件描述檔。
- **ResourcePack**: 包含伺服器資源包的資產。

## 建置

本專案使用 Maven 建置。若要建置插件，請執行標準的 Maven package 指令。輸出的 jar 檔將位於 `target` 目錄中。
