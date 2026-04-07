package me.xydesu.core.Command.Commands;

import me.xydesu.core.Command.Command;
import me.xydesu.core.Utils.Keys;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemInfoCommand extends Command {
    @Override
    public String getCommand() {
        return "iteminfo";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item != null && item.getType() != Material.AIR) {
                ItemMeta meta = item.getItemMeta();
                player.sendMessage("==========Meta Data==========");
                if (meta.hasDisplayName()) {
                    player.sendMessage(Component.text("Item: ").append(meta.displayName()));
                } else {
                    player.sendMessage("Item: " + item.getType().name());
                }

                if (meta.hasLore()) {
                    player.sendMessage("Lore:");
                    for (Component component : meta.lore()) {
                        player.sendMessage(component);
                    }
                }

                player.sendMessage("==========Item Data==========");
                player.sendMessage("ID: " + meta.getPersistentDataContainer().get(Keys.ID, PersistentDataType.STRING));
                player.sendMessage(
                        "Rarity: " + meta.getPersistentDataContainer().get(Keys.RARITY, PersistentDataType.STRING));
                player.sendMessage(
                        "Damage: " + meta.getPersistentDataContainer().get(Keys.DAMAGE, PersistentDataType.DOUBLE));
                player.sendMessage(
                        "Range: " + meta.getPersistentDataContainer().get(Keys.RANGE, PersistentDataType.DOUBLE));
                player.sendMessage(
                        "AOE: " + meta.getPersistentDataContainer().get(Keys.AOE, PersistentDataType.BOOLEAN));

                String ability = meta.getPersistentDataContainer().get(Keys.ABILITY, PersistentDataType.STRING);
                if (ability != null) {
                    player.sendMessage("Ability: " + ability);
                }
            } else {
                player.sendMessage("You are not holding an item.");
            }
        }
    }

    @Override
    public String getDescription() {
        return "Displays information about the item in hand";
    }

    @Override
    public String getUsage() {
        return "/iteminfo";
    }
}
