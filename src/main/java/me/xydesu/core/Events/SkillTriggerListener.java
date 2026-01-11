package me.xydesu.core.Events;

import me.xydesu.core.Item.ToolType;
import me.xydesu.core.Player.Class.ClassManager;
import me.xydesu.core.Player.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class SkillTriggerListener implements Listener {

    // Map to store player's recent actions: UUID -> List of ClickType
    private final Map<UUID, List<ClickType>> playerActions = new HashMap<>();
    // Map to store the last action timestamp to expire combos
    private final Map<UUID, Long> lastActionTime = new HashMap<>();

    private static final long COMBO_TIMEOUT_MS = 1500; // 1.5 seconds to complete combo

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL)
            return;

        ClickType clickType = (event.getAction() == Action.LEFT_CLICK_AIR
                || event.getAction() == Action.LEFT_CLICK_BLOCK)
                        ? ClickType.LEFT
                        : ClickType.RIGHT;

        handleInteraction(event.getPlayer(), clickType);
    }

    @EventHandler
    public void onEntityDamageByEntity(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof org.bukkit.entity.Player) {
            handleInteraction((org.bukkit.entity.Player) event.getDamager(), ClickType.LEFT);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerActions.remove(event.getPlayer().getUniqueId());
        lastActionTime.remove(event.getPlayer().getUniqueId());
    }

    private void handleInteraction(org.bukkit.entity.Player bukkitPlayer, ClickType clickType) {
        Player player = Player.get(bukkitPlayer);
        ClassManager playerClass = player.getPlayerClass();
        if (playerClass == null)
            return;

        if (!me.xydesu.core.Item.Item.canUse(player, bukkitPlayer.getInventory().getItemInMainHand())) {
            if (clickType == ClickType.RIGHT) {
                player.getBukkitPlayer().sendMessage(net.kyori.adventure.text.Component.text("§c無法使用此武器 (職業不符)"));
            }
            return;
        }

        // Check if item is a weapon (New Requirement)
        me.xydesu.core.Item.ToolType toolType = me.xydesu.core.Item.Item
                .getToolType(bukkitPlayer.getInventory().getItemInMainHand());
        if (toolType == null || !toolType.isWeapon())
            return;

        UUID uuid = bukkitPlayer.getUniqueId();
        long currentTime = System.currentTimeMillis();

        if (lastActionTime.containsKey(uuid)) {
            if (currentTime - lastActionTime.get(uuid) > COMBO_TIMEOUT_MS) {
                playerActions.remove(uuid);
            }
        }

        playerActions.putIfAbsent(uuid, new ArrayList<>());
        List<ClickType> actions = playerActions.get(uuid); // Changed to store ClickType directly

        if (actions.isEmpty()) {
            boolean isRanged = toolType == ToolType.BOW || toolType == ToolType.CROSSBOW;
            boolean isValidStart = isRanged ? clickType == ClickType.LEFT : clickType == ClickType.RIGHT;

            // If invalid start, do not start a combo
            if (!isValidStart) {
                return;
            }
        }

        actions.add(clickType);
        lastActionTime.put(uuid, currentTime);

        // Show Combo Title
        StringBuilder comboStr = new StringBuilder();
        for (ClickType type : actions) {
            String color = type == ClickType.RIGHT ? "§e" : "§f"; // Yellow for Right, White for Left
            String text = type == ClickType.RIGHT ? "R" : "L";
            if (comboStr.length() > 0)
                comboStr.append(" §7- ");
            comboStr.append(color).append(text);
        }
        player.getBukkitPlayer().showTitle(net.kyori.adventure.title.Title.title(
                net.kyori.adventure.text.Component.empty(),
                net.kyori.adventure.text.Component.text(comboStr.toString())));

        if (actions.size() > 3) {
            actions.remove(0);
        }

        if (actions.size() == 3) {
            if (checkCombo(player, actions)) {
                playerActions.remove(uuid);
                lastActionTime.remove(uuid);
            }
        }
    }

    private boolean checkCombo(Player player, List<ClickType> clicks) {
        ToolType weaponType = player.getPlayerClass().getWeaponType();
        boolean isRanged = weaponType == ToolType.BOW || weaponType == ToolType.CROSSBOW;

        if (clicks.size() != 3)
            return false;

        ClickType c1 = clicks.get(0);
        ClickType c2 = clicks.get(1);
        ClickType c3 = clicks.get(2);

        if (isRanged) {
            if (c1 == ClickType.LEFT && c2 == ClickType.RIGHT && c3 == ClickType.LEFT) {
                player.getPlayerClass().castSkill1(player);
                return true;
            }
            if (c1 == ClickType.LEFT && c2 == ClickType.LEFT && c3 == ClickType.RIGHT) {
                player.getPlayerClass().castSkill2(player);
                return true;
            }
            if (c1 == ClickType.LEFT && c2 == ClickType.RIGHT && c3 == ClickType.RIGHT) {
                player.getPlayerClass().castSkill3(player);
                return true;
            }
            if (c1 == ClickType.LEFT && c2 == ClickType.LEFT && c3 == ClickType.LEFT) {
                player.getPlayerClass().castUltimate(player);
                return true;
            }
        } else {
            if (c1 == ClickType.RIGHT && c2 == ClickType.LEFT && c3 == ClickType.RIGHT) {
                player.getPlayerClass().castSkill1(player);
                return true;
            }
            if (c1 == ClickType.RIGHT && c2 == ClickType.RIGHT && c3 == ClickType.LEFT) {
                player.getPlayerClass().castSkill2(player);
                return true;
            }
            if (c1 == ClickType.RIGHT && c2 == ClickType.LEFT && c3 == ClickType.LEFT) {
                player.getPlayerClass().castSkill3(player);
                return true;
            }
            if (c1 == ClickType.RIGHT && c2 == ClickType.RIGHT && c3 == ClickType.RIGHT) {
                player.getPlayerClass().castUltimate(player);
                return true;
            }
        }
        return false;
    }

    private enum ClickType {
        LEFT, RIGHT
    }
}
