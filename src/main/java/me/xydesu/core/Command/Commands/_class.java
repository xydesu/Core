package me.xydesu.core.Command.Commands;

import me.xydesu.core.Command.Command;
import me.xydesu.core.GUI.GUIs.Classes.Classes;
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
        if (args.length == 0) {
            sender.sendMessage("§eClass Command Help:");
            sender.sendMessage("§f/class set <class> §7- Set your class");
            sender.sendMessage("§f/class unset §7- Reset your class");
            sender.sendMessage("§f/class list §7- List all classes");
            sender.sendMessage("§f/class info <class> §7- View class details");
            sender.sendMessage("§f/class menu §7- Show menu");
            if (player.getPlayerClass() == null)
                sender.sendMessage("§cYou have not selected a class yet.");
            else {
                sender.sendMessage("§aYour Class: §f" + player.getPlayerClass().classDisplay());
            }
        } else {
            if (args[0].equalsIgnoreCase("set")) {
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /class set <class>");
                    return;
                }

                // Case-insensitive lookup
                String targetClass = getClassName(args[1]);
                if (targetClass == null) {
                    sender.sendMessage("§cClass not found: " + args[1]);
                    return;
                }

                player.setPlayerClass(targetClass);
                sender.sendMessage("§aClass set to " + targetClass);

            } else if (args[0].equalsIgnoreCase("unset")) {
                player.setPlayerClass();
                sender.sendMessage("§aClass unset.");

            } else if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage("§eAvailable Classes:");
                for (ClassManager classed : ClassManager.getAllClasses()) {
                    sender.sendMessage("§6" + classed.classDisplay() + " §7(" + classed.className() + ") §8- §f"
                            + classed.getRole());
                }

            } else if (args[0].equalsIgnoreCase("info")) {
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /class info <class>");
                    return;
                }

                String targetClass = getClassName(args[1]);
                if (targetClass == null) {
                    sender.sendMessage("§cClass not found.");
                    return;
                }

                ClassManager classed = ClassManager.get(targetClass);
                sender.sendMessage("§8§m--------------------------------");
                sender.sendMessage("§6" + classed.classDisplay() + " §7(" + classed.className() + ")");
                sender.sendMessage("§eRole: §f" + classed.getRole());
                sender.sendMessage("§eWeapon: §f" + classed.getWeaponType().name());
                sender.sendMessage("§eStyle: §f" + classed.getFightingStyle());
                sender.sendMessage("§eDescription:");
                for (String desc : classed.classDescription()) {
                    sender.sendMessage("  §7" + desc);
                }

                sender.sendMessage(" ");
                sender.sendMessage("§eSkills:");

                // Skill 1
                sender.sendMessage(" §6[Skill 1] §f" + classed.getSkill1Name());
                sender.sendMessage("  §7Mana: §b" + (int) classed.getSkill1ManaCost() + " §7| CD: §b"
                        + (int) classed.getSkill1Cooldown() + "s");
                for (String s : classed.getSkill1Description())
                    sender.sendMessage("  §7" + s);

                // Skill 2
                sender.sendMessage(" §6[Skill 2] §f" + classed.getSkill2Name());
                sender.sendMessage("  §7Mana: §b" + (int) classed.getSkill2ManaCost() + " §7| CD: §b"
                        + (int) classed.getSkill2Cooldown() + "s");
                for (String s : classed.getSkill2Description())
                    sender.sendMessage("  §7" + s);

                // Skill 3
                sender.sendMessage(" §6[Skill 3] §f" + classed.getSkill3Name());
                sender.sendMessage("  §7Mana: §b" + (int) classed.getSkill3ManaCost() + " §7| CD: §b"
                        + (int) classed.getSkill3Cooldown() + "s");
                for (String s : classed.getSkill3Description())
                    sender.sendMessage("  §7" + s);

                // Ultimate
                sender.sendMessage(" §6[Ultimate] §f" + classed.getUltimateName());
                sender.sendMessage("  §7Mana: §b" + (int) classed.getUltimateManaCost() + " §7| CD: §b"
                        + (int) classed.getUltimateCooldown() + "s");
                for (String s : classed.getUltimateDescription())
                    sender.sendMessage("  §7" + s);

                sender.sendMessage("§8§m--------------------------------");

            } else if (args[0].equalsIgnoreCase("help")) {
                onExecute(sender, new String[0]); // Show help
            } else if (args[0].equalsIgnoreCase("menu")) {
                new Classes(player).open(player.getBukkitPlayer());
            } else {
                sender.sendMessage("§cUnknown subcommand. Use /class for help.");
            }
        }
    }

    private String getClassName(String input) {
        for (ClassManager cm : ClassManager.getAllClasses()) {
            if (cm.className().equalsIgnoreCase(input)) {
                return cm.className();
            }
        }
        return null; // Not found
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias,
            String[] args) {
        if (args.length == 1) {
            return List.of("set", "unset", "list", "info", "help");
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("info"))) {
            return ClassManager.getAllClasses().stream().map(ClassManager::className).toList();
        }
        return super.onTabComplete(sender, command, alias, args);
    }

    @Override
    public String getDescription() {
        return "Manage your class";
    }

    @Override
    public String getUsage() {
        return "/class";
    }
}
