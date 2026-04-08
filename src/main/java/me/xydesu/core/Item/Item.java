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

// Lore building is handled by ItemLoreBuilder.
// Quality generation and persistence is handled by ItemQualities.

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

            ItemQualities qualities = ItemQualities.generate();
            qualities.saveToPDC(meta);
            qualities.applyStats(meta, this);

            PDC.set(meta, Keys.ID, PersistentDataType.STRING, getID());
            PDC.set(meta, Keys.REQUIRED_LEVEL, PersistentDataType.INTEGER, getRequiredLevel());
            PDC.set(meta, Keys.RANGE, PersistentDataType.DOUBLE, getRange());
            PDC.set(meta, Keys.AOE, PersistentDataType.BOOLEAN, hasAOE());
            PDC.set(meta, Keys.RARITY, PersistentDataType.STRING, getRarity().name());
            if (getSetID() != null) {
                PDC.set(meta, Keys.SET_ID, PersistentDataType.STRING, getSetID());
            }

            meta.lore(ItemLoreBuilder.buildLore(this, meta));
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

            meta.lore(ItemLoreBuilder.buildDisplayLore(this));
            meta.setUnbreakable(isUnbreakable());

            item.setItemMeta(meta);
        }
        return item;
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
            lore = ItemLoreBuilder.buildLore(customItem, item.getItemMeta());
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

        ItemQualities qualities = ItemQualities.generate();
        qualities.saveToPDC(meta);
        qualities.applyStats(meta, template);

        meta.lore(ItemLoreBuilder.buildLore(template, meta));
        target.setItemMeta(meta);

        material.setAmount(material.getAmount() - 1);
        return true;
    }

}
