package me.xydesu.core.Tasks;

import me.xydesu.core.Core;
import me.xydesu.core.Player.Player;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Runs on the main thread to snapshot online players, then dispatches
 * each player's DB save to an async thread.
 */
public class AutoSaveTask extends BukkitRunnable {

    @Override
    public void run() {
        // This runs on the main thread (registered with runTaskTimer).
        // Snapshot the player list safely here, then do I/O async.
        List<Player> snapshot = new ArrayList<>();
        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
            snapshot.add(Player.get(p));
        }

        Bukkit.getScheduler().runTaskAsynchronously(Core.getPlugin(), () -> {
            for (Player customPlayer : snapshot) {
                Core.getDatabaseManager().savePlayer(customPlayer);
            }
        });
    }
}
