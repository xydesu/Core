package me.xydesu.core.Mob;

import me.xydesu.core.Mob.Mobs.SuperZombie;
import me.xydesu.core.Mob.Mobs.TestPlayer;
import me.xydesu.core.Utils.Keys;
import me.xydesu.core.Utils.PDC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import java.util.*;

public abstract class CustomMob {

    public static List<CustomMob> registeredMobs = new ArrayList<>();

    public static Map<LivingEntity, TextDisplay> line = new HashMap<>();

    static {
        registeredMobs.addAll(List.of(
                new SuperZombie(),
                new TestPlayer()
        ));
    }

    public abstract String getID();
    public abstract String getName();
    public abstract EntityType getType();
    public abstract double getMaxHealth();
    public abstract int getLevel();

    public double getDroppedExp() {
        return getLevel() * 10;
    }

    public List<ItemStack> getDrops() {
        return new ArrayList<>();
    }

    protected void tryDrop(List<ItemStack> drops, ItemStack item, double chance) {
        if (Math.random() <= chance) {
            drops.add(item);
        }
    }

    public abstract void onSpawn(LivingEntity entity);

    public LivingEntity spawn(Location location) {
        LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, getType());
        
        PDC.set(entity, Keys.CUSTOM_MOB, PersistentDataType.BOOLEAN, true);
        PDC.set(entity, Keys.ID, PersistentDataType.STRING, getID());
        PDC.set(entity, Keys.LEVEL, PersistentDataType.INTEGER, getLevel());
        PDC.set(entity, Keys.MAX_HEALTH, PersistentDataType.DOUBLE, getMaxHealth());
        PDC.set(entity, Keys.CURRENT_HEALTH, PersistentDataType.DOUBLE, getMaxHealth());
        PDC.set(entity, Keys.MOB_NAME, PersistentDataType.STRING, getName());

        onSpawn(entity);
        createNameLine(entity);
        updateNameTag(entity);

