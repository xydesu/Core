package me.xydesu.core.GUI.GUIs;

import me.xydesu.core.GUI.GUI;
import me.xydesu.core.Item.Item;
import me.xydesu.core.Utils.Keys;
import me.xydesu.core.Utils.PDC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Reforging GUI.
 *
 * <p>Layout (9×3 chest):
 * <pre>
 *  [ ][ ][ ][ ][ ][ ][ ][ ][ ]   row 0
 *  [ ][ G][ ][ ][ R][ ][ G][ ]   row 1
 *  [ ][ ][ ][ ][ ][ ][ ][ ][ ]   row 2
 * </pre>
 * Slot 10 – item to reforge (open)<br>
 * Slot 13 – "Reforge" button (always present)<br>
 * Slot 16 – material (open)<br>
 * Slot 22 – back button<br>
 * All other slots are blocked by filler glass.
 */
public class ReforgeMenu extends GUI {

    private static final int SLOT_ITEM = 10;
    private static final int SLOT_REFORGE = 13;
    private static final int SLOT_MATERIAL = 16;
    private static final int SLOT_BACK = 22;

    private static final Set<Integer> OPEN_SLOTS = Set.of(SLOT_ITEM, SLOT_MATERIAL);

    public ReforgeMenu(Player player) {
        super(27, Component.text("重鑄台"));
        buildLayout();
    }

    private void buildLayout() {
        // Filler glass for all slots
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.displayName(Component.empty());
        filler.setItemMeta(fillerMeta);

        for (int i = 0; i < getInventory().getSize(); i++) {
            if (!OPEN_SLOTS.contains(i)) {
                getInventory().setItem(i, filler);
            }
        }

        // Reforge button
        List<Component> reforgeLore = new ArrayList<>();
        reforgeLore.add(Component.text("將物品放入左槽，材料放入右槽。", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        reforgeLore.add(Component.empty());
        reforgeLore.add(Component.text("消耗 1 個材料重新隨機物品品質。", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
        reforgeLore.add(Component.empty());
        reforgeLore.add(Component.text("點擊重鑄！", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        ItemStack reforgeBtn = createButton(Material.ANVIL, "⚒ 重鑄", reforgeLore, NamedTextColor.GOLD);
        PDC.set(reforgeBtn, Keys.ID, PersistentDataType.STRING, "REFORGE_BTN");
        getInventory().setItem(SLOT_REFORGE, reforgeBtn);

        // Item slot label
        ItemStack itemSlotLabel = createButton(Material.ITEM_FRAME, "◈ 物品槽", List.of(
                Component.text("將要重鑄的物品放入此槽", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
        ), NamedTextColor.AQUA);
        PDC.set(itemSlotLabel, Keys.ID, PersistentDataType.STRING, "LABEL");
        getInventory().setItem(SLOT_ITEM - 9, itemSlotLabel);

        // Material slot label
        ItemStack matSlotLabel = createButton(Material.ITEM_FRAME, "◈ 材料槽", List.of(
                Component.text("將材料放入此槽 (消耗 1 個)", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
        ), NamedTextColor.GREEN);
        PDC.set(matSlotLabel, Keys.ID, PersistentDataType.STRING, "LABEL");
        getInventory().setItem(SLOT_MATERIAL - 9, matSlotLabel);

        // Back button
        List<Component> backLore = new ArrayList<>();
        backLore.add(Component.text("點擊返回主選單", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        ItemStack backBtn = createButton(Material.ARROW, "返回", backLore, NamedTextColor.RED);
        PDC.set(backBtn, Keys.ID, PersistentDataType.STRING, "BACK");
        getInventory().setItem(SLOT_BACK, backBtn);
    }

    private ItemStack createButton(Material mat, String name, List<Component> lore, NamedTextColor color) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name, color).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        // Allow players to freely place/take items in the open slots
        if (OPEN_SLOTS.contains(event.getRawSlot())) {
            return; // do not cancel – let Bukkit handle the normal inventory interaction
        }

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        String id = PDC.get(clicked, Keys.ID, PersistentDataType.STRING, null);
        if (id == null) return;

        Player player = (Player) event.getWhoClicked();

        switch (id) {
            case "BACK" -> {
                returnItems(player);
                new MainMenu(player).open(player);
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            }
            case "REFORGE_BTN" -> performReforge(player);
        }
    }

    @Override
    public void onDrag(InventoryDragEvent event) {
        // Allow drags only if ALL affected raw slots are open input slots
        for (int rawSlot : event.getRawSlots()) {
            if (!OPEN_SLOTS.contains(rawSlot)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        returnItems((Player) event.getPlayer());
    }

    private void performReforge(Player player) {
        ItemStack targetItem = getInventory().getItem(SLOT_ITEM);
        ItemStack material = getInventory().getItem(SLOT_MATERIAL);

        if (targetItem == null || targetItem.getType().isAir()) {
            player.sendMessage(Component.text("請先將物品放入左側物品槽！", NamedTextColor.RED));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }
        if (material == null || material.getType().isAir()) {
            player.sendMessage(Component.text("請先將材料放入右側材料槽！", NamedTextColor.RED));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        boolean success = Item.reforge(targetItem, material);
        if (success) {
            if (material.getAmount() <= 0) {
                getInventory().setItem(SLOT_MATERIAL, null);
            }
            player.sendMessage(Component.text("✔ 重鑄成功！物品品質已重新隨機。", NamedTextColor.GREEN));
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
        } else {
            player.sendMessage(Component.text("✘ 此物品無法被重鑄。", NamedTextColor.RED));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        }
    }

    /** Returns any items left in the input slots to the player's inventory (or drops them). */
    private void returnItems(Player player) {
        for (int slot : OPEN_SLOTS) {
            ItemStack item = getInventory().getItem(slot);
            if (item != null && !item.getType().isAir()) {
                getInventory().setItem(slot, null);
                player.getInventory().addItem(item).values()
                        .forEach(leftover -> player.getWorld().dropItemNaturally(player.getLocation(), leftover));
            }
        }
    }
}
