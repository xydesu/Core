package me.xydesu.core.Command.Commands;

import me.xydesu.core.Command.Command;
import me.xydesu.core.GUI.GUIs.MainMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class menu extends Command {
    @Override
    public String getCommand() {
        return "menu";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            new MainMenu(player).open(player);
        } else {
            sender.sendMessage("只有玩家可以使用此指令。");
        }
    }

    @Override
    public String getDescription() {
        return "開啟主選單";
    }

    @Override
    public String getUsage() {
        return "/menu";
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
