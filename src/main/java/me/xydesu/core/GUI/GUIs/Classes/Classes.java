package me.xydesu.core.GUI.GUIs.Classes;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.bukkit.Sound;

import me.xydesu.core.GUI.GUI;
import me.xydesu.core.Player.Class.ClassManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;

public class Classes extends GUI {

    private final java.util.Map<Integer, ClassManager> slotMap = new java.util.HashMap<>();
    private final me.xydesu.core.Player.Player player;

    public Classes(me.xydesu.core.Player.Player player) {
        super(54, Component.text("職業列表"));
        this.player = player;
        setup();
    }

    public Classes(Player player) {
        this(me.xydesu.core.Player.Player.get(player));
    }

    private void setup() {
        // Filler
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.displayName(Component.empty());
        filler.setItemMeta(fillerMeta);

        for (int i = 0; i < 54; i++) {
            getInventory().setItem(i, filler);
        }

        // Title Item (Row 0, Center)
        ItemStack info = new ItemStack(Material.NETHER_STAR);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.displayName(Component.text("職業選擇", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
        info.setItemMeta(infoMeta);
        getInventory().setItem(4, info);

        // Calculate slots for centering
        // Row 2 (Index 18-26)
        // 7 Classes -> Start at Index 1 (Slot 19)
        int[] slots = { 19, 20, 21, 22, 23, 24, 25 };

        List<ClassManager> classes = ClassManager.getAllClasses();
        int classIndex = 0;

        for (int slot : slots) {
            if (classIndex >= classes.size())
                break;

            ClassManager cm = classes.get(classIndex);
            slotMap.put(slot, cm);

            ItemStack item = new ItemStack(getMaterialForClass(cm));
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(cm.classDisplay(), NamedTextColor.GOLD)
                    .decoration(TextDecoration.ITALIC, false));

            List<Component> lore = new ArrayList<>();
            lore.add(Component.text(cm.className(), NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.empty());
            lore.add(Component.text("定位: ", NamedTextColor.WHITE)
                    .append(Component.text(cm.getRole(), NamedTextColor.YELLOW))
                    .decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("風格: ", NamedTextColor.WHITE)
                    .append(Component.text(cm.getFightingStyle(), NamedTextColor.YELLOW))
                    .decoration(TextDecoration.ITALIC, false));
            lore.add(Component.empty());
            lore.add(Component.text("點擊查看詳情", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
            meta.lore(lore);
            item.setItemMeta(meta);

            getInventory().setItem(slot, item);
            classIndex++;
        }

        // Close Button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.displayName(Component.text("關閉選單", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        close.setItemMeta(closeMeta);
        getInventory().setItem(49, close);
    }

    private Material getMaterialForClass(ClassManager cm) {
        me.xydesu.core.Item.ToolType type = cm.getWeaponType();
        switch (type) {
            case SWORD -> {
                return Material.IRON_SWORD;
            }
            case GREATSWORD -> {
                return Material.NETHERITE_SWORD;
            }
            case DAGGER -> {
                return Material.IRON_SWORD;
            }
            case BOW -> {
                return Material.BOW;
            }
            case CROSSBOW -> {
                return Material.CROSSBOW;
            }
            case STAFF -> {
                return Material.BLAZE_ROD;
            }
            case SCYTHE -> {
                return Material.IRON_HOE;
            }
            case AXE -> {
                return Material.IRON_AXE;
            }
            default -> {
                return Material.BOOK;
            }
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getCurrentItem() == null)
            return;

        int slot = event.getSlot();
        if (slotMap.containsKey(slot)) {
            ClassManager target = slotMap.get(slot);
            new ClassInfoGUI(player, target).open(player.getBukkitPlayer());
            player.getBukkitPlayer().playSound(player.getBukkitPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        } else if (slot == 49) {
            player.getBukkitPlayer().closeInventory();
        }
    }
}
