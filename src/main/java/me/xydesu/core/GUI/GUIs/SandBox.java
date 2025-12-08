package me.xydesu.core.GUI.GUIs;

import me.xydesu.core.GUI.GUI;
import me.xydesu.core.Item.Item;
import me.xydesu.core.Utils.Keys;
import me.xydesu.core.Utils.PDC;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class SandBox extends GUI {

    ItemStack glasspane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
    ItemMeta meta = glasspane.getItemMeta();
    ItemStack quit = new ItemStack(Material.BARRIER);
    ItemMeta meta2 = quit.getItemMeta();


    public SandBox() {
        super(54, Component.text("SandBox"));

        for (Item item : Item.registeredItems) {
            getInventory().addItem(item.getDisplayItem());
        }


        meta.displayName(Component.text(""));
        meta.setHideTooltip(true);
        glasspane.setItemMeta(meta);
        meta2.setHideTooltip(true);
        quit.setItemMeta(meta2);

        for( int i=53 ; i>53-9 ; i-- ) {
            getInventory().setItem(i, glasspane);
        }
        getInventory().setItem(49, quit);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getCurrentItem() != null) {
            if(event.getCurrentItem().equals(glasspane)) return ;
            if (event.getWhoClicked() instanceof Player player) {
                if(event.getClickedInventory().equals(player.getInventory())) return ;
                String id = PDC.get(event.getCurrentItem(), Keys.ID, PersistentDataType.STRING);
                if (id != null) {
                    ItemStack realItem = Item.createItem(id);
                    if (realItem != null) {
                        player.getInventory().addItem(realItem);
                    }
                }
            }
        }
    }


}
