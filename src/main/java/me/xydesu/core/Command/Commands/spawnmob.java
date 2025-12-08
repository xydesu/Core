package me.xydesu.core.Command.Commands;

import me.xydesu.core.Command.Command;
import me.xydesu.core.Mob.CustomMob;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class spawnmob extends Command {

    @Override
    public String getCommand() {
        return "spawnmob";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("只有玩家可以使用此指令。");
            return;
        }

        if (args.length < 1) {
            player.sendMessage("用法: /spawnmob <類型> [等級] [血量] [名稱]");
            return;
        }

        String typeStr = args[0].toUpperCase();

        // Handle Presets
        CustomMob customMob = CustomMob.get(typeStr);
        if (customMob != null) {
            customMob.spawn(player.getLocation());
            player.sendMessage("已生成 " + customMob.getName());
            return;
        }

        if (args.length < 3) {
            player.sendMessage("用法: /spawnmob <類型> <等級> <血量> [名稱]");
            return;
        }

        int level;
        double health;
        String name = typeStr;

        try {
            level = Integer.parseInt(args[1]);
            health = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage("等級和血量必須是數字。");
            return;
        }

        if (args.length >= 4) {
            name = args[3];
        }

        EntityType type;
        try {
            type = EntityType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            player.sendMessage("無效的實體類型: " + typeStr);
            return;
        }

        if (!type.isAlive()) {
            player.sendMessage("該實體類型不是生物。");
            return;
        }

        LivingEntity entity = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), type);
        CustomMob.setLevel(entity, level);
        CustomMob.setMaxHealth(entity, health);
        CustomMob.setCurrentHealth(entity, health);
        CustomMob.setMobName(entity, name);
        CustomMob.setCustomMob(entity, true);

        player.sendMessage("已生成 " + name + " (Lv." + level + ", HP: " + health + ")");
    }

    @Override
    public String getDescription() {
        return "生成自訂怪物";
    }

    @Override
    public String getUsage() {
        return "/spawnmob <類型> <等級> <血量> [名稱]";
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (CustomMob mob : CustomMob.registeredMobs) {
                completions.add(mob.getID());
            }
            for (EntityType type : EntityType.values()) {
                if (type.isAlive() && type.isSpawnable()) {
                    completions.add(type.name().toLowerCase());
                }
            }
        }
        return completions;
    }
}
