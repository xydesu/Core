package me.xydesu.core.Command.Commands;

import me.xydesu.core.Command.Command;
import me.xydesu.core.Player.Player;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class StatsCommand extends Command {
    @Override
    public String getCommand() {
        return "stats";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("用法: /stats <屬性> <數值> [玩家]");
            return;
        }

        String attribute = args[0].toLowerCase();
        double value;
        try {
            value = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("無效的數字: " + args[1]);
            return;
        }

        org.bukkit.entity.Player targetBukkitPlayer;
        if (args.length > 2) {
            targetBukkitPlayer = Bukkit.getPlayer(args[2]);
            if (targetBukkitPlayer == null) {
                sender.sendMessage("找不到玩家: " + args[2]);
                return;
            }
        } else if (sender instanceof org.bukkit.entity.Player) {
            targetBukkitPlayer = (org.bukkit.entity.Player) sender;
        } else {
            sender.sendMessage("控制台必須指定玩家。");
            return;
        }

        Player customPlayer = Player.get(targetBukkitPlayer);

        switch (attribute) {
            case "strength":
                customPlayer.setAllocatedStrength(value);
                sender.sendMessage("已將 " + targetBukkitPlayer.getName() + " 的力量設定為 " + value);
                break;
            case "agility":
                customPlayer.setAllocatedAgility(value);
                sender.sendMessage("已將 " + targetBukkitPlayer.getName() + " 的敏捷設定為 " + value);
                break;
            case "intelligence":
                customPlayer.setAllocatedIntelligence(value);
                sender.sendMessage("已將 " + targetBukkitPlayer.getName() + " 的智力設定為 " + value);
                break;
            case "vitality":
                customPlayer.setAllocatedVitality(value);
                sender.sendMessage("已將 " + targetBukkitPlayer.getName() + " 的體力設定為 " + value);
                break;
            case "dexterity":
                customPlayer.setAllocatedDexterity(value);
                sender.sendMessage("已將 " + targetBukkitPlayer.getName() + " 的靈巧設定為 " + value);
                break;
            case "points":
                customPlayer.setAttributePoints((int) value);
                sender.sendMessage("已將 " + targetBukkitPlayer.getName() + " 的屬性點設定為 " + (int) value);
                break;
            case "currentmana":
                customPlayer.setCurrentMana(value);
                sender.sendMessage("已將 " + targetBukkitPlayer.getName() + " 的當前魔力設定為 " + value);
                break;
            case "level":
                customPlayer.setLevel((int) value);
                sender.sendMessage("已將 " + targetBukkitPlayer.getName() + " 的等級設定為 " + (int) value);
                break;
            case "exp":
                customPlayer.setExp(value);
                sender.sendMessage("已將 " + targetBukkitPlayer.getName() + " 的經驗值設定為 " + value);
                break;
            default:
                sender.sendMessage("未知屬性: " + attribute);
                sender.sendMessage("有效屬性: strength, agility, intelligence, vitality, dexterity, points, currentmana, level, exp");
                break;
        }
    }

    @Override
    public String getDescription() {
        return "設定玩家屬性";
    }

    @Override
    public String getUsage() {
        return "/stats <屬性> <數值> [玩家]";
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("strength");
            completions.add("agility");
            completions.add("intelligence");
            completions.add("vitality");
            completions.add("dexterity");
            completions.add("points");
            completions.add("currentmana");
            completions.add("level");
            completions.add("exp");
        }
        return completions;
    }
}
