package me.xydesu.core.Tasks;

import me.xydesu.core.Player.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Updates the tab-list header/footer and each player's list-name every 10 ticks (2×/s).
 *
 * Animation layers:
 *   • Header title   – 6-frame gradient shimmer (§6/§e/§f cycling), 1 s per frame
 *   • Header tip     – 5 rotating gameplay tips, one shown every 5 s
 *   • Footer border  – alternates §6◈ ↔ §e◆ every 1 s
 *   • Player name    – class-coloured bold level badge + white name
 *
 * Sorting:
 *   Players are sorted level-descending then name-ascending by assigning each player
 *   to a Team named "s{000-099}" on the viewer's personal scoreboard.  The Minecraft
 *   client renders tab-list entries in alphabetical team-name order, so "s000" (highest
 *   level) always appears above "s099" (level 1).
 */
public class TablistTask extends BukkitRunnable {

    private static final int MAX_LEVEL = 100;

    // Server title cycles through 6 colour combinations (1 frame per second = every 2 ticks at 10-tick interval)
    private static final String[] TITLE = {
        "§6§l✦ §e§lCoreRPG §6§l✦",
        "§e§l✧ §f§lCoreRPG §e§l✧",
        "§f§l✦ §6§lCoreRPG §f§l✦",
        "§e§l✧ §6§lCoreRPG §e§l✧",
        "§6§l✦ §f§lCoreRPG §6§l✦",
        "§e§l✧ §e§lCoreRPG §e§l✧",
    };

    // Rotating tips – one shown every 5 s (every 10 ticks at 10-tick interval)
    private static final String[] TIPS = {
        "§7§o  使用 §e/menu §7§o開啟主選單  ",
        "§7§o  輸入 §e/class §7§o選擇你的職業  ",
        "§7§o  擊倒怪物以獲得 §e經驗值  ",
        "§7§o  使用 §e/party §7§o與朋友組隊冒險  ",
        "§7§o  強化裝備以提升戰鬥力  ",
    };

    private int tick = 0;

    @Override
    public void run() {
        tick++;
        // Title frame changes every 2 ticks = 1 s
        int titleFrame = (tick / 2) % TITLE.length;
        // Tip changes every 10 ticks = 5 s
        int tipFrame   = (tick / 10) % TIPS.length;
        // Border character alternates every 2 ticks = 1 s
        boolean altBorder = (tick / 2) % 2 == 0;

        // Sort players: level descending, then name ascending
        List<org.bukkit.entity.Player> sorted = new ArrayList<>(Bukkit.getOnlinePlayers());
        sorted.sort(Comparator
                .comparingInt((org.bukkit.entity.Player p) -> {
                    Player cp = Player.get(p);
                    return -(cp != null ? cp.getLevel() : 0);
                })
                .thenComparing(org.bukkit.entity.Player::getName));

        for (org.bukkit.entity.Player p : sorted) {
            Player cp = Player.get(p);
            if (cp == null) continue;
            updateTablist(p, cp, titleFrame, tipFrame, altBorder, sorted.size());
            updateSortTeams(p, sorted);
        }
    }

    private void updateTablist(org.bukkit.entity.Player p, Player cp,
                               int titleFrame, int tipFrame, boolean altBorder, int online) {
        String classColor = classColor(cp);
        String className  = cp.getPlayerClass() != null ? cp.getPlayerClass().classDisplay() : "§7無職業";
        int    lv  = cp.getLevel();
        int    hp  = (int) cp.getCurrentHealth(), mhp = (int) cp.getMaxHealth();
        int    mp  = (int) cp.getCurrentMana(),   mmp = (int) cp.getMaxMana();

        String hpColor = mhp > 0 && (double) hp / mhp >= 0.6 ? "§a"
                : mhp > 0 && (double) hp / mhp >= 0.3 ? "§6" : "§c";

        // Animated footer border
        String bChar  = altBorder ? "§6◈" : "§e◆";
        String border = bChar + "§7──────────────────────" + bChar;

        // ── Header ────────────────────────────────────────────────────────────
        Component header = legacy(
                "\n" +
                "  " + TITLE[titleFrame] + "  \n" +
                TIPS[tipFrame] + "\n"
        );

        // ── Footer ────────────────────────────────────────────────────────────
        Component footer = legacy(
                "\n" +
                border + "\n" +
                "§f" + p.getName()
                        + " §8│ " + classColor + className
                        + " §8│ §aLv.§f" + lv + "\n" +
                hpColor + "❤ §f" + hp + "§7/§f" + mhp
                        + "  §b✎ §f" + mp + "§7/§f" + mmp + "\n" +
                "§7在線玩家: §f" + online + "\n" +
                border + "\n"
        );

        p.sendPlayerListHeaderAndFooter(header, footer);

        // ── Tab list player name: class-coloured bold level badge ─────────────
        p.playerListName(legacy(classColor + "§l[" + lv + "]§r §f" + p.getName()));
    }

    /**
     * Assigns every online player to a sort team on {@code viewer}'s personal scoreboard.
     * Team names follow the pattern {@code "s000"} (highest level) → {@code "s099"} (level 1),
     * causing the Minecraft client to render the tab list in descending-level order.
     * Players at the same level are sorted alphabetically by name within their shared team.
     */
    private static void updateSortTeams(org.bukkit.entity.Player viewer,
                                        List<org.bukkit.entity.Player> allPlayers) {
        Scoreboard sb = ScoreboardTask.getBoard(viewer.getUniqueId());
        if (sb == null) return;

        for (org.bukkit.entity.Player target : allPlayers) {
            Player cp = Player.get(target);
            if (cp == null) continue;

            // Pad to 3 digits so lexicographic order equals numeric order;
            // subtract from MAX_LEVEL so higher levels sort first.
            String teamName = "s" + String.format("%03d", MAX_LEVEL - cp.getLevel());
            Team team = sb.getTeam(teamName);
            if (team == null) team = sb.registerNewTeam(teamName);

            // Only mutate membership when the player is in the wrong team
            Team current = sb.getEntryTeam(target.getName());
            if (current == null || !current.getName().equals(teamName)) {
                if (current != null) current.removeEntry(target.getName());
                team.addEntry(target.getName());
            }
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static String classColor(Player cp) {
        if (cp.getPlayerClass() == null) return "§7";
        return switch (cp.getPlayerClass().className()) {
            case "BladeMaster"    -> "§6";
            case "Berserker"      -> "§c";
            case "ShadowAssassin" -> "§5";
            case "WindRanger"     -> "§a";
            case "Arbalestier"    -> "§e";
            case "Elementalist"   -> "§b";
            case "SoulReaper"     -> "§4";
            default               -> "§f";
        };
    }

    private static Component legacy(String s) {
        return LegacyComponentSerializer.legacySection().deserialize(s);
    }
}

