package me.xydesu.core.Events;

import com.google.common.collect.Multimap;
import me.xydesu.core.GUI.GUI;
import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Utils.Keys;
import me.xydesu.core.Utils.PDC;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemUpdateListener implements Listener {

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            updateItem(event.getItem().getItemStack());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        for (ItemStack item : event.getPlayer().getInventory().getContents()) {
            updateItem(item);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof GUI) {
            return;
        }
        updateItem(event.getCurrentItem());
        updateItem(event.getCursor());
    }

    private void updateItem(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir())
            return;

        if (PDC.has(itemStack, Keys.ID, PersistentDataType.STRING))
            return;

        System.out.println("[Debug] Checking item: " + itemStack.getType());

        for (Item item : Item.registeredItems) {
            if (item.getMaterial() == itemStack.getType()) {
                System.out.println("[Debug] Found registered custom item: " + item.getID());
                ItemStack customItem = item.getItem();
                itemStack.setItemMeta(customItem.getItemMeta());
                return;
            }
        }

        updateVanillaItem(itemStack);
    }

    private void updateVanillaItem(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> modifiers = itemStack.getType()
                .getDefaultAttributeModifiers(EquipmentSlot.HAND);

        double damage = 0;
        boolean hasDamage = false;

        if (modifiers != null && modifiers.containsKey(Attribute.ATTACK_DAMAGE)) {
            damage = 1;
            for (AttributeModifier modifier : modifiers.get(Attribute.ATTACK_DAMAGE)) {
                damage += modifier.getAmount();
            }
            hasDamage = true;
            System.out.println("[Debug] Vanilla item " + itemStack.getType() + " damage: " + damage);
        }

        Rarity rarity = getVanillaRarity(itemStack.getType());
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return;

        String chineseName = me.xydesu.core.Utils.Translation.get(itemStack.getType());
        Component displayName;
        if (chineseName != null) {
            displayName = Component.text(chineseName);
        } else {
            displayName = Component.translatable(itemStack.getType().getTranslationKey());
        }

        displayName = displayName.color(rarity.getColor())
                .decoration(net.kyori.adventure.text.format.TextDecoration.ITALIC, false);
        meta.displayName(displayName);

        PDC.set(meta, Keys.ID, PersistentDataType.STRING, "VANILLA_" + itemStack.getType().name());
        if (hasDamage) {
            PDC.set(meta, Keys.DAMAGE, PersistentDataType.DOUBLE, damage);
        }
        PDC.set(meta, Keys.RARITY, PersistentDataType.STRING, rarity.name());
        PDC.set(meta, Keys.RANGE, PersistentDataType.DOUBLE, 0.0);

        itemStack.setItemMeta(meta);
        Item.updateLore(itemStack);
    }

    private Rarity getVanillaRarity(org.bukkit.Material material) {
        if (material == org.bukkit.Material.ELYTRA)
            return Rarity.LEGENDARY;
        if (material == org.bukkit.Material.ENCHANTED_GOLDEN_APPLE)
            return Rarity.MYTHIC;
        if (material == org.bukkit.Material.GOLDEN_APPLE)
            return Rarity.RARE;
        if (material == org.bukkit.Material.NETHER_STAR)
            return Rarity.LEGENDARY;
        if (material == org.bukkit.Material.BEACON)
            return Rarity.LEGENDARY;
        if (material == org.bukkit.Material.DRAGON_EGG)
            return Rarity.MYTHIC;

        String name = material.name();
        if (name.startsWith("NETHERITE_"))
            return Rarity.EPIC;
        if (name.startsWith("DIAMOND_") || name.startsWith("TRIDENT"))
            return Rarity.RARE;
        if (name.startsWith("IRON_") || name.startsWith("GOLDEN_"))
            return Rarity.COMMON;
        return Rarity.COMMON;
    }

}
