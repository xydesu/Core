package me.xydesu.core.Tasks;

import me.xydesu.core.Core;
import me.xydesu.core.Player.Player;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSaveTask extends BukkitRunnable {

    @Override
    public void run() {
        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
            Player customPlayer = Player.get(p);
            Core.getDatabaseManager().savePlayer(customPlayer);
        }
    }
}
