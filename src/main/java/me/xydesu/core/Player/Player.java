package me.xydesu.core.Player;

import me.xydesu.core.Item.SetBonusManager;
import me.xydesu.core.Player.Class.ClassManager;
import me.xydesu.core.Utils.Keys;
import me.xydesu.core.Utils.PDC;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

public class Player {

    private static final Map<UUID, Player> players = new HashMap<>();

    public static Player get(org.bukkit.entity.Player player) {
        return get(player.getUniqueId());
    }

    public static Player get(UUID uuid) {
        if (!players.containsKey(uuid)) {
            players.put(uuid, new Player(uuid));
        }
        return players.get(uuid);
    }

    private final UUID uuid;

    public UUID getUuid() {
        return uuid;
    }

    private ClassManager playerClass;

    // Attributes
    private double maxHealth;
    private double currentHealth;
    private double strength;
    private double defense;
    private double critChance;
    private double critDamage;
    private double maxMana;
    private double currentMana;
    private double manaRegen;
    private double healthRegen;
    private double maxStamina;
    private double currentStamina;

    // Allocated Attributes (from Attribute Points)
    private int attributePoints;
    private double allocatedStrength;
    private double allocatedAgility;
    private double allocatedIntelligence;
    private double allocatedVitality;
    private double allocatedDexterity;
    
    private double tempAttackSpeedBonus = 0;

    public double getTempAttackSpeedBonus() { return tempAttackSpeedBonus; }
    public void setTempAttackSpeedBonus(double bonus) { this.tempAttackSpeedBonus = bonus; }

    // Level System
    private int level;
    private double exp;
    private static final int MAX_LEVEL = 100;

    public Player(UUID uuid) {
        this.uuid = uuid;
        // Default values
        this.maxHealth = 100;
        this.currentHealth = this.maxHealth;
        this.strength = 0;
        this.defense = 0;
        this.critChance = 0.0;
        this.critDamage = 1.5; // 150%
        this.maxMana = 100;
        this.currentMana = 100;
        this.manaRegen = 0; // 0% bonus by default
        this.healthRegen = 0; // 0% bonus by default
        this.maxStamina = 20;
        this.currentStamina = 20;

        this.attributePoints = 0;
        this.allocatedStrength = 0;
        this.allocatedAgility = 0;
        this.allocatedIntelligence = 0;
        this.allocatedVitality = 0;
        this.allocatedDexterity = 0;

        this.level = 1;
        this.exp = 0;
        this.playerClass = null;
    }

    public org.bukkit.entity.Player getBukkitPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void setPlayerClass(ClassManager playerClass) {
        this.playerClass = playerClass;
    }

    public void clearPlayerClass() {
        this.playerClass = null;
    }

    public void setPlayerClass(String className) {
        ClassManager cm = ClassManager.get(className);
        if (cm != null) {
            this.playerClass = cm;
        }
    }

    public ClassManager getPlayerClass() {
        return this.playerClass;
    }

    // Getters and Setters
    public double getMaxHealth() { return maxHealth; }
    public void setMaxHealth(double maxHealth) { this.maxHealth = maxHealth; }

    public double getCurrentHealth() { return currentHealth; }
    public void setCurrentHealth(double currentHealth) { 
        this.currentHealth = Math.min(currentHealth, maxHealth); 
        if (this.currentHealth < 0) this.currentHealth = 0;
    }

    public double getHealthRegen() { return healthRegen; }
    public void setHealthRegen(double healthRegen) { this.healthRegen = healthRegen; }

    public double getStrength() { return strength; }
    public void setStrength(double strength) { this.strength = strength; }

    public double getDefense() { return defense; }
    public void setDefense(double defense) { this.defense = defense; }

    public double getCritChance() { return critChance; }
    public void setCritChance(double critChance) { this.critChance = critChance; }

    public double getCritDamage() { return critDamage; }
    public void setCritDamage(double critDamage) { this.critDamage = critDamage; }

    public double getMaxMana() { return maxMana; }
    public void setMaxMana(double maxMana) { this.maxMana = maxMana; }

    public double getCurrentMana() { return currentMana; }
    public void setCurrentMana(double currentMana) { 
        this.currentMana = Math.min(currentMana, maxMana); 
    }

    public double getManaRegen() { return manaRegen; }
    public void setManaRegen(double manaRegen) { this.manaRegen = manaRegen; }

    public double getMaxStamina() { return maxStamina; }
    public void setMaxStamina(double maxStamina) { this.maxStamina = maxStamina; }

    public double getCurrentStamina() { return currentStamina; }
    public void setCurrentStamina(double currentStamina) {
        this.currentStamina = Math.min(currentStamina, maxStamina);
        if (this.currentStamina < 0) this.currentStamina = 0;
        
        // Update vanilla hunger bar
        org.bukkit.entity.Player p = getBukkitPlayer();
        if (p != null) {
            // Scale stamina to 20 hunger points
            int foodLevel = (int) ((this.currentStamina / this.maxStamina) * 20);
            p.setFoodLevel(foodLevel);
        }
    }

