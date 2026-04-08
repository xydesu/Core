package me.xydesu.core.Item;

import java.util.*;

/**
 * Manages armor set definitions and their bonus stats.
 * Each set is identified by a set ID string.  When a player equips enough
 * pieces of a set the corresponding {@link SetBonus} is applied on top of
 * their normal item stats.
 */
public class SetBonusManager {

    /**
     * Immutable snapshot of the bonus stats granted by a set.
     */
    public static class SetBonus {
        public final int requiredPieces;
        public final double strength;
        public final double defense;
        public final double critChance;
        public final double critDamage;
        public final double maxHealth;
        public final double healthRegen;
        public final double maxMana;
        public final double manaRegen;
        public final String displayName;
        /** Human-readable name of the set (without piece count), e.g. "測試套裝". */
        public final String setName;

        public SetBonus(int requiredPieces, double strength, double defense,
                double critChance, double critDamage,
                double maxHealth, double healthRegen,
                double maxMana, double manaRegen,
                String setName, String displayName) {
            this.requiredPieces = requiredPieces;
            this.strength = strength;
            this.defense = defense;
            this.critChance = critChance;
            this.critDamage = critDamage;
            this.maxHealth = maxHealth;
            this.healthRegen = healthRegen;
            this.maxMana = maxMana;
            this.manaRegen = manaRegen;
            this.displayName = displayName;
            this.setName = setName;
        }
    }

    // setId -> ordered list of set bonuses (sorted by requiredPieces ascending)
    private static final Map<String, List<SetBonus>> sets = new HashMap<>();

    static {
        // Test Set: 4 pieces total (Helmet, Chestplate, Leggings, Boots)
        List<SetBonus> testSetBonuses = new ArrayList<>();
        testSetBonuses.add(new SetBonus(2, 5, 5, 0, 0, 0, 0, 0, 0, "測試套裝", "測試套裝 (2件)"));
        testSetBonuses.add(new SetBonus(4, 10, 10, 0.05, 0.1, 50, 1, 0, 0, "測試套裝", "測試套裝 (4件)"));
        sets.put("TEST_SET", testSetBonuses);
    }

    /**
     * Returns all applicable set bonuses for a given set and equipped piece count.
     * Multiple tiers can apply simultaneously (e.g. 2-piece AND 4-piece bonuses).
     *
     * @param setId  the set identifier
     * @param pieces number of equipped pieces
     * @return list of all applicable {@link SetBonus} objects
     */
    public static List<SetBonus> getApplicableBonuses(String setId, int pieces) {
        List<SetBonus> all = sets.get(setId);
        if (all == null) return Collections.emptyList();

        List<SetBonus> result = new ArrayList<>();
        for (SetBonus bonus : all) {
            if (pieces >= bonus.requiredPieces) {
                result.add(bonus);
            }
        }
        return result;
    }

    /**
     * Returns the display name of a set.
     *
     * @param setId the set identifier
     * @return display name or the raw set ID if not found
     */
    public static String getSetDisplayName(String setId) {
        List<SetBonus> bonuses = sets.get(setId);
        if (bonuses != null && !bonuses.isEmpty()) {
            return bonuses.get(bonuses.size() - 1).setName;
        }
        return setId;
    }

    /**
     * Returns the full list of bonus tiers defined for a set (for display
     * purposes, e.g. in item lore).
     */
    public static List<SetBonus> getAllTiers(String setId) {
        return sets.getOrDefault(setId, Collections.emptyList());
    }
}
