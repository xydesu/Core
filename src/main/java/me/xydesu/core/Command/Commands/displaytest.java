package me.xydesu.core.Command.Commands;

import me.xydesu.core.Command.Command;
import me.xydesu.core.Core;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

public class displaytest extends Command {
    @Override
    public String getCommand() {
        return "displaytest";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if(sender instanceof Player player) {
            Location location = player.getLocation();
            World world = player.getWorld();

            TextDisplay display = world.spawn(location, TextDisplay.class);

            display.text(Component.text("██████████", NamedTextColor.GREEN));

            display.setAlignment(TextDisplay.TextAlignment.LEFT);

            display.setBillboard(Display.Billboard.CENTER);

            Transformation t = display.getTransformation();
            Vector3f pos = t.getTranslation();

            float x = -0.4f;
            float y = 0.2f;
            float z = -1f;
            float sx = 0.5f;
            float sy = 0.5f;
            float sz = 0.5f;

            if (args.length >= 3) {
                x = Float.parseFloat(args[0]);
                y = Float.parseFloat(args[1]);
                z = Float.parseFloat(args[2]);
            }

            if (args.length >= 6) {
                sx = Float.parseFloat(args[3]);
                sy = Float.parseFloat(args[4]);
                sz = Float.parseFloat(args[5]);
            }

            pos.set(x, y, z);
            t.getScale().set(sx, sy, sz);

            display.setTransformation(new Transformation(
                    pos,
                    t.getLeftRotation(),
                    t.getScale(),
                    t.getRightRotation()
            ));

            player.addPassenger(display);

            Bukkit.getOnlinePlayers().forEach(p -> {
                if(p.equals(player)) return;
                p.hideEntity(Core.getPlugin(), display);
            });

            sender.sendMessage(display.text());
        }

    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getUsage() {
        return "displaytest [x] [y] [z] [sx] [sy] [sz]";
    }
}