    public int getAttributePoints() { return attributePoints; }
    public void setAttributePoints(int attributePoints) { this.attributePoints = attributePoints; }

    public double getAllocatedStrength() { return allocatedStrength; }
    public void setAllocatedStrength(double allocatedStrength) { this.allocatedStrength = allocatedStrength; }

    public double getAllocatedAgility() { return allocatedAgility; }
    public void setAllocatedAgility(double allocatedAgility) { this.allocatedAgility = allocatedAgility; }

    public double getAllocatedIntelligence() { return allocatedIntelligence; }
    public void setAllocatedIntelligence(double allocatedIntelligence) { this.allocatedIntelligence = allocatedIntelligence; }

    public double getAllocatedVitality() { return allocatedVitality; }
    public void setAllocatedVitality(double allocatedVitality) { this.allocatedVitality = allocatedVitality; }

    public double getAllocatedDexterity() { return allocatedDexterity; }
    public void setAllocatedDexterity(double allocatedDexterity) { this.allocatedDexterity = allocatedDexterity; }

    // Level System Methods
    public int getLevel() { return level; }
    public void setLevel(int level) {
        this.level = Math.min(level, MAX_LEVEL);
        updateVanillaExp();
    }

    public double getExp() { return exp; }
    public void setExp(double exp) {
        this.exp = exp;
        checkLevelUp();
        updateVanillaExp();
    }

    public void addExp(double amount) {
        if (level >= MAX_LEVEL) return;
        this.exp += amount;
        checkLevelUp();
        updateVanillaExp();
    }

    private void checkLevelUp() {
        double required = getRequiredExp();
        while (this.exp >= required) {
            this.exp -= required;
            this.level++;
            this.attributePoints += 5; // Add 5 attribute points per level
            // TODO: Add level up effects/rewards here
            if (this.level >= MAX_LEVEL) {
                this.level = MAX_LEVEL;
                this.exp = 0;
                break;
            }
            required = getRequiredExp();
        }
    }

    public double getRequiredExp() {
        return getRequiredExp(this.level);
    }

    public double getRequiredExp(int level) {
        // Formula: 100 * Level + 20 * Level^2
        // This makes the curve quadratic instead of linear
        return 100 * level + 20 * level * level;
    }

    public void updateVanillaExp() {
        org.bukkit.entity.Player p = getBukkitPlayer();
        if (p != null) {
            p.setLevel(this.level);
            
            if (level >= MAX_LEVEL) {
                p.setExp(0);
            } else {
                p.setExp((float) Math.min(1.0, this.exp / getRequiredExp()));
            }
        }
    }

    public void updateVanillaHealth() {
        org.bukkit.entity.Player p = getBukkitPlayer();
        if (p == null) return;
        if (maxHealth <= 0) return;
        double vanillaHealth = (currentHealth / maxHealth) * 20;
        if (vanillaHealth < 1 && currentHealth > 0) vanillaHealth = 1;
        if (vanillaHealth > 20) vanillaHealth = 20;
        if (vanillaHealth < 0) vanillaHealth = 0;
        p.setHealth(vanillaHealth);
    }

    public void setStat(String stat, double value) {
        switch (stat.toLowerCase()) {
            case "strength": this.strength = value; break;
            case "defense": this.defense = value; break;
            case "crit_chance": this.critChance = value; break;
            case "crit_damage": this.critDamage = value; break;
            case "max_health": this.maxHealth = value; break;
            case "health_regen": this.healthRegen = value; break;
            case "max_mana": this.maxMana = value; break;
            case "mana_regen": this.manaRegen = value; break;
        }
    }

