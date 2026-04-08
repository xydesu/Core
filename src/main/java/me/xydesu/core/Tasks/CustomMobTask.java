package me.xydesu.core.Tasks;

import me.xydesu.core.Mob.CustomMob;
import org.bukkit.scheduler.BukkitRunnable;

public class CustomMobTask extends BukkitRunnable {
    @Override
    public void run() {
        CustomMob.getNameDisplays().forEach((entity, name)-> {
            name.teleport(entity);
        });

    }
}
