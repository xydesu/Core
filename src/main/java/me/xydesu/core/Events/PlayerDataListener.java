package me.xydesu.core.Events;

import me.xydesu.core.Core;
import me.xydesu.core.Player.Party.PartyManager;
import me.xydesu.core.Player.Player;
import me.xydesu.core.Tasks.ScoreboardTask;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDataListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = Player.get(event.getPlayer());

        Bukkit.getScheduler().runTaskAsynchronously(Core.getPlugin(), () -> {
            Core.getDatabaseManager().loadPlayer(player);

            // Return to main thread to apply stats and set vanilla values
            Bukkit.getScheduler().runTask(Core.getPlugin(), () -> {
                player.updateStats();
                player.setCurrentHealth(player.getMaxHealth());
                player.setCurrentStamina(player.getMaxStamina());
                player.setCurrentMana(player.getMaxMana());

                org.bukkit.entity.Player bp = player.getBukkitPlayer();
                if (bp != null) {
                    bp.setHealth(20);
                    bp.setSaturation(20);
                }
            });
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = Player.get(event.getPlayer());
        Bukkit.getScheduler().runTaskAsynchronously(Core.getPlugin(),
                () -> Core.getDatabaseManager().savePlayer(player));
        PartyManager.handleDisconnect(event.getPlayer().getUniqueId());
        ScoreboardTask.remove(event.getPlayer().getUniqueId());
    }
}
