package me.xydesu.core.Item;

import me.xydesu.core.Player.Class.ClassManager;
import me.xydesu.core.Utils.Keys;
import me.xydesu.core.Utils.PDC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds the item lore {@link Component} lists used by {@link Item#getItem()},
 * {@link Item#getDisplayItem()}, and {@link Item#updateLore(org.bukkit.inventory.ItemStack)}.
 */
public class ItemLoreBuilder {

    private ItemLoreBuilder() {}

    /**
     * Builds the lore for a fully-instantiated item (shows actual rolled stat
     * values and a quality grade).
     */
    static List<Component> buildLore(Item item, ItemMeta meta) {
        List<Component> lore = new ArrayList<>();
        MiniMessage mm = MiniMessage.miniMessage();
        Component separator = mm.deserialize("<gray>═════════════════════");

        String rarityName = item.getRarity().getLoreTag();
        String typeName = item.getToolType().getLoreTag();
        lore.add(mm.deserialize("<italic:false>" + rarityName));
        lore.add(mm.deserialize("<italic:false>" + typeName));
        lore.add(separator);

        double variance = item.getStatVariance();
        List<Component> stats = new ArrayList<>();
        List<Double> qualities = new ArrayList<>();

        if (item.getDamage() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_DAMAGE, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            stats.add(formatStat(mm, "攻擊力", item.getDamage() * (1.0 + (q - 0.5) * 2 * variance), q, false));
        }
        if (item.getStrength() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_STRENGTH, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            stats.add(formatStat(mm, "力量", item.getStrength() * (1.0 + (q - 0.5) * 2 * variance), q, false));
        }
        if (item.getDefense() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_DEFENSE, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            stats.add(formatStat(mm, "防禦", item.getDefense() * (1.0 + (q - 0.5) * 2 * variance), q, false));
        }
        if (item.getCritChance() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_CRIT_CHANCE, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            stats.add(formatStat(mm, "暴擊率", item.getCritChance() * (1.0 + (q - 0.5) * 2 * variance) * 100, q, true));
        }
        if (item.getCritDamage() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_CRIT_DAMAGE, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            stats.add(formatStat(mm, "暴擊傷害", item.getCritDamage() * (1.0 + (q - 0.5) * 2 * variance) * 100, q, true));
        }
        if (item.getMaxHealth() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_MAX_HEALTH, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            stats.add(formatStat(mm, "生命值", item.getMaxHealth() * (1.0 + (q - 0.5) * 2 * variance), q, false));
        }
        if (item.getHealthRegen() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_HEALTH_REGEN, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            stats.add(formatStat(mm, "生命回復", item.getHealthRegen() * (1.0 + (q - 0.5) * 2 * variance), q, true));
        }
        if (item.getMaxMana() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_MAX_MANA, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            stats.add(formatStat(mm, "魔力", item.getMaxMana() * (1.0 + (q - 0.5) * 2 * variance), q, false));
        }
        if (item.getManaRegen() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_MANA_REGEN, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            stats.add(formatStat(mm, "魔力回復", item.getManaRegen() * (1.0 + (q - 0.5) * 2 * variance), q, true));
        }
        if (item.getAttackSpeed() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_ATTACK_SPEED, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            stats.add(formatStat(mm, "攻擊速度", item.getAttackSpeed() * (1.0 + (q - 0.5) * 2 * variance), q, true));
        }
        if (item.getElementalDamage() != 0) {
            double q = meta != null
                    ? PDC.get(meta, Keys.QUALITY_ELEMENTAL_DAMAGE, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            stats.add(formatStat(mm, "元素傷害", item.getElementalDamage() * (1.0 + (q - 0.5) * 2 * variance), q, false));
        }
        if (item.getMovementSpeed() != 0) {
            double q = meta != null
                    ? PDC.get(meta, Keys.QUALITY_MOVEMENT_SPEED, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            stats.add(formatStat(mm, "移動速度", item.getMovementSpeed() * (1.0 + (q - 0.5) * 2 * variance), q, true));
        }
        if (item.getLifeSteal() != 0) {
            double q = meta != null ? PDC.get(meta, Keys.QUALITY_LIFE_STEAL, PersistentDataType.DOUBLE, 0.5) : 0.5;
            qualities.add(q);
            stats.add(formatStat(mm, "生命竊取", item.getLifeSteal() * (1.0 + (q - 0.5) * 2 * variance), q, true));
        }
        if (item.getRange() != 0) {
            stats.add(mm.deserialize(
                    "<italic:false><gray>攻擊距離：<white>+" + String.format(java.util.Locale.US, "%.1f", item.getRange())));
        }

        if (!stats.isEmpty()) {
            lore.addAll(stats);
            lore.add(Component.empty());
        }

        // Calculate average quality for the grade display
        double avgQuality = 0.5;
        if (!qualities.isEmpty()) {
            double sum = 0;
            for (Double d : qualities) sum += d;
            avgQuality = sum / qualities.size();
        } else if (meta != null) {
            Double q = PDC.get(meta, Keys.QUALITY, PersistentDataType.DOUBLE);
            if (q != null) avgQuality = q;
        }

        int qualityScore = (int) (avgQuality * 100);
        String grade;
        if (qualityScore >= 100)      grade = "<rainbow>SSS</rainbow>";
        else if (qualityScore >= 95)  grade = "<gold>SS</gold>";
        else if (qualityScore >= 90)  grade = "<yellow>S</yellow>";
        else if (qualityScore >= 80)  grade = "<dark_purple>A</dark_purple>";
        else if (qualityScore >= 60)  grade = "<blue>B</blue>";
        else if (qualityScore >= 40)  grade = "<green>C</green>";
        else                          grade = "<gray>D</gray>";

        lore.add(mm.deserialize("<italic:false><gray>品質：<reset>" + grade + " <dark_gray>(" + qualityScore + "%)"));

        if (item.getRequiredLevel() > 0) {
            lore.add(mm.deserialize("<italic:false><red>需求等級：" + item.getRequiredLevel()));
        }

        lore.add(separator);

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

    /**
     * Builds the lore for a display-only copy of the item (shows stat ranges
     * instead of actual rolled values, no quality grade).
     */
    static List<Component> buildDisplayLore(Item item) {
        List<Component> lore = new ArrayList<>();
        MiniMessage mm = MiniMessage.miniMessage();
        Component separator = mm.deserialize("<gray>═════════════════════");

        lore.add(mm.deserialize("<italic:false>" + item.getRarity().getLoreTag()));
        lore.add(mm.deserialize("<italic:false>" + item.getToolType().getLoreTag()));
        lore.add(separator);

        double variance = item.getStatVariance();
        double minMult = 1.0 - variance;
        double maxMult = 1.0 + variance;

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
        if (item.getLifeSteal() != 0)
            stats.add(mm.deserialize("<italic:false><gray>生命竊取：<gold>"
                    + String.format(java.util.Locale.US, "%.1f", item.getLifeSteal() * minMult) + "-"
                    + String.format(java.util.Locale.US, "%.1f", item.getLifeSteal() * maxMult) + "%"));
        // Range is a fixed stat with no variance
        if (item.getRange() != 0)
            stats.add(mm.deserialize(
                    "<italic:false><gray>攻擊距離：<gold>+" + String.format(java.util.Locale.US, "%.1f", item.getRange())));

        if (!stats.isEmpty()) {
            lore.addAll(stats);
            lore.add(Component.empty());
        }

        lore.add(mm.deserialize("<italic:false><gray>品質浮動：<yellow>+/- " + (int) (variance * 100) + "%"));

        if (item.getRequiredLevel() > 0) {
            lore.add(mm.deserialize("<italic:false><red>需求等級：" + item.getRequiredLevel()));
        }

        lore.add(separator);

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
        if (quality >= 1.0)          valueColor = "<light_purple>";
        else if (quality >= 0.95)    valueColor = "<gold>";
        else if (quality >= 0.90)    valueColor = "<red>";
        else if (quality >= 0.80)    valueColor = "<dark_purple>";
        else if (quality >= 0.60)    valueColor = "<blue>";
        else if (quality >= 0.40)    valueColor = "<green>";
        else                         valueColor = "<gray>";

        String valStr = "+" + String.format(java.util.Locale.US, "%.1f", value);
        if (isPercent) valStr += "%";
        return mm.deserialize("<italic:false><gray>" + name + "：" + valueColor + valStr);
    }

    static List<String> wrapText(String text, int maxCharWidth) {
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
}
