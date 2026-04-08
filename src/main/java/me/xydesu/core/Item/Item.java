package me.xydesu.core.Item;

import me.xydesu.core.Item.Items.Accessories.TestAmulet;
import me.xydesu.core.Item.Items.Accessories.TestNecklace;
import me.xydesu.core.Item.Items.Accessories.TestRing;
import me.xydesu.core.Item.Items.Armor.*;
import me.xydesu.core.Item.Items.Misc.TestConsumable;
import me.xydesu.core.Item.Items.Misc.TestMaterial;
import me.xydesu.core.Item.Items.Misc.TestMisc;
import me.xydesu.core.Item.Items.Misc.TestQuestItem;
import me.xydesu.core.Item.Items.Tools.*;
import me.xydesu.core.Item.Items.Weapons.Bows.BowTest;
import me.xydesu.core.Player.Class.ClassManager;
import me.xydesu.core.Item.Items.Weapons.Crossbows.TestCrossbow;
import me.xydesu.core.Item.Items.Weapons.Daggers.TestDagger;
import me.xydesu.core.Item.Items.Weapons.Greatswords.TestGreatsword;
import me.xydesu.core.Item.Items.Weapons.Scythe.AoeTest;
import me.xydesu.core.Item.Items.Weapons.Scythe.ReaperOfFate;
import me.xydesu.core.Item.Items.Weapons.Scythe.TestScythe;
import me.xydesu.core.Item.Items.Weapons.Staffs.TestStaff;
import me.xydesu.core.Item.Items.Weapons.Swords.Hyperion;
import me.xydesu.core.Item.Items.Weapons.Swords.TestSword;
import me.xydesu.core.Player.Player;
import me.xydesu.core.Utils.Keys;
import me.xydesu.core.Utils.PDC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import com.google.common.collect.ArrayListMultimap;

import java.util.ArrayList;
import java.util.List;

public abstract class Item {

    MiniMessage mm = MiniMessage.miniMessage();

    List<ItemStack> items = new ArrayList<>();
    private static final List<Item> registeredItems = new ArrayList<>();

    public static List<Item> getRegisteredItems() {
        return java.util.Collections.unmodifiableList(registeredItems);
    }

    static {
        registeredItems.addAll(List.of(
                new TestSword(),
                new TestGreatsword(),
                new TestDagger(),
                new BowTest(),
                new TestCrossbow(),
                new TestStaff(),
                new TestScythe(),
                new AoeTest(),
                new ReaperOfFate(),
                new Hyperion(),

                // Tools
                new TestAxe(),
                new TestPickaxe(),
                new TestShovel(),
                new TestHoe(),
                new TestFishingRod(),

                // Armor
                new TestHelmet(),
                new TestChestplate(),
                new TestLeggings(),
                new TestBoots(),
                new TestCloak(),

                // Accessories
                new TestRing(),
                new TestNecklace(),
                new TestAmulet(),

                // Misc
                new TestConsumable(),
                new TestQuestItem(),
                new TestMaterial(),
                new TestMisc()));
    }

    public abstract String getID();

    public abstract String getName();

    public abstract Material getMaterial();

    public abstract ToolType getToolType();

    public double getDamage() {
        return 0;
    }

    public double getStrength() {
        return 0;
    }

    public double getDefense() {
        return 0;
    }

    public double getCritChance() {
        return 0;
    }

    public double getCritDamage() {
        return 0;
    }

    public double getMaxMana() {
        return 0;
    }

    public double getManaRegen() {
        return 0;
    }

    public double getMaxHealth() {
        return 0;
    }

    public double getHealthRegen() {
        return 0;
    }

    public double getAttackSpeed() {
        return 0;
    }

    public double getElementalDamage() {
        return 0;
    }

    public double getMovementSpeed() {
        return 0;
    }

    public double getLifeSteal() {
        return 0;
    }

    public double getRange() {
        return 0;
    }

    public int getRequiredLevel() {
        return 0;
    }

    public double getStatVariance() {
        return 0.1; // +/- 10%
    }

    public boolean hasAOE() {
        return false;
    }

    public List<String> getLore() {
        return new ArrayList<>();
    }

    public boolean isUnbreakable() {
        return true;
    }

    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    public void onInteract(PlayerInteractEvent event) {
    }

    /**
     * Returns the set ID this item belongs to, or {@code null} if it is not
     * part of any set.  Override in subclasses to declare set membership.
     */
    public String getSetID() {
        return null;
    }

