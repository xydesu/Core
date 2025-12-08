package me.xydesu.core.Command.Commands;

import me.xydesu.core.Command.Command;
import me.xydesu.core.GUI.GUIs.SandBox;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import me.xydesu.core.Item.Item;

import java.util.ArrayList;
import java.util.List;

public class item extends Command {
    @Override
    public String getCommand() {
        return "item";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if(sender instanceof Player player){
            if (args.length > 0) {
                ItemStack item = Item.createItem(args[0]);
                if (item != null) {
                    player.getInventory().addItem(item);
                    player.sendMessage("Given " + args[0]);
                } else {
                    player.sendMessage("Item " + args[0] + " not found.");
                }
            } else {
                new SandBox().open(player);
            }
        }
    }

    @Override
    public String getDescription() {
        return "Get Item";
    }

    @Override
    public String getUsage() {
        return "/item <item>";
    }

    public List<String> getTabCompletions(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        List<String> items = new ArrayList<>();
        for (Item registeredItem : Item.registeredItems) {
            items.add(registeredItem.getID());
        }
        return items;
    }


}
