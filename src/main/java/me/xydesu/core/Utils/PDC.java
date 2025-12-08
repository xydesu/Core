package me.xydesu.core.Utils;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class PDC {

    // Entity Methods
    public static <T, Z> Z get(Entity entity, NamespacedKey key, PersistentDataType<T, Z> type) {
        if (entity == null) return null;
        return entity.getPersistentDataContainer().get(key, type);
    }

    public static <T, Z> Z get(Entity entity, NamespacedKey key, PersistentDataType<T, Z> type, Z def) {
        Z value = get(entity, key, type);
        return value == null ? def : value;
    }

    public static <T, Z> void set(Entity entity, NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        if (entity == null) return;
        entity.getPersistentDataContainer().set(key, type, value);
    }

    public static <T, Z> boolean has(Entity entity, NamespacedKey key, PersistentDataType<T, Z> type) {
        if (entity == null) return false;
        return entity.getPersistentDataContainer().has(key, type);
    }

    // ItemStack Methods
    public static <T, Z> Z get(ItemStack item, NamespacedKey key, PersistentDataType<T, Z> type) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer().get(key, type);
    }

    public static <T, Z> Z get(ItemMeta meta, NamespacedKey key, PersistentDataType<T, Z> type) {
        if (meta == null) return null;
        return meta.getPersistentDataContainer().get(key, type);
    }

    public static <T, Z> Z get(ItemStack item, NamespacedKey key, PersistentDataType<T, Z> type, Z def) {
        Z value = get(item, key, type);
        return value == null ? def : value;
    }

    public static <T, Z> Z get(ItemMeta meta, NamespacedKey key, PersistentDataType<T, Z> type, Z def) {
        Z value = get(meta, key, type);
        return value == null ? def : value;
    }

    public static <T, Z> void set(ItemStack item, NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        if (item == null) return;
        item.editMeta(meta -> meta.getPersistentDataContainer().set(key, type, value));
    }

    public static <T, Z> void set(ItemMeta meta, NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        if (meta == null) return;
        meta.getPersistentDataContainer().set(key, type, value);
    }

    public static <T, Z> boolean has(ItemStack item, NamespacedKey key, PersistentDataType<T, Z> type) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(key, type);
    }

    public static <T, Z> boolean has(ItemMeta meta, NamespacedKey key, PersistentDataType<T, Z> type) {
        if (meta == null) return false;
        return meta.getPersistentDataContainer().has(key, type);
    }

}
