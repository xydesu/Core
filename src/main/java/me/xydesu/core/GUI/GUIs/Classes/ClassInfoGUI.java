package me.xydesu.core.GUI.GUIs.Classes;

import me.xydesu.core.GUI.GUI;
import me.xydesu.core.Player.Class.ClassManager;
import me.xydesu.core.Player.Player;
import me.xydesu.core.Item.ToolType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ClassInfoGUI extends GUI {

    private final ClassManager targetClass;
    private final Player player;

    public ClassInfoGUI(Player player, ClassManager targetClass) {
        super(27, Component.text("職業詳情: " + targetClass.classDisplay()));
        this.player = player;
        this.targetClass = targetClass;
        setup();
    }

    private void setup() {
        // Center: Class Info
        ItemStack icon = new ItemStack(getMaterialForClass(targetClass));
        ItemMeta meta = icon.getItemMeta();
        meta.displayName(Component.text(targetClass.classDisplay(), NamedTextColor.GOLD)
                .decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(targetClass.className(), NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.empty());
        lore.add(Component.text("定位: ", NamedTextColor.WHITE)
                .append(Component.text(targetClass.getRole(), NamedTextColor.YELLOW))
                .decoration(TextDecoration.ITALIC, false));
        lore.add(Component.text("武器: ", NamedTextColor.WHITE)
                .append(Component.text(targetClass.getWeaponType().getName(), NamedTextColor.YELLOW))
                .decoration(TextDecoration.ITALIC, false));
        lore.add(Component.text("風格: ", NamedTextColor.WHITE)
                .append(Component.text(targetClass.getFightingStyle(), NamedTextColor.YELLOW))
                .decoration(TextDecoration.ITALIC, false));
        lore.add(Component.empty());
        for (String line : targetClass.classDescription()) {
            lore.add(Component.text(line, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        }
        meta.lore(lore);
        icon.setItemMeta(meta);
        getInventory().setItem(4, icon);

        // Skills (Slots 10, 11, 12, 13) -> Spaced out: 10, 12, 14, 16
        getInventory().setItem(10, createSkillItem(1));
        getInventory().setItem(12, createSkillItem(2));
        getInventory().setItem(14, createSkillItem(3));
        getInventory().setItem(16, createSkillItem(4)); // Ult

        // Select Button (Slot 22)
        ItemStack select = new ItemStack(Material.LIME_CONCRETE);
        ItemMeta selectMeta = select.getItemMeta();
        selectMeta.displayName(Component.text("選擇此職業", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        select.setItemMeta(selectMeta);
        getInventory().setItem(22, select);

        // Back Button (Slot 18)
        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.displayName(Component.text("返回列表", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        back.setItemMeta(backMeta);
        getInventory().setItem(18, back);
    }

    private ItemStack createSkillItem(int index) {
        ItemStack item = new ItemStack(index == 4 ? Material.NETHER_STAR : Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();
        String name = "";
        List<String> desc = new ArrayList<>();
        double mana = 0;
        double cd = 0;

        switch (index) {
            case 1 -> {
                name = targetClass.getSkill1Name();
                desc = targetClass.getSkill1Description();
                mana = targetClass.getSkill1ManaCost();
                cd = targetClass.getSkill1Cooldown();
            }
            case 2 -> {
                name = targetClass.getSkill2Name();
                desc = targetClass.getSkill2Description();
                mana = targetClass.getSkill2ManaCost();
                cd = targetClass.getSkill2Cooldown();
            }
            case 3 -> {
                name = targetClass.getSkill3Name();
                desc = targetClass.getSkill3Description();
                mana = targetClass.getSkill3ManaCost();
                cd = targetClass.getSkill3Cooldown();
            }
            case 4 -> {
                name = targetClass.getUltimateName();
                desc = targetClass.getUltimateDescription();
                mana = targetClass.getUltimateManaCost();
                cd = targetClass.getUltimateCooldown();
            }
        }

        meta.displayName(Component.text(index == 4 ? "[終極技能] " + name : "[技能 " + index + "] " + name,
                index == 4 ? NamedTextColor.GOLD : NamedTextColor.AQUA)
                .decoration(TextDecoration.ITALIC, false));

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("魔力消耗: " + (int) mana, NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.text("冷卻時間: " + (int) cd + "s", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC,
                false));
        lore.add(Component.empty());
        for (String s : desc) {
            lore.add(Component.text(s, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        }
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private Material getMaterialForClass(ClassManager cm) {
        ToolType type = cm.getWeaponType();
        switch (type) {
            case SWORD -> {
                return Material.IRON_SWORD;
            }
            case GREATSWORD -> {
                return Material.NETHERITE_SWORD;
            }
            case DAGGER -> {
                return Material.IRON_SWORD;
            } // No dagger in vanilla, use sword
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
        if (slot == 22) {
            // Select Class
            player.setPlayerClass(targetClass);
            player.getBukkitPlayer().sendMessage(Component.text("§a已選擇職業: " + targetClass.classDisplay()));
            player.getBukkitPlayer().playSound(player.getBukkitPlayer().getLocation(),
                    Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
            player.getBukkitPlayer().closeInventory();
        } else if (slot == 18) {
            // Back
            new Classes(player).open(player.getBukkitPlayer());
        }
    }
}
