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

/**
 * Updates each player's personal sidebar scoreboard every 5 ticks (4×/s).
 *
 * Animation layers:
 *   • Objective title   – gradient shimmer cycles through 6 colour frames (1 s each)
 *   • Player name       – per-character wave colour (gold→yellow→white→yellow→gold→gray)
 *   • Inner divider     – ◈ dot bounces left↔right across 10 positions (0.5 s/step)
 *   • Outer border      – alternates §6✦ ↔ §e✧ every 1 s; turns red-pulsing at critical HP
 *   • HP line           – solid §4 bar flashes every 0.5 s when HP < 30 %
 *   • AP alert          – gold ↔ green pulse when attribute points are available
 */
public class ScoreboardTask extends BukkitRunnable {

    // 13 unique invisible anchors (§0–§c) for 13 sidebar lines
    private static final String[] ENTRIES = {
        "§0","§1","§2","§3","§4","§5","§6","§7","§8","§9","§a","§b","§c"
    };

    // Objective title shimmer – cycles once per second (every 4 runs at 5-tick interval)
    private static final Component[] TITLE_FRAMES = {
        title("#FFD700", "✦ CoreRPG ✦"),
        title("#FFEE44", "✦ CoreRPG ✦"),
        title("#FFFFFF", "✧ CoreRPG ✧"),
        title("#FFEE44", "✦ CoreRPG ✦"),
        title("#FFD700", "✦ CoreRPG ✦"),
        title("#AAAAAA", "✦ CoreRPG ✦"),
    };

    // Wave palette applied across player-name characters
    private static final String[] WAVE = {"§6", "§e", "§f", "§e", "§6", "§7"};

    // 4-frame spinner shown before the player name
    private static final String[] SPIN = {"◐", "◓", "◑", "◒"};

    private int tick = 0;
    private static final Map<UUID, Scoreboard> boards = new HashMap<>();

