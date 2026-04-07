package me.xydesu.core.Command.Commands;

import me.xydesu.core.Command.Command;
import me.xydesu.core.Player.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ApCommand extends Command {
    @Override
    public String getCommand() {
        return "ap";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Component.text("你沒有權限使用此指令。", NamedTextColor.RED));
            return;
        }

        if (args.length < 3) {
            sender.sendMessage(Component.text("用法: /ap give <玩家> <數量>", NamedTextColor.RED));
            return;
        }

        if (args[0].equalsIgnoreCase("give")) {
            org.bukkit.entity.Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(Component.text("找不到玩家。", NamedTextColor.RED));
                return;
            }

            try {
                int amount = Integer.parseInt(args[2]);
                Player customPlayer = Player.get(target);
                customPlayer.setAttributePoints(customPlayer.getAttributePoints() + amount);
                sender.sendMessage(Component.text("給予 " + amount + " 點屬性點數給 " + target.getName(), NamedTextColor.GREEN));
                target.sendMessage(Component.text("你獲得了 " + amount + " 點屬性點數！", NamedTextColor.GREEN));
            } catch (NumberFormatException e) {
                sender.sendMessage(Component.text("無效的數量。", NamedTextColor.RED));
            }
        }
    }

    @Override
    public String getDescription() {
        return "Manage Attribute Points";
    }

    @Override
    public String getUsage() {
        return "/ap give <player> <amount>";
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("give");
        } else if (args.length == 2) {
            return null; // Player names
        }
        return completions;
    }
}