    public ItemStack getItem() {
        ItemStack item = new ItemStack(getMaterial());
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);

            Component displayName;
            if (getRarity() == Rarity.MYTHIC) {
                displayName = mm.deserialize("<italic:false><rainbow>" + getName() + "</rainbow>");
            } else {
                displayName = Component.text(getName()).color(getRarity().getColor())
                        .decoration(net.kyori.adventure.text.format.TextDecoration.ITALIC, false);
            }
            meta.displayName(displayName);

            // Random Quality Generation
            double overallQuality = Math.random(); // 0.0 to 1.0
            double variance = getStatVariance();

            // Generate individual qualities centered around overallQuality
            // This ensures the average quality is close to overallQuality, preventing the
            // "bell curve" effect around 50%
            double dev = 0.15; // +/- 15% deviation

            double qDamage = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
            double qStrength = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
            double qDefense = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
            double qCritChance = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
            double qCritDamage = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
            double qMaxHealth = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
            double qHealthRegen = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
            double qMaxMana = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
            double qManaRegen = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
            double qAttackSpeed = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
            double qElementalDamage = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
            double qMovementSpeed = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
            double qLifeSteal = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));

            PDC.set(meta, Keys.ID, PersistentDataType.STRING, getID());
            PDC.set(meta, Keys.QUALITY, PersistentDataType.DOUBLE, overallQuality); // Keep overall for display score

            // Store individual qualities
            PDC.set(meta, Keys.QUALITY_DAMAGE, PersistentDataType.DOUBLE, qDamage);
            PDC.set(meta, Keys.QUALITY_STRENGTH, PersistentDataType.DOUBLE, qStrength);
            PDC.set(meta, Keys.QUALITY_DEFENSE, PersistentDataType.DOUBLE, qDefense);
            PDC.set(meta, Keys.QUALITY_CRIT_CHANCE, PersistentDataType.DOUBLE, qCritChance);
            PDC.set(meta, Keys.QUALITY_CRIT_DAMAGE, PersistentDataType.DOUBLE, qCritDamage);
            PDC.set(meta, Keys.QUALITY_MAX_HEALTH, PersistentDataType.DOUBLE, qMaxHealth);
            PDC.set(meta, Keys.QUALITY_HEALTH_REGEN, PersistentDataType.DOUBLE, qHealthRegen);
            PDC.set(meta, Keys.QUALITY_MAX_MANA, PersistentDataType.DOUBLE, qMaxMana);
            PDC.set(meta, Keys.QUALITY_MANA_REGEN, PersistentDataType.DOUBLE, qManaRegen);
            PDC.set(meta, Keys.QUALITY_ATTACK_SPEED, PersistentDataType.DOUBLE, qAttackSpeed);
            PDC.set(meta, Keys.QUALITY_ELEMENTAL_DAMAGE, PersistentDataType.DOUBLE, qElementalDamage);
            PDC.set(meta, Keys.QUALITY_MOVEMENT_SPEED, PersistentDataType.DOUBLE, qMovementSpeed);
            PDC.set(meta, Keys.QUALITY_LIFE_STEAL, PersistentDataType.DOUBLE, qLifeSteal);

            PDC.set(meta, Keys.REQUIRED_LEVEL, PersistentDataType.INTEGER, getRequiredLevel());

            // Store Randomized Stats (using individual multipliers)
            PDC.set(meta, Keys.DAMAGE, PersistentDataType.DOUBLE, getDamage() * (1.0 + (qDamage - 0.5) * 2 * variance));
            PDC.set(meta, Keys.STRENGTH, PersistentDataType.DOUBLE,
                    getStrength() * (1.0 + (qStrength - 0.5) * 2 * variance));
            PDC.set(meta, Keys.DEFENSE, PersistentDataType.DOUBLE,
                    getDefense() * (1.0 + (qDefense - 0.5) * 2 * variance));
            PDC.set(meta, Keys.CRIT_CHANCE, PersistentDataType.DOUBLE,
                    getCritChance() * (1.0 + (qCritChance - 0.5) * 2 * variance));
            PDC.set(meta, Keys.CRIT_DAMAGE, PersistentDataType.DOUBLE,
                    getCritDamage() * (1.0 + (qCritDamage - 0.5) * 2 * variance));
            PDC.set(meta, Keys.MAX_HEALTH, PersistentDataType.DOUBLE,
                    getMaxHealth() * (1.0 + (qMaxHealth - 0.5) * 2 * variance));
            PDC.set(meta, Keys.HEALTH_REGEN, PersistentDataType.DOUBLE,
                    getHealthRegen() * (1.0 + (qHealthRegen - 0.5) * 2 * variance));
            PDC.set(meta, Keys.MAX_MANA, PersistentDataType.DOUBLE,
                    getMaxMana() * (1.0 + (qMaxMana - 0.5) * 2 * variance));
            PDC.set(meta, Keys.MANA_REGEN, PersistentDataType.DOUBLE,
                    getManaRegen() * (1.0 + (qManaRegen - 0.5) * 2 * variance));
            PDC.set(meta, Keys.ATTACK_SPEED, PersistentDataType.DOUBLE,
                    getAttackSpeed() * (1.0 + (qAttackSpeed - 0.5) * 2 * variance));
            PDC.set(meta, Keys.ELEMENTAL_DAMAGE, PersistentDataType.DOUBLE,
                    getElementalDamage() * (1.0 + (qElementalDamage - 0.5) * 2 * variance));
            PDC.set(meta, Keys.MOVEMENT_SPEED, PersistentDataType.DOUBLE,
                    getMovementSpeed() * (1.0 + (qMovementSpeed - 0.5) * 2 * variance));
            PDC.set(meta, Keys.LIFE_STEAL, PersistentDataType.DOUBLE,
                    getLifeSteal() * (1.0 + (qLifeSteal - 0.5) * 2 * variance));

            PDC.set(meta, Keys.RANGE, PersistentDataType.DOUBLE, getRange());
            PDC.set(meta, Keys.AOE, PersistentDataType.BOOLEAN, hasAOE());
            PDC.set(meta, Keys.RARITY, PersistentDataType.STRING, getRarity().name());
            if (getSetID() != null) {
                PDC.set(meta, Keys.SET_ID, PersistentDataType.STRING, getSetID());
            }

            meta.lore(buildLore(this, meta));
            meta.setUnbreakable(isUnbreakable());
            meta.setAttributeModifiers(ArrayListMultimap.create());

            item.setItemMeta(meta);
        }
        return item;
    }

    public ItemStack getDisplayItem() {
        ItemStack item = new ItemStack(getMaterial());
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
            meta.setAttributeModifiers(ArrayListMultimap.create());

            Component displayName;
            if (getRarity() == Rarity.MYTHIC) {
                displayName = mm.deserialize("<italic:false><rainbow>" + getName() + "</rainbow>");
            } else {
                displayName = Component.text(getName()).color(getRarity().getColor())
                        .decoration(net.kyori.adventure.text.format.TextDecoration.ITALIC, false);
            }
            meta.displayName(displayName);

            PDC.set(meta, Keys.ID, PersistentDataType.STRING, getID());
            PDC.set(meta, Keys.RARITY, PersistentDataType.STRING, getRarity().name());

            meta.lore(buildDisplayLore(this));
            meta.setUnbreakable(isUnbreakable());

            item.setItemMeta(meta);
        }
        return item;
    }

    private static List<Component> buildDisplayLore(Item item) {
        List<Component> lore = new ArrayList<>();
        MiniMessage mm = MiniMessage.miniMessage();
        Component separator = mm.deserialize("<gray>═════════════════════");

        // Header
        String rarityName = item.getRarity().getLoreTag();
        String typeName = item.getToolType().getLoreTag();
        lore.add(mm.deserialize("<italic:false>" + rarityName));
        lore.add(mm.deserialize("<italic:false>" + typeName));
        lore.add(separator);

        double variance = item.getStatVariance();
        double minMult = 1.0 - variance;
        double maxMult = 1.0 + variance;

        // Stats
        List<Component> stats = new ArrayList<>();
        if (item.getDamage() != 0)
            stats.add(mm.deserialize("<italic:false><gray>攻擊力：<gold>"
                    + String.format(java.util.Locale.US, "%.1f", item.getDamage() * minMult) + "-"
                    + String.format(java.util.Locale.US, "%.1f", item.getDamage() * maxMult)));
        if (item.getStrength() != 0)
            stats.add(mm.deserialize("<italic:false><gray>力量：<gold>"
                    + String.format(java.util.Locale.US, "%.1f", item.getStrength() * minMult) + "-"
                    + String.format(java.util.Locale.US, "%.1f", item.getStrength() * maxMult)));
        if (item.getDefense() != 0)
            stats.add(mm.deserialize("<italic:false><gray>防禦：<gold>"
                    + String.format(java.util.Locale.US, "%.1f", item.getDefense() * minMult) + "-"
                    + String.format(java.util.Locale.US, "%.1f", item.getDefense() * maxMult)));
        if (item.getCritChance() != 0)
            stats.add(mm.deserialize("<italic:false><gray>暴擊率：<gold>"
                    + String.format(java.util.Locale.US, "%.1f", item.getCritChance() * 100 * minMult) + "-"
                    + String.format(java.util.Locale.US, "%.1f", item.getCritChance() * 100 * maxMult) + "%"));
        if (item.getCritDamage() != 0)
            stats.add(mm.deserialize("<italic:false><gray>暴擊傷害：<gold>"
                    + String.format(java.util.Locale.US, "%.1f", item.getCritDamage() * 100 * minMult) + "-"
                    + String.format(java.util.Locale.US, "%.1f", item.getCritDamage() * 100 * maxMult) + "%"));
        if (item.getMaxHealth() != 0)
            stats.add(mm.deserialize("<italic:false><gray>生命值：<gold>"
                    + String.format(java.util.Locale.US, "%.1f", item.getMaxHealth() * minMult) + "-"
                    + String.format(java.util.Locale.US, "%.1f", item.getMaxHealth() * maxMult)));
        if (item.getHealthRegen() != 0)
            stats.add(mm.deserialize("<italic:false><gray>生命回復：<gold>"
                    + String.format(java.util.Locale.US, "%.1f", item.getHealthRegen() * minMult) + "-"
                    + String.format(java.util.Locale.US, "%.1f", item.getHealthRegen() * maxMult) + "%"));
        if (item.getMaxMana() != 0)
            stats.add(mm.deserialize("<italic:false><gray>魔力：<gold>"
                    + String.format(java.util.Locale.US, "%.1f", item.getMaxMana() * minMult) + "-"
                    + String.format(java.util.Locale.US, "%.1f", item.getMaxMana() * maxMult)));
        if (item.getManaRegen() != 0)
            stats.add(mm.deserialize("<italic:false><gray>魔力回復：<gold>"
                    + String.format(java.util.Locale.US, "%.1f", item.getManaRegen() * minMult) + "-"
                    + String.format(java.util.Locale.US, "%.1f", item.getManaRegen() * maxMult) + "%"));
        if (item.getAttackSpeed() != 0)
            stats.add(mm.deserialize("<italic:false><gray>攻擊速度：<gold>"
                    + String.format(java.util.Locale.US, "%.1f", item.getAttackSpeed() * minMult) + "-"
                    + String.format(java.util.Locale.US, "%.1f", item.getAttackSpeed() * maxMult) + "%"));
        if (item.getElementalDamage() != 0)
            stats.add(mm.deserialize("<italic:false><gray>元素傷害：<gold>"
                    + String.format(java.util.Locale.US, "%.1f", item.getElementalDamage() * minMult) + "-"
                    + String.format(java.util.Locale.US, "%.1f", item.getElementalDamage() * maxMult)));
        if (item.getMovementSpeed() != 0)
            stats.add(mm.deserialize("<italic:false><gray>移動速度：<gold>"
                    + String.format(java.util.Locale.US, "%.1f", item.getMovementSpeed() * minMult) + "-"
                    + String.format(java.util.Locale.US, "%.1f", item.getMovementSpeed() * maxMult) + "%"));
        if (item.getRange() != 0)
            stats.add(mm.deserialize(
                    "<italic:false><gray>攻擊距離：<gold>+" + String.format(java.util.Locale.US, "%.1f", item.getRange())));

        if (!stats.isEmpty()) {
            lore.addAll(stats);
            lore.add(Component.empty());
        }

        // Quality Range Info
        lore.add(mm.deserialize("<italic:false><gray>品質浮動：<yellow>+/- " + (int) (variance * 100) + "%"));

        // Requirement
        if (item.getRequiredLevel() > 0) {
            lore.add(mm.deserialize("<italic:false><red>需求等級：" + item.getRequiredLevel()));
        }

        lore.add(separator);

        // Class Usability (Added)
        if (item.getToolType().isWeapon()) {
            List<String> validClasses = new ArrayList<>();
            for (ClassManager cm : ClassManager.getAllClasses()) {
                if (cm.getWeaponType() == item.getToolType()) {
                    validClasses.add(cm.classDisplay());
                }
            }
            if (!validClasses.isEmpty()) {
                lore.add(mm.deserialize("<italic:false><gray>適用職業：<white>" + String.join(", ", validClasses)));
                lore.add(separator);
            }
        }

        // Description
        List<String> desc = item.getLore();
        if (!desc.isEmpty()) {
            for (String line : desc) {
                for (String wrapped : wrapText(line, 40)) {
                    lore.add(mm.deserialize("<italic:false><gray>" + wrapped));
                }
            }
            lore.add(separator);
        }

        return lore;
    }

    private static List<Component> buildLore(Item item, ItemMeta meta) {
        List<Component> lore = new ArrayList<>();
        MiniMessage mm = MiniMessage.miniMessage();
        Component separator = mm.deserialize("<gray>═════════════════════");

        // Header
        String rarityName = item.getRarity().getLoreTag();
        String typeName = item.getToolType().getLoreTag();
        lore.add(mm.deserialize("<italic:false>" + rarityName));
        lore.add(mm.deserialize("<italic:false>" + typeName));
        lore.add(separator);

        double variance = item.getStatVariance();
        List<Component> stats = new ArrayList<>();
        List<Double> qualities = new ArrayList<>();

        // Damage
        if (item.getDamage() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_DAMAGE, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            double val = item.getDamage() * (1.0 + (q - 0.5) * 2 * variance);
            stats.add(formatStat(mm, "攻擊力", val, q, false));
        }
        // Strength
        if (item.getStrength() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_STRENGTH, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            double val = item.getStrength() * (1.0 + (q - 0.5) * 2 * variance);
            stats.add(formatStat(mm, "力量", val, q, false));
        }
        // Defense
        if (item.getDefense() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_DEFENSE, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            double val = item.getDefense() * (1.0 + (q - 0.5) * 2 * variance);
            stats.add(formatStat(mm, "防禦", val, q, false));
        }
        // Crit Chance
        if (item.getCritChance() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_CRIT_CHANCE, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            double val = item.getCritChance() * (1.0 + (q - 0.5) * 2 * variance);
            stats.add(formatStat(mm, "暴擊率", val * 100, q, true));
        }
        // Crit Damage
        if (item.getCritDamage() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_CRIT_DAMAGE, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            double val = item.getCritDamage() * (1.0 + (q - 0.5) * 2 * variance);
            stats.add(formatStat(mm, "暴擊傷害", val * 100, q, true));
        }
        // Max Health
        if (item.getMaxHealth() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_MAX_HEALTH, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            double val = item.getMaxHealth() * (1.0 + (q - 0.5) * 2 * variance);
            stats.add(formatStat(mm, "生命值", val, q, false));
        }
        // Health Regen
        if (item.getHealthRegen() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_HEALTH_REGEN, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            double val = item.getHealthRegen() * (1.0 + (q - 0.5) * 2 * variance);
            stats.add(formatStat(mm, "生命回復", val, q, true));
        }
        // Max Mana
        if (item.getMaxMana() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_MAX_MANA, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            double val = item.getMaxMana() * (1.0 + (q - 0.5) * 2 * variance);
            stats.add(formatStat(mm, "魔力", val, q, false));
        }
        // Mana Regen
        if (item.getManaRegen() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_MANA_REGEN, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            double val = item.getManaRegen() * (1.0 + (q - 0.5) * 2 * variance);
            stats.add(formatStat(mm, "魔力回復", val, q, true));
        }
        // Attack Speed
        if (item.getAttackSpeed() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_ATTACK_SPEED, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            double val = item.getAttackSpeed() * (1.0 + (q - 0.5) * 2 * variance);
            stats.add(formatStat(mm, "攻擊速度", val, q, true));
        }
        // Elemental Damage
        if (item.getElementalDamage() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_ELEMENTAL_DAMAGE, PersistentDataType.DOUBLE, 0.5)
                    : 0.5;
            qualities.add(q);
            double val = item.getElementalDamage() * (1.0 + (q - 0.5) * 2 * variance);
            stats.add(formatStat(mm, "元素傷害", val, q, false));
        }
        // Movement Speed
        if (item.getMovementSpeed() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_MOVEMENT_SPEED, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            double val = item.getMovementSpeed() * (1.0 + (q - 0.5) * 2 * variance);
            stats.add(formatStat(mm, "移動速度", val, q, true));
        }
        // Life Steal
        if (item.getLifeSteal() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_LIFE_STEAL, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            double val = item.getLifeSteal() * (1.0 + (q - 0.5) * 2 * variance);
            stats.add(formatStat(mm, "生命竊取", val, q, true));
        }
        // Range
        if (item.getRange() != 0) {
            stats.add(mm.deserialize(
                    "<italic:false><gray>攻擊距離：<white>+" + String.format(java.util.Locale.US, "%.1f", item.getRange())));
        }

        if (!stats.isEmpty()) {
            lore.addAll(stats);
            lore.add(Component.empty());
        }

        // Calculate Average Quality
        double avgQuality = 0.5;
        if (!qualities.isEmpty()) {
            double sum = 0;
            for (Double d : qualities)
                sum += d;
            avgQuality = sum / qualities.size();
        } else if (meta != null) {
            Double q = PDC.get(meta, Keys.QUALITY, PersistentDataType.DOUBLE);
            if (q != null)
                avgQuality = q;
        }

        // Quality Score Display
        int qualityScore = (int) (avgQuality * 100);
        String grade;
        if (qualityScore >= 100)
            grade = "<rainbow>SSS</rainbow>";
        else if (qualityScore >= 95)
            grade = "<gold>SS</gold>";
        else if (qualityScore >= 90)
            grade = "<yellow>S</yellow>";
        else if (qualityScore >= 80)
            grade = "<dark_purple>A</dark_purple>";
        else if (qualityScore >= 60)
            grade = "<blue>B</blue>";
        else if (qualityScore >= 40)
            grade = "<green>C</green>";
        else
            grade = "<gray>D</gray>";

        lore.add(mm.deserialize("<italic:false><gray>品質：<reset>" + grade + " <dark_gray>(" + qualityScore + "%)"));

        // Requirement
        if (item.getRequiredLevel() > 0) {
            lore.add(mm.deserialize("<italic:false><red>需求等級：" + item.getRequiredLevel()));
        }

        lore.add(separator);

        // Class Usability (Added)
        if (item.getToolType().isWeapon()) {
            List<String> validClasses = new ArrayList<>();
            for (ClassManager cm : ClassManager.getAllClasses()) {
                if (cm.getWeaponType() == item.getToolType()) {
                    validClasses.add(cm.classDisplay());
                }
            }
            if (!validClasses.isEmpty()) {
                lore.add(mm.deserialize("<italic:false><gray>適用職業：<white>" + String.join(", ", validClasses)));
                lore.add(separator);
            }
        }

        // Description
        List<String> desc = item.getLore();
        if (!desc.isEmpty()) {
            for (String line : desc) {
                for (String wrapped : wrapText(line, 40)) {
                    lore.add(mm.deserialize("<italic:false><gray>" + wrapped));
                }
            }
            lore.add(separator);
        }

        return lore;
    }

    private static Component formatStat(MiniMessage mm, String name, double value, double quality, boolean isPercent) {
        String valueColor;
        if (quality >= 1.0)
            valueColor = "<light_purple>";
        else if (quality >= 0.95)
            valueColor = "<gold>";
        else if (quality >= 0.90)
            valueColor = "<red>";
        else if (quality >= 0.80)
            valueColor = "<dark_purple>";
        else if (quality >= 0.60)
            valueColor = "<blue>";
        else if (quality >= 0.40)
            valueColor = "<green>";
        else
            valueColor = "<gray>";

        String valStr = "+" + String.format(java.util.Locale.US, "%.1f", value);
        if (isPercent)
            valStr += "%";

        return mm.deserialize("<italic:false><gray>" + name + "：" + valueColor + valStr);
    }

    private static List<String> wrapText(String text, int maxCharWidth) {
        List<String> result = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            result.add("");
            return result;
        }

        StringBuilder currentLine = new StringBuilder();
        int currentWidth = 0;
        boolean inTag = false;
        StringBuilder currentTag = new StringBuilder();
        List<String> activeTags = new ArrayList<>();

        for (char c : text.toCharArray()) {
            if (c == '<') {
                inTag = true;
                currentTag = new StringBuilder();
                currentTag.append(c);
            } else if (c == '>') {
                inTag = false;
                currentTag.append(c);
                String tag = currentTag.toString();
                currentLine.append(tag);

                if (tag.equalsIgnoreCase("<reset>")) {
                    activeTags.clear();
                } else if (tag.startsWith("</")) {
                    String tagName = tag.substring(2, tag.length() - 1);
                    for (int i = activeTags.size() - 1; i >= 0; i--) {
                        String t = activeTags.get(i);
                        String tName = t.substring(1, t.length() - 1);
                        if (tName.startsWith(tagName)) {
                            activeTags.remove(i);
                            break;
                        }
                    }
                } else {
                    if (!tag.equalsIgnoreCase("<br>")) {
                        activeTags.add(tag);
                    }
                }
            } else if (inTag) {
                currentTag.append(c);
            } else {
                currentLine.append(c);
                int charWidth = (c > 127) ? 2 : 1;
                currentWidth += charWidth;

                if (currentWidth >= maxCharWidth) {
                    result.add(currentLine.toString());
                    currentLine = new StringBuilder();
                    currentWidth = 0;
                    for (String t : activeTags) {
                        currentLine.append(t);
                    }
                }
            }
        }

        if (currentLine.length() > 0) {
            result.add(currentLine.toString());
        }
        return result;
    }

    public static ItemStack createItem(String ID) {
        for (Item item : registeredItems) {
            if (item.getID().equals(ID)) {
                return item.getItem();
            }
        }
        return null;
    }

    public static void updateLore(ItemStack item) {
        String id = PDC.get(item, Keys.ID, PersistentDataType.STRING, null);
        if (id == null)
            return;

        List<Component> lore = new ArrayList<>();
        MiniMessage mm = MiniMessage.miniMessage();

        Item customItem = null;
        for (Item i : registeredItems) {
            if (i.getID().equals(id)) {
                customItem = i;
                break;
            }
        }

        if (customItem != null) {
            lore = buildLore(customItem, item.getItemMeta());
        } else if (id.startsWith("VANILLA_")) {
            double damage = PDC.get(item, Keys.DAMAGE, PersistentDataType.DOUBLE, 0.0);

            if (damage > 0) {
                lore.add(mm.deserialize("<italic:false><gray>傷害: <red>" + damage));
            }

            lore.add(mm.deserialize("<italic:false>").append(mm.deserialize(Rarity.COMMON.getLoreTag())));
        } else {
            return;
        }

        ItemMeta currentMeta = item.getItemMeta();
        currentMeta.lore(lore);

        item.setItemMeta(currentMeta);
    }

    public static ToolType getToolType(ItemStack item) {
        if (item == null || item.getType() == Material.AIR)
            return null;
        String id = PDC.get(item, Keys.ID, PersistentDataType.STRING, null);
        if (id == null)
            return null;

        for (Item registered : registeredItems) {
            if (registered.getID().equals(id)) {
                return registered.getToolType();
            }
        }
        return null;
    }

    public static boolean canUse(Player player, ItemStack item) {
        ToolType type = getToolType(item);
        if (type == null)
            return true; // Only restrict custom items

        if (!type.isWeapon())
            return true; // Non-weapons are usable by everyone

        if (player.getPlayerClass() == null)
            return false; // logic: weapon requires class match, no class = mismatch

        return player.getPlayerClass().getWeaponType() == type;
    }

    /**
     * Re-rolls all quality values on the given item in-place, consuming one
     * item from the provided material stack.
     *
     * @param target   the item to reforge (must be a registered custom item)
     * @param material the material stack used as the reforge cost (one is consumed)
     * @return {@code true} if the reforge was successful, {@code false} if the
     *         item is not reforgeable or either argument is null/air
     */
    public static boolean reforge(ItemStack target, ItemStack material) {
        if (target == null || target.getType().isAir()) return false;
        if (material == null || material.getType().isAir()) return false;

        String id = PDC.get(target, Keys.ID, PersistentDataType.STRING, null);
        if (id == null || id.startsWith("VANILLA_")) return false;

        Item template = null;
        for (Item registered : registeredItems) {
            if (registered.getID().equals(id)) {
                template = registered;
                break;
            }
        }
        if (template == null) return false;

        ItemMeta meta = target.getItemMeta();
        if (meta == null) return false;

        double variance = template.getStatVariance();
        double overallQuality = Math.random();
        double dev = 0.15;

        double qDamage = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
        double qStrength = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
        double qDefense = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
        double qCritChance = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
        double qCritDamage = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
        double qMaxHealth = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
        double qHealthRegen = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
        double qMaxMana = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
        double qManaRegen = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
        double qAttackSpeed = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
        double qElementalDamage = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
        double qMovementSpeed = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));
        double qLifeSteal = Math.max(0, Math.min(1, overallQuality + (Math.random() * 2 - 1) * dev));

        PDC.set(meta, Keys.QUALITY, PersistentDataType.DOUBLE, overallQuality);
        PDC.set(meta, Keys.QUALITY_DAMAGE, PersistentDataType.DOUBLE, qDamage);
        PDC.set(meta, Keys.QUALITY_STRENGTH, PersistentDataType.DOUBLE, qStrength);
        PDC.set(meta, Keys.QUALITY_DEFENSE, PersistentDataType.DOUBLE, qDefense);
        PDC.set(meta, Keys.QUALITY_CRIT_CHANCE, PersistentDataType.DOUBLE, qCritChance);
        PDC.set(meta, Keys.QUALITY_CRIT_DAMAGE, PersistentDataType.DOUBLE, qCritDamage);
        PDC.set(meta, Keys.QUALITY_MAX_HEALTH, PersistentDataType.DOUBLE, qMaxHealth);
        PDC.set(meta, Keys.QUALITY_HEALTH_REGEN, PersistentDataType.DOUBLE, qHealthRegen);
        PDC.set(meta, Keys.QUALITY_MAX_MANA, PersistentDataType.DOUBLE, qMaxMana);
        PDC.set(meta, Keys.QUALITY_MANA_REGEN, PersistentDataType.DOUBLE, qManaRegen);
        PDC.set(meta, Keys.QUALITY_ATTACK_SPEED, PersistentDataType.DOUBLE, qAttackSpeed);
        PDC.set(meta, Keys.QUALITY_ELEMENTAL_DAMAGE, PersistentDataType.DOUBLE, qElementalDamage);
        PDC.set(meta, Keys.QUALITY_MOVEMENT_SPEED, PersistentDataType.DOUBLE, qMovementSpeed);
        PDC.set(meta, Keys.QUALITY_LIFE_STEAL, PersistentDataType.DOUBLE, qLifeSteal);

        PDC.set(meta, Keys.DAMAGE, PersistentDataType.DOUBLE, template.getDamage() * (1.0 + (qDamage - 0.5) * 2 * variance));
        PDC.set(meta, Keys.STRENGTH, PersistentDataType.DOUBLE, template.getStrength() * (1.0 + (qStrength - 0.5) * 2 * variance));
        PDC.set(meta, Keys.DEFENSE, PersistentDataType.DOUBLE, template.getDefense() * (1.0 + (qDefense - 0.5) * 2 * variance));
        PDC.set(meta, Keys.CRIT_CHANCE, PersistentDataType.DOUBLE, template.getCritChance() * (1.0 + (qCritChance - 0.5) * 2 * variance));
        PDC.set(meta, Keys.CRIT_DAMAGE, PersistentDataType.DOUBLE, template.getCritDamage() * (1.0 + (qCritDamage - 0.5) * 2 * variance));
        PDC.set(meta, Keys.MAX_HEALTH, PersistentDataType.DOUBLE, template.getMaxHealth() * (1.0 + (qMaxHealth - 0.5) * 2 * variance));
        PDC.set(meta, Keys.HEALTH_REGEN, PersistentDataType.DOUBLE, template.getHealthRegen() * (1.0 + (qHealthRegen - 0.5) * 2 * variance));
        PDC.set(meta, Keys.MAX_MANA, PersistentDataType.DOUBLE, template.getMaxMana() * (1.0 + (qMaxMana - 0.5) * 2 * variance));
        PDC.set(meta, Keys.MANA_REGEN, PersistentDataType.DOUBLE, template.getManaRegen() * (1.0 + (qManaRegen - 0.5) * 2 * variance));
        PDC.set(meta, Keys.ATTACK_SPEED, PersistentDataType.DOUBLE, template.getAttackSpeed() * (1.0 + (qAttackSpeed - 0.5) * 2 * variance));
        PDC.set(meta, Keys.ELEMENTAL_DAMAGE, PersistentDataType.DOUBLE, template.getElementalDamage() * (1.0 + (qElementalDamage - 0.5) * 2 * variance));
        PDC.set(meta, Keys.MOVEMENT_SPEED, PersistentDataType.DOUBLE, template.getMovementSpeed() * (1.0 + (qMovementSpeed - 0.5) * 2 * variance));
        PDC.set(meta, Keys.LIFE_STEAL, PersistentDataType.DOUBLE, template.getLifeSteal() * (1.0 + (qLifeSteal - 0.5) * 2 * variance));

        meta.lore(buildLore(template, meta));
        target.setItemMeta(meta);

        material.setAmount(material.getAmount() - 1);
        return true;
    }

}