    @Override
    public void run() {
        tick++;
        // Title frame:  changes every 4 ticks  = every 1 s
        // Spinner frame: changes every tick     = every 0.25 s  → 1 full rotation per second
        // Divider pos:  advances every 2 ticks  = every 0.5 s
        // Flash toggle: changes every 2 ticks   = every 0.5 s
        int titleFrame = (tick / 4) % TITLE_FRAMES.length;
        int spinFrame  = tick % SPIN.length;
        int divPos     = bouncePos(tick, 2, 10);
        boolean flash  = (tick / 2) % 2 == 0;

        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
            updateBoard(p, Player.get(p), titleFrame, spinFrame, divPos, flash);
        }
    }

    private void updateBoard(org.bukkit.entity.Player p, Player cp,
                             int titleFrame, int spinFrame, int divPos, boolean flash) {
        // ── Create board once per player ──────────────────────────────────────
        if (!boards.containsKey(p.getUniqueId())) {
            Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective obj = sb.registerNewObjective("stats", Criteria.DUMMY, TITLE_FRAMES[0]);
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
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

        // Animate title
        obj.displayName(TITLE_FRAMES[titleFrame]);

        // ── Stats ─────────────────────────────────────────────────────────────
        String classColor = classColor(cp);
        String className  = cp.getPlayerClass() != null ? cp.getPlayerClass().classDisplay() : "未選擇";
        String classIcon  = cp.getPlayerClass() != null ? classIcon(cp.getPlayerClass().className()) : "§7✦";
        int    lv         = cp.getLevel();
        double hp = cp.getCurrentHealth(), mhp = cp.getMaxHealth();
        double mp = cp.getCurrentMana(),   mmp = cp.getMaxMana();
        double st = cp.getCurrentStamina(), mst = cp.getMaxStamina();
        int    str = (int) cp.getStrength(), def = (int) cp.getDefense();
        double crit     = cp.getCritChance() * 100;
        double critMult = cp.getCritDamage();
        int    ap       = cp.getAttributePoints();
        double expPct   = lv >= 100 ? 100.0 : cp.getExp() / cp.getRequiredExp() * 100;

        boolean critHp = mhp > 0 && hp / mhp < 0.3;
        String  hpFill = hpFill(hp, mhp);

        // ── Dynamic outer border (pulses red when HP critical) ────────────────
        String border = critHp
                ? (flash ? "§c§l! §4────────────§c§l !" : "§4§l! §c────────────§4§l !")
                : (flash ? "§6✦§e────────────§6✦"        : "§e✧§6────────────§e✧");

        // ── Travelling ◈ dot inner divider ───────────────────────────────────
        String divider = travelDiv(divPos, 10);

        // ── HP line: flash solid dark-red bar when critical ───────────────────
        String hpLine = (critHp && flash)
                ? "§c❤ §4§l" + "█".repeat(7) + "§r §c" + (int) hp + "§7/§f" + (int) mhp
                : hpFill + "❤ " + miniBar(hp, mhp, 7, hpFill) + " §f" + (int) hp + "§7/§f" + (int) mhp;

        // ── AP alert: pulses gold ↔ green ─────────────────────────────────────
        String apLine = ap > 0
                ? (flash ? "§a§l▶ §r§aAP 可分配: §e§l" + ap : "§e§l▶ §r§eAP 可分配: §a§l" + ap)
                : "§7AP: §f0";

        // ── Lines (index 0 = top, index 12 = bottom) ──────────────────────────
        String[] lines = {
            border,                                                              //  0 animated border
            classColor + SPIN[spinFrame] + " " + waveName(p.getName(), tick),  //  1 spinner + wave name
            "§8[§aLv.§f" + lv + "§8] " + classIcon + " " + classColor + className, // 2 level badge + class
            "§7EXP " + expBar(expPct),                                          //  3 exp bar
            divider,                                                             //  4 travelling divider
            hpLine,                                                              //  5 HP
            "§b✎ "  + miniBar(mp, mmp, 7, "§b") + " §f" + (int) mp + "§7/§f" + (int) mmp, // 6 MP
            "§a⚡ " + miniBar(st, mst, 7, "§a") + " §f" + (int) st + "§7/§f" + (int) mst, // 7 Stamina
            divider,                                                             //  8 travelling divider
            "§6⚔ §f" + str + "  §9♦ §f" + def,                               //  9 STR / DEF
            String.format("§eCrit §f%.0f%% §8│ §6×%.1f", crit, critMult),     // 10 crit stats
            apLine,                                                              // 11 AP
            border                                                               // 12 animated border
        };

        for (int i = 0; i < lines.length; i++) {
            Team team = board.getTeam("l" + i);
            if (team == null) continue;
            team.prefix(legacy(lines[i]));
            team.suffix(Component.empty());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Produces a position that bounces 0 → (max-1) → 0 with one step per {@code step} ticks. */
    private static int bouncePos(int tick, int step, int max) {
        int period = max * 2 - 2;
        int raw    = (tick / step) % period;
        return raw < max ? raw : period - raw;
    }

    /** Wave shimmer: each character of {@code name} gets a colour from WAVE[], offset by tick. */
    private static String waveName(String name, int tick) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            sb.append(WAVE[(i + tick) % WAVE.length]).append("§l").append(name.charAt(i));
        }
        return sb.toString();
    }

    /** Inner divider: a §e◈ dot travels across 10 §8─ dashes. */
    private static String travelDiv(int pos, int width) {
        StringBuilder sb = new StringBuilder("§8");
        for (int i = 0; i < width; i++) {
            sb.append(i == pos ? "§e◈§8" : "─");
        }
        return sb.toString();
    }

    /** 10-block EXP bar (§a filled, §8 empty). */
    private static String expBar(double pct) {
        int filled = Math.min(10, (int) (pct / 10.0));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) sb.append(i < filled ? "§a█" : "§8░");
        return sb.append(" §f").append(String.format("%.0f", pct)).append("%").toString();
    }

    /** Fixed-width progress bar with a given fill colour. */
    private static String miniBar(double cur, double max, int width, String fillColor) {
        int filled = max > 0 ? Math.max(0, Math.min(width, (int) Math.round(cur / max * width))) : 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < width; i++) sb.append(i < filled ? fillColor + "█" : "§8░");
        return sb.toString();
    }

    /** HP fill colour: green → orange → red as health drops. */
    private static String hpFill(double cur, double max) {
        if (max <= 0) return "§c";
        double r = cur / max;
        return r >= 0.6 ? "§a" : r >= 0.3 ? "§6" : "§c";
    }

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

    private static Component title(String hex, String text) {
        return Component.text(text)
                .color(TextColor.fromHexString(hex))
                .decoration(TextDecoration.BOLD, true);
    }

    private static Component legacy(String s) {
        return LegacyComponentSerializer.legacySection().deserialize(s);
    }

    /** Call on player quit to release the board reference. */
    public static void remove(UUID uuid) {
        boards.remove(uuid);
    }
}