    public void updateStats() {
        org.bukkit.entity.Player p = Bukkit.getPlayer(uuid);
        if (p == null) return;

        // Base Stats
        double strength = 0;
        double defense = 0;
        double critChance = 0;
        double critDamage = 1.5;
        double maxHealth = 100;
        double healthRegen = 0;
        double maxMana = 100;
        double manaRegen = 0;
        double maxStamina = 20;
        double walkSpeed = 0.2;
        double attackSpeed = 0;

        // Attribute Bonuses
        maxHealth += allocatedVitality * 5;
        healthRegen += allocatedVitality * 0.1;
        maxStamina += allocatedVitality * 1; // Vitality increases stamina
        
        strength += allocatedStrength;
        
        walkSpeed += allocatedAgility * 0.0005;
        
        critChance += allocatedDexterity * 0.001;
        critDamage += allocatedDexterity * 0.005;
        
        maxMana += allocatedIntelligence * 5;
        manaRegen += allocatedIntelligence * 0.1;

        List<ItemStack> items = new ArrayList<>();
        if (p.getInventory().getItemInMainHand() != null) items.add(p.getInventory().getItemInMainHand());
        if (p.getInventory().getHelmet() != null) items.add(p.getInventory().getHelmet());
        if (p.getInventory().getChestplate() != null) items.add(p.getInventory().getChestplate());
        if (p.getInventory().getLeggings() != null) items.add(p.getInventory().getLeggings());
        if (p.getInventory().getBoots() != null) items.add(p.getInventory().getBoots());

        for (ItemStack item : items) {
            if (item == null || !item.hasItemMeta()) continue;

            // Check Level Requirement
            int requiredLevel = PDC.get(item, Keys.REQUIRED_LEVEL, PersistentDataType.INTEGER, 0);
            if (this.level < requiredLevel) {
                p.getWorld().dropItemNaturally(p.getLocation(), item);
                // Remove from the correct inventory slot
                if (item.equals(p.getInventory().getItemInMainHand())) {
                    p.getInventory().setItemInMainHand(null);
                } else if (item.equals(p.getInventory().getHelmet())) {
                    p.getInventory().setHelmet(null);
                } else if (item.equals(p.getInventory().getChestplate())) {
                    p.getInventory().setChestplate(null);
                } else if (item.equals(p.getInventory().getLeggings())) {
                    p.getInventory().setLeggings(null);
                } else if (item.equals(p.getInventory().getBoots())) {
                    p.getInventory().setBoots(null);
                }
                p.sendMessage("§c你的等級不足以使用此物品！ (需要等級: " + requiredLevel + ")");
                continue;
            }

            strength += PDC.get(item, Keys.STRENGTH, PersistentDataType.DOUBLE, 0.0);
            defense += PDC.get(item, Keys.DEFENSE, PersistentDataType.DOUBLE, 0.0);
            critChance += PDC.get(item, Keys.CRIT_CHANCE, PersistentDataType.DOUBLE, 0.0);
            critDamage += PDC.get(item, Keys.CRIT_DAMAGE, PersistentDataType.DOUBLE, 0.0);
            maxHealth += PDC.get(item, Keys.MAX_HEALTH, PersistentDataType.DOUBLE, 0.0);
            healthRegen += PDC.get(item, Keys.HEALTH_REGEN, PersistentDataType.DOUBLE, 0.0);
            maxMana += PDC.get(item, Keys.MAX_MANA, PersistentDataType.DOUBLE, 0.0);
            manaRegen += PDC.get(item, Keys.MANA_REGEN, PersistentDataType.DOUBLE, 0.0);
            attackSpeed += PDC.get(item, Keys.ATTACK_SPEED, PersistentDataType.DOUBLE, 0.0);
            // Assuming we might add stamina stats to items later, but for now just base + vitality
        }

        // Set Bonus detection: count equipped pieces per set ID
        List<ItemStack> armorSlots = new ArrayList<>();
        if (p.getInventory().getHelmet() != null) armorSlots.add(p.getInventory().getHelmet());
        if (p.getInventory().getChestplate() != null) armorSlots.add(p.getInventory().getChestplate());
        if (p.getInventory().getLeggings() != null) armorSlots.add(p.getInventory().getLeggings());
        if (p.getInventory().getBoots() != null) armorSlots.add(p.getInventory().getBoots());

        Map<String, Integer> setPieceCounts = new HashMap<>();
        for (ItemStack armorItem : armorSlots) {
            if (armorItem == null || !armorItem.hasItemMeta()) continue;
            String setId = PDC.get(armorItem, Keys.SET_ID, PersistentDataType.STRING, null);
            if (setId != null) {
                setPieceCounts.merge(setId, 1, Integer::sum);
            }
        }

        for (Map.Entry<String, Integer> entry : setPieceCounts.entrySet()) {
            for (SetBonusManager.SetBonus bonus : SetBonusManager.getApplicableBonuses(entry.getKey(), entry.getValue())) {
                strength += bonus.strength;
                defense += bonus.defense;
                critChance += bonus.critChance;
                critDamage += bonus.critDamage;
                maxHealth += bonus.maxHealth;
                healthRegen += bonus.healthRegen;
                maxMana += bonus.maxMana;
                manaRegen += bonus.manaRegen;
            }
        }

        setStat("strength", strength);
        setStat("defense", defense);
        setStat("crit_chance", critChance);
        setStat("crit_damage", critDamage);
        setStat("max_health", maxHealth);
        setStat("health_regen", healthRegen);
        setStat("max_mana", maxMana);
        setStat("mana_regen", manaRegen);
        
        this.maxStamina = maxStamina;
        // Apply vanilla attributes
        // We keep vanilla MAX_HEALTH at 20 to work with the visual health scaling in HealthListener
        if (p.getAttribute(Attribute.MAX_HEALTH) != null)
            p.getAttribute(Attribute.MAX_HEALTH).setBaseValue(20);
        
        // Defense reduces damage
        if (p.getAttribute(Attribute.ARMOR) != null)
            p.getAttribute(Attribute.ARMOR).setBaseValue(defense);
            
        // Walk Speed
        if (walkSpeed > 1.0) walkSpeed = 1.0;
        p.setWalkSpeed((float) walkSpeed);

        // Attack Speed
        if (attackSpeed <= 0) attackSpeed = 4.0;
        attackSpeed *= (1.0 + tempAttackSpeedBonus);
        
        if (p.getAttribute(Attribute.ATTACK_SPEED) != null)
            p.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(attackSpeed);
    }
}
