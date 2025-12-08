package me.xydesu.core.Command.Commands;

import me.xydesu.core.Command.Command;
import me.xydesu.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class loop extends Command {
    @Override
    public String getCommand() {
        return "loop";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§cUsage: /loop <times> <delay_ticks> <command>");
            return;
        }

        int times;
        long delay;

        try {
            times = Integer.parseInt(args[0]);
            delay = Long.parseLong(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cTimes and delay must be valid numbers!");
            return;
        }

        // Join the rest of the arguments to get the command
        String commandToRun = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        
        // Remove leading slash if present, as dispatchCommand doesn't need it usually, 
        // but we want to support both inputs.
        final String finalCommand = commandToRun.startsWith("/") ? commandToRun.substring(1) : commandToRun;

        sender.sendMessage("§aStarting loop: " + times + " times, every " + delay + " ticks.");

        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (count >= times) {
                    this.cancel();
                    sender.sendMessage("§aLoop finished.");
                    return;
                }

                // Execute the command
                Bukkit.dispatchCommand(sender, finalCommand);

                count++;
            }
        }.runTaskTimer(Core.getPlugin(), 0L, delay);
    }

    @Override
    public String getDescription() {
        return "Loop a command multiple times with delay";
    }

    @Override
    public String getUsage() {
        return "/loop <times> <delay> <command>";
    }
}
