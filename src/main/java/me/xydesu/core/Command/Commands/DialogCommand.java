package me.xydesu.core.Command.Commands;

import me.xydesu.core.Command.Command;
import me.xydesu.core.Dialog.DialogManager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DialogCommand extends Command {
    @Override
    public String getCommand() {
        return "dialog";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            // 如果沒有參數，預設開啟 test
            String dialogId = args.length > 0 ? args[0] : "test";

            DialogManager manager = DialogManager.get(dialogId);
            if (manager != null) {
                sender.sendMessage("嘗試開啟對話框: " + manager.getID());
                player.showDialog(manager.getDialog());
            } else {
                sender.sendMessage("找不到ID為 " + dialogId + " 的對話框。");
            }
        }
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getUsage() {
        return "";
    }
}
