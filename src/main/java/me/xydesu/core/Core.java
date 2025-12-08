package me.xydesu.core;

import me.xydesu.core.Command.Commands.item;
import me.xydesu.core.Command.Commands.iteminfo;
import me.xydesu.core.Command.Commands.loop;
import me.xydesu.core.Command.Commands.menu;
import me.xydesu.core.Command.Commands.stats;
import me.xydesu.core.Command.Commands.ap;
import me.xydesu.core.Command.Commands.spawnmob;
import me.xydesu.core.Events.Attack;
import me.xydesu.core.Events.BlockInteractListener;
import me.xydesu.core.Events.ExpListener;
import me.xydesu.core.Events.HealthListener;
import me.xydesu.core.Events.ItemUpdateListener;
import me.xydesu.core.Events.MobDeathListener;
import me.xydesu.core.Events.PlayerDataListener;
import me.xydesu.core.Events.StaminaListener;
import me.xydesu.core.GUI.GUIListener;
import me.xydesu.core.Tasks.*;
import me.xydesu.core.Database.DatabaseManager;
import me.xydesu.core.Utils.ResourcePackManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Core extends JavaPlugin {

    private static Plugin plugin;
    private static DatabaseManager databaseManager;

    List<Player> debug = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        //saveDefaultConfig();

        // Package Resource Pack
        //ResourcePackManager.packageResourcePack(this);

        databaseManager = new DatabaseManager();
        databaseManager.connect(
                "127.0.0.1",
                "3306",
                "minecraft",
                "core",
                "corepassword"
        );

        List.of(
                new Attack(),
                new GUIListener(),
                new ItemUpdateListener(),
                new BlockInteractListener(),
                new HealthListener(),
                new ExpListener(),
                new MobDeathListener(),
                new PlayerDataListener(),
                new StaminaListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        List.of(
                new item(),
                new iteminfo(),
                new stats(),
                new menu(),
                new ap(),
                new spawnmob(),
                new loop()
        ).forEach(commands -> {
            PluginCommand pluginCommand = getCommand(commands.getCommand());
            if (pluginCommand != null) {
                pluginCommand.setExecutor((commandSender, command, s, args) -> {
                    commands.onExecute(commandSender, args);
                    return true;
                });
                pluginCommand.setTabCompleter((commandSender, command, s, args) -> commands.onTabComplete(commandSender, command, s, args));
            }
        });

        //new ActionBarTask().runTaskTimer(this, 0L, 20L);
        
        /*PlayerHudTask hudTask = new PlayerHudTask();
        hudTask.runTaskTimer(this, 0L, 5L); // Update HUD faster (every 0.25s)
        getServer().getPluginManager().registerEvents(hudTask, this);*/
        
        new ManaRegenTask().runTaskTimer(this, 0L, 20L);
        new HealthRegenTask().runTaskTimer(this, 0L, 20L);
        new StaminaTask().runTaskTimer(this, 0L, 4L); // Run every 4 ticks (0.2s)
        new StatUpdateTask().runTaskTimer(this, 0L, 10L);
        new AutoSaveTask().runTaskTimerAsynchronously(this, 6000L, 6000L); // Save every 5 minutes
        //new CustomMobTask().runTaskTimer(this, 0L, 0L);

        // Load data for online players (in case of reload)
        for (Player player : getServer().getOnlinePlayers()) {
            me.xydesu.core.Player.Player customPlayer = me.xydesu.core.Player.Player.get(player);
            databaseManager.loadPlayer(customPlayer);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (databaseManager != null) {
            // Save all online players before disconnecting
            for (Player player : getServer().getOnlinePlayers()) {
                me.xydesu.core.Player.Player customPlayer = me.xydesu.core.Player.Player.get(player);
                databaseManager.savePlayer(customPlayer);
            }
            databaseManager.disconnect();
        }
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
