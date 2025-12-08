package me.xydesu.core.Command;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {

    public abstract String getCommand();

    public abstract void onExecute(CommandSender sender, String[] args);

    public abstract String getDescription();

    public abstract String getUsage();

    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {

        return getTabCompletions(sender, command, alias, args);
    }

    protected List<String> getTabCompletions(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        return new ArrayList<>();
    }


}
