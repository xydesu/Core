package me.xydesu.core.Command.Commands;

import me.xydesu.core.Command.Command;
import me.xydesu.core.Player.Class.ClassManager;
import me.xydesu.core.Player.Player;
import org.bukkit.command.CommandSender;

import java.util.List;

public class _class extends Command {
    @Override
    public String getCommand() {
        return "class";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Player player = Player.get(((org.bukkit.entity.Player) sender).getUniqueId());
        if(args.length==0){
            sender.sendMessage("/class set <className>");
            sender.sendMessage("/class unset");
            sender.sendMessage("/class list");
            sender.sendMessage("/class info <className>");
            sender.sendMessage("/class help");
            if(player.getPlayerClass()==null)
                sender.sendMessage("You have not selected a class yet.");
            else {
                sender.sendMessage("Your Class:"+player.getPlayerClass().classDisplay());
            }
        } else {
            if (args[0].equalsIgnoreCase("set")) {
                // set class
                player.setPlayerClass(args[1]);
                sender.sendMessage("Class set to " + args[1]);
            } else if (args[0].equalsIgnoreCase("unset")) {
                // unset class
                player.setPlayerClass();
                sender.sendMessage("Class unset.");
            } else if (args[0].equalsIgnoreCase("list")) {
                // list classes
                sender.sendMessage("Available Classes:");
                for(ClassManager classed : ClassManager.getAllClasses()){
                    sender.sendMessage("§6" + classed.classDisplay() + " §f(" + classed.className() + ")");
                }
            } else if (args[0].equalsIgnoreCase("info")) {
                // class info
                ClassManager classed = ClassManager.get(args[1]);
                if(classed==null){
                    sender.sendMessage("Class not found.");
                    return;
                }
                sender.sendMessage("§6" + classed.classDisplay() + " §f(" + classed.className() + "):");
                sender.sendMessage("Description:");
                for(String desc : classed.classDescription()){;
                    sender.sendMessage("   §7" + desc);
                }
            } else if (args[0].equalsIgnoreCase("help")) {
                // help
                sender.sendMessage("/class set <className>");
                sender.sendMessage("/class list");
                sender.sendMessage("/class info <className>");
                sender.sendMessage("/class help");
            } else {
                sender.sendMessage("Unknown subcommand. Use /class for a list of commands.");
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if(args.length==1){
            return List.of("set", "unset", "list", "info", "help");
        } else if(args.length==2 && args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("info")){
            return ClassManager.getAllClasses().stream().map(ClassManager::className).toList();
        }
        return super.onTabComplete(sender, command, alias, args);
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
