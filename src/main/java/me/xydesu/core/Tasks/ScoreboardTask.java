package me.xydesu.core.Tasks;

import me.xydesu.core.Player.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardTask extends BukkitRunnable {

    // 13 unique invisible anchors for 13 sidebar lines (§0–§c)
    private static final String[] ENTRIES = {
        "§0", "§1", "§2", "§3", "§4", "§5", "§6",
        "§7", "§8", "§9", "§a", "§b", "§c"
    };

    // Animated outer border – swaps every 2 seconds
    private static final String[] BORDER = {
        "§6✦§e────────────§6✦",
        "§e✧§6────────────§e✧"
    };

    // Animated inner divider – swaps offset from outer border
    private static final String[] DIVIDER = {
        "§8◈§7──────────§8◈",
        "§7─§8◈§7────────§8◈§7─"
    };

    // Spinning loader shown next to player name (cycles each second)
    private static final String[] SPINNER = {"◐", "◓", "◑", "◒"};

    private int tick = 0;
    private static final Map<UUID, Scoreboard> boards = new HashMap<>();

    @Override
    public void run() {
        tick++;
        // Separator frame changes every 2 seconds; spinner every second
        int frame   = (tick / 2) % 2;
        int spinner = tick % SPINNER.length;

        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
            updateBoard(p, Player.get(p), frame, spinner);
        }
    }

    private void updateBoard(org.bukkit.entity.Player p, Player cp, int frame, int spinner) {
        // ── Create board once per player ──────────────────────────────────────
        if (!boards.containsKey(p.getUniqueId())) {
            Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
            Component title = Component.text("✦ CoreRPG ✦")
                    .color(TextColor.fromHexString("#FFD700"))
                    .decoration(TextDecoration.BOLD, true);
            Objective obj = sb.registerNewObjective("stats", Criteria.DUMMY, title);
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);

            // Pre-register teams with fixed scores – positions never change
            for (int i = 0; i < ENTRIES.length; i++) {
                Team t = sb.registerNewTeam("l" + i);
                t.addEntry(ENTRIES[i]);
                obj.getScore(ENTRIES[i]).setScore(ENTRIES.length - i);
            }
            boards.put(p.getUniqueId(), sb);
            p.setScoreboard(sb);
        }

        Scoreboard board = boards.get(p.getUniqueId());
        Objective obj = board.getObjective("stats");
        if (obj == null) return;

        // ── Gather stats ──────────────────────────────────────────────────────
        String classColor = classColor(cp);
        String className  = cp.getPlayerClass() != null
                ? cp.getPlayerClass().classDisplay() : "未選擇";
        String classIcon  = cp.getPlayerClass() != null
                ? classIcon(cp.getPlayerClass().className()) : "§7✦";

        int lv  = cp.getLevel();
        double hp  = cp.getCurrentHealth(), mhp = cp.getMaxHealth();
        double mp  = cp.getCurrentMana(),   mmp = cp.getMaxMana();
        double st  = cp.getCurrentStamina(), mst = cp.getMaxStamina();
        int    str = (int) cp.getStrength(), def = (int) cp.getDefense();
        double crit     = cp.getCritChance() * 100;
        double critMult = cp.getCritDamage();           // e.g. 1.5
        int    ap   = cp.getAttributePoints();
        double expPct = lv >= 100 ? 100.0
                : cp.getExp() / cp.getRequiredExp() * 100;

        boolean criticalHp = mhp > 0 && hp / mhp < 0.3;

        // ── Dynamic border (pulses red when HP is critical) ───────────────────
        String outerBorder = criticalHp
                ? (tick % 2 == 0
                        ? "§c§l! §4────────────§c§l !"
                        : "§4§l! §c────────────§4§l !")
                : BORDER[frame];
        String innerDivider = DIVIDER[frame];

        // ── HP bar colour transitions green→yellow→red ────────────────────────
        String hpFill = mhp > 0 && hp / mhp >= 0.6 ? "§a"
                : mhp > 0 && hp / mhp >= 0.3 ? "§6" : "§c";

        // ── Build 13 lines (index 0 = top) ───────────────────────────────────
        String[] lines = {
            outerBorder,                                                         //  0 top border (animated)
            classColor + "§l" + SPINNER[spinner] + " §r"
                    + classColor + "§l" + p.getName(),                          //  1 spinning class-coloured name
            "§8[§aLv.§f" + lv + "§8] " + classIcon + " " + classColor + className, //  2 level badge + class
            "§7EXP " + expBar(expPct),                                           //  3 exp bar
            innerDivider,                                                        //  4 inner divider
            hpFill  + "❤ " + miniBar(hp, mhp, 7, hpFill)
                    + " §f" + (int) hp + "§7/§f" + (int) mhp,                  //  5 HP bar + numbers
            "§b✎ "  + miniBar(mp, mmp, 7, "§b")
                    + " §f" + (int) mp + "§7/§f" + (int) mmp,                  //  6 MP bar + numbers
            "§a⚡ " + miniBar(st, mst, 7, "§a")
                    + " §f" + (int) st + "§7/§f" + (int) mst,                  //  7 Stamina bar + numbers
            innerDivider,                                                        //  8 inner divider
            "§6⚔ §f" + str + "  §9♦ §f" + def,                                //  9 STR / DEF
            String.format("§eCrit §f%.0f%% §8│ §6×%.1f", crit, critMult),      // 10 crit stats
            ap > 0 ? "§a§l▶ §r§aAP 可分配: §e§l" + ap : "§7AP: §f0",          // 11 AP (alert if available)
            outerBorder                                                          // 12 bottom border (animated)
        };

        // ── Push text into teams (only prefix changes, scores stay fixed) ─────
        for (int i = 0; i < lines.length; i++) {
            Team team = board.getTeam("l" + i);
            if (team == null) continue;
            team.prefix(legacy(lines[i]));
            team.suffix(Component.empty());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** 10-block EXP progress bar: §a filled, §8 empty. */
    private static String expBar(double pct) {
        int filled = Math.min(10, (int) (pct / 10.0));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) sb.append(i < filled ? "§a█" : "§8░");
        return sb.append(" §f").append(String.format("%.0f", pct)).append("%").toString();
    }

    /** Mini progress bar with configurable width and fill colour. */
    private static String miniBar(double cur, double max, int width, String fillColor) {
        int filled = max > 0
                ? Math.max(0, Math.min(width, (int) Math.round(cur / max * width)))
                : 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < width; i++) sb.append(i < filled ? fillColor + "█" : "§8░");
        return sb.toString();
    }

    /** Returns class-specific chat colour code. */
    private static String classColor(Player cp) {
        if (cp.getPlayerClass() == null) return "§7";
        return switch (cp.getPlayerClass().className()) {
            case "BladeMaster"    -> "§6";  // Gold
            case "Berserker"      -> "§c";  // Red
            case "ShadowAssassin" -> "§5";  // Dark Purple
            case "WindRanger"     -> "§a";  // Green
            case "Arbalestier"    -> "§e";  // Yellow
            case "Elementalist"   -> "§b";  // Aqua
            case "SoulReaper"     -> "§4";  // Dark Red
            default               -> "§f";
        };
    }

    /** Returns a small coloured symbol representing each class. */
    private static String classIcon(String name) {
        return switch (name) {
            case "BladeMaster"    -> "§6⚔";
            case "Berserker"      -> "§c⚡";
            case "ShadowAssassin" -> "§5✦";
            case "WindRanger"     -> "§a»";
            case "Arbalestier"    -> "§e→";
            case "Elementalist"   -> "§b✸";
            case "SoulReaper"     -> "§4☽";
            default               -> "§7✦";
        };
    }

    private static Component legacy(String s) {
        return LegacyComponentSerializer.legacySection().deserialize(s);
    }

    /** Call on player quit to release the board reference. */
    public static void remove(UUID uuid) {
        boards.remove(uuid);
    }
}