        return entity;
    }

    public static CustomMob get(String id) {
        for (CustomMob mob : registeredMobs) {
            if (mob.getID().equals(id)) {
                return mob;
            }
        }
        return null;
    }

    // Static Helper Methods for Entity Instances

    public static boolean isCustomMob(LivingEntity entity) {
        return PDC.has(entity, Keys.CUSTOM_MOB, PersistentDataType.BOOLEAN);
    }

    public static boolean damage(LivingEntity entity, double amount) {
        return damage(entity, amount, true);
    }

    public static boolean damage(LivingEntity entity, double amount, boolean applyDeath) {
        if (!isCustomMob(entity)) return false;
        
        double currentHealth = PDC.get(entity, Keys.CURRENT_HEALTH, PersistentDataType.DOUBLE, 0.0);
        double maxHealth = PDC.get(entity, Keys.MAX_HEALTH, PersistentDataType.DOUBLE, 100.0);
        
        currentHealth -= amount;
        if (currentHealth < 0) currentHealth = 0;
        
        PDC.set(entity, Keys.CURRENT_HEALTH, PersistentDataType.DOUBLE, currentHealth);
        updateNameTag(entity);

        if (currentHealth <= 0) {
            if (applyDeath) entity.setHealth(0); // Kill
            return true;
        } else {
            // Sync vanilla health
            double vanillaMax = entity.getAttribute(Attribute.MAX_HEALTH).getValue();
            double vanillaHealth = (currentHealth / maxHealth) * vanillaMax;
            entity.setHealth(Math.max(0.1, vanillaHealth));
            return false;
        }
    }

    public static void updateNameTag(LivingEntity entity) {
        if (!isCustomMob(entity)) return;

        int level = PDC.get(entity, Keys.LEVEL, PersistentDataType.INTEGER, 1);
        double current = PDC.get(entity, Keys.CURRENT_HEALTH, PersistentDataType.DOUBLE, 100.0);
        double max = PDC.get(entity, Keys.MAX_HEALTH, PersistentDataType.DOUBLE, 100.0);
        String name = PDC.get(entity, Keys.MOB_NAME, PersistentDataType.STRING);
        if (name == null) name = entity.getType().name();

        entity.setCustomNameVisible(false);

        // For Lines
        Component displayName = Component.text("[Lv." + level + "] ", NamedTextColor.GRAY)
                .append(Component.text(name + " ", NamedTextColor.GREEN))
                .appendNewline()
                .append(MiniMessage.miniMessage().deserialize(getProgressBar(current, max)))
                .append(Component.text(" ", NamedTextColor.GRAY))
                .append(Component.text(Math.round(current)+"❤", NamedTextColor.WHITE));
        
        if (!line.containsKey(entity)) {
            createNameLine(entity);
        }
        
        if (line.containsKey(entity)) {
            line.get(entity).text(displayName);
        }
    }

    public static void createNameLine(LivingEntity entity) {
        if (!isCustomMob(entity)) return;
        if (line.containsKey(entity)) return;

        int level = PDC.get(entity, Keys.LEVEL, PersistentDataType.INTEGER, 1);
        String name =  PDC.get(entity, Keys.MOB_NAME, PersistentDataType.STRING);
        if (name == null) name = entity.getType().name();

        double current = PDC.get(entity, Keys.CURRENT_HEALTH, PersistentDataType.DOUBLE, 100.0);
        double max = PDC.get(entity, Keys.MAX_HEALTH, PersistentDataType.DOUBLE, 100.0);
        World world =  entity.getWorld();
        Location location = entity.getLocation();

        TextDisplay display = world.spawn(location, TextDisplay.class);

        Component displayName = Component.text("[Lv." + level + "] ", NamedTextColor.GRAY)
                .append(Component.text(name + " ", NamedTextColor.GREEN))
                .appendNewline()
                .append(MiniMessage.miniMessage().deserialize(getProgressBar(current, max)))
                .append(Component.text(" ", NamedTextColor.GRAY))
                .append(Component.text(Math.round(current)+"❤", NamedTextColor.WHITE));

        display.text(displayName);
        display.setBillboard(Display.Billboard.CENTER);

        // 再往上加 0.3
        Transformation t = display.getTransformation();
        Vector3f pos = t.getTranslation();
        pos.add(0f, 0.3f, 0f);

        display.setTransformation(new Transformation(
                pos,
                t.getLeftRotation(),
                t.getScale(),
                t.getRightRotation()
        ));

        entity.addPassenger(display);



        line.put(entity, display);
    }

    public static String getProgressBar(double current, double max) {
        int totalBars = 20; // 固定長度，也可以改成參數
        char symbol = '|';  // 使用直線符號，比較緊湊

        double percent = current / max;
        if (percent < 0) percent = 0;
        if (percent > 1) percent = 1;

        int filledCount = (int) (totalBars * percent);
        int emptyCount = totalBars - filledCount;

        // 決定顏色邏輯
        String colorCode;
        if (percent > 0.5) {
            colorCode = "<green>";       // 血量 > 50% 綠色
        } else if (percent > 0.2) {
            colorCode = "<yellow>";      // 血量 > 20% 黃色
        } else {
            colorCode = "<red>";         // 血量 <= 20% 紅色
        }

        return colorCode + String.valueOf(symbol).repeat(filledCount) +
                "<gray>" + String.valueOf(symbol).repeat(emptyCount);
    }
    
    // Helper for manual setup (e.g. from command with custom args)
    public static void setCustomMob(LivingEntity entity, boolean isCustom) {
        PDC.set(entity, Keys.CUSTOM_MOB, PersistentDataType.BOOLEAN, isCustom);
        createNameLine(entity);
    }
    
    public static void setLevel(LivingEntity entity, int level) {
        PDC.set(entity, Keys.LEVEL, PersistentDataType.INTEGER, level);
        updateNameTag(entity);
    }
    
    public static void setMaxHealth(LivingEntity entity, double maxHealth) {
        PDC.set(entity, Keys.MAX_HEALTH, PersistentDataType.DOUBLE, maxHealth);
        updateNameTag(entity);
    }
    
    public static void setCurrentHealth(LivingEntity entity, double currentHealth) {
        PDC.set(entity, Keys.CURRENT_HEALTH, PersistentDataType.DOUBLE, currentHealth);
        updateNameTag(entity);
    }
    
    public static void setMobName(LivingEntity entity, String name) {
        PDC.set(entity, Keys.MOB_NAME, PersistentDataType.STRING, name);
        updateNameTag(entity);
    }
}
