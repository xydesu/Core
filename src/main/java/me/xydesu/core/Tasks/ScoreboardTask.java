package me.xydesu.core.Tasks;

import me.xydesu.core.Player.Party.Party;
import me.xydesu.core.Player.Party.PartyManager;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Updates each player's personal sidebar scoreboard every 5 ticks (4x/s).
 *
 * Animation layers:
 *   - Objective title   - 12-frame rainbow shimmer (0.5 s/frame, 6 s full cycle)
 *   - Player name       - 8-colour rainbow wave, offset by tick
 *   - Inner divider     - dot bounces left-right, advances every tick (smoothest travel)
 *   - Outer border      - alternates gold/yellow every 0.5 s; pulses red at critical HP
 *   - HP/MP/ST bars     - smooth eased bars, no numeric values
 *   - Party section     - party header + up to 2 member rows (name + mini HP bar)
 *                         or "solo" row + AP alert when not in a party
 */
public class ScoreboardTask extends BukkitRunnable {

    // 13 unique invisible anchors for 13 sidebar lines
    private static final String[] ENTRIES = {
        "\u00a70","\u00a71","\u00a72","\u00a73","\u00a74","\u00a75","\u00a76","\u00a77","\u00a78","\u00a79","\u00a7a","\u00a7b","\u00a7c"
    };

    // 12-frame rainbow title - changes every 2 ticks (0.5 s/frame, 6 s full cycle)
    private static final Component[] TITLE_FRAMES = {
        title("#FF4455", "\u2746 CoreRPG \u2746"),
        title("#FF7733", "\u2746 CoreRPG \u2746"),
        title("#FFBB22", "\u2747 CoreRPG \u2747"),
        title("#FFFF33", "\u2746 CoreRPG \u2746"),
        title("#AAFF44", "\u2746 CoreRPG \u2746"),
        title("#33FF99", "\u2747 CoreRPG \u2747"),
        title("#33FFFF", "\u2746 CoreRPG \u2746"),
        title("#3399FF", "\u2746 CoreRPG \u2746"),
        title("#9933FF", "\u2747 CoreRPG \u2747"),
        title("#FF33FF", "\u2746 CoreRPG \u2746"),
        title("#FF3399", "\u2746 CoreRPG \u2746"),
        title("#FF3366", "\u2747 CoreRPG \u2747"),
    };

    // 8-colour rainbow wave for player-name shimmer
    private static final String[] WAVE = {"\u00a7c", "\u00a76", "\u00a7e", "\u00a7a", "\u00a7b", "\u00a79", "\u00a75", "\u00a7d"};

    // 4-frame spinner shown before the player name
    private static final String[] SPIN = {"\u25d0", "\u25d3", "\u25d1", "\u25d2"};

    private int tick = 0;
    private static final Map<UUID, Scoreboard> boards = new HashMap<>();

    // Per-player eased display values - smoothly slide toward the real value each tick
    private static final Map<UUID, Double> easedHp = new HashMap<>();
    private static final Map<UUID, Double> easedMp = new HashMap<>();
    private static final Map<UUID, Double> easedSt = new HashMap<>();

    /** Easing factor: 25% of the remaining gap is closed each tick (approx 4x/s). */
    private static final double EASE = 0.25;

    @Override
    public void run() {
        tick++;
        // Title frame:   changes every 2 ticks = every 0.5 s (12 frames -> 6 s cycle)
        // Spinner frame: changes every tick     = every 0.25 s
        // Divider pos:   advances every tick    = smoothest travel
        // Flash toggle:  changes every 2 ticks  = every 0.5 s
        int titleFrame = (tick / 2) % TITLE_FRAMES.length;
        int spinFrame  = tick % SPIN.length;
        int divPos     = bouncePos(tick, 1, 10);
        boolean flash  = (tick / 2) % 2 == 0;

        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
            updateBoard(p, Player.get(p), titleFrame, spinFrame, divPos, flash);
        }
    }

    private void updateBoard(org.bukkit.entity.Player p, Player cp,
                             int titleFrame, int spinFrame, int divPos, boolean flash) {
        if (cp == null) return;

        // Create board once per player
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

        // Stats
        String classColor = classColor(cp);
        String className  = cp.getPlayerClass() != null ? cp.getPlayerClass().classDisplay() : "\u672a\u9078\u64c7";
        String classIcon  = cp.getPlayerClass() != null ? classIcon(cp.getPlayerClass().className()) : "\u00a77\u2746";
        int    lv         = cp.getLevel();
        double hp = cp.getCurrentHealth(), mhp = cp.getMaxHealth();
        double mp = cp.getCurrentMana(),   mmp = cp.getMaxMana();
        double st = cp.getCurrentStamina(), mst = cp.getMaxStamina();
        int    ap     = cp.getAttributePoints();
        double expPct = lv >= 100 ? 100.0 : cp.getExp() / cp.getRequiredExp() * 100;

        boolean critHp = mhp > 0 && hp / mhp < 0.3;
        String  hpFill = hpFill(hp, mhp);

        // Ease displayed bar values toward real values
        UUID   uid = p.getUniqueId();
        double dHp = easedHp.merge(uid, hp, (old, real) -> old + (real - old) * EASE);
        double dMp = easedMp.merge(uid, mp, (old, real) -> old + (real - old) * EASE);
        double dSt = easedSt.merge(uid, st, (old, real) -> old + (real - old) * EASE);

        // Dynamic outer border (pulses red when HP critical)
        String border = critHp
                ? (flash ? "\u00a7c\u00a7l\u2746\u00a74\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u00a7c\u00a7l\u2746"
                         : "\u00a74\u00a7l\u2746\u00a7c\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u00a74\u00a7l\u2746")
                : (flash ? "\u00a76\u2746\u00a7e\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u00a76\u2746"
                         : "\u00a7e\u2747\u00a76\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u00a7e\u2747");

        // Travelling dot inner divider
        String divider = travelDiv(divPos, 10);

        // HP bar: no numbers; flash solid dark-red when critical
        String hpLine = (critHp && flash)
                ? "\u00a7c\u2764 \u00a74\u00a7l\u2588\u2588\u2588\u2588\u2588\u2588\u2588"
                : hpFill + "\u2764 " + miniBar(dHp, mhp, 7, hpFill);

        // MP / ST bars (no numbers)
        String mpLine = "\u00a7b\u270e " + miniBar(dMp, mmp, 7, "\u00a7b");
        String stLine = "\u00a7a\u26a1 " + miniBar(dSt, mst, 7, "\u00a7a");

        // Party section
        Party party = PartyManager.getParty(uid);
        String partyHeader;
        String partyLine1;
        String partyLine2;

        if (party != null) {
            boolean isLeader = party.getLeader().equals(uid);
            String flagColor = flash ? "\u00a7e" : "\u00a76";
            partyHeader = flagColor + "\u2751 " + (isLeader ? "\u00a7e\u00a7l\u968a\u9577" : "\u00a7b\u00a7l\u968a\u4f0d")
                          + " \u00a78(" + party.size() + " \u4eba)";

            List<UUID> others = new ArrayList<>();
            for (UUID m : party.getMembers()) {
                if (!m.equals(uid)) others.add(m);
            }
            partyLine1 = memberLine(others.size() > 0 ? others.get(0) : null, flash);
            partyLine2 = others.size() > 1 ? memberLine(others.get(1), flash) : apLine(ap, flash);
        } else {
            partyHeader = flash ? "\u00a77\u2694 \u00a7f\u55ae\u4eba\u6a21\u5f0f" : "\u00a78\u2694 \u00a77\u55ae\u4eba\u6a21\u5f0f";
            partyLine1  = "\u00a78  \u00a77\u7d44\u968a: \u00a7f/party invite";
            partyLine2  = apLine(ap, flash);
        }

        // Lines (index 0 = top, index 12 = bottom)
        String[] lines = {
            border,                                                              //  0 animated border
            classColor + SPIN[spinFrame] + " " + waveName(p.getName(), tick),  //  1 spinner + wave name
            classIcon + " " + classColor + className + " \u00a78[\u00a7aLv.\u00a7f" + lv + "\u00a78]", // 2 class + level
            "\u00a77EXP " + expBar(expPct),                                    //  3 exp bar (bar only)
            divider,                                                             //  4 travelling divider
            hpLine,                                                              //  5 HP bar
            mpLine,                                                              //  6 MP bar
            stLine,                                                              //  7 ST bar
            divider,                                                             //  8 travelling divider
            partyHeader,                                                         //  9 party header
            partyLine1,                                                          // 10 party member 1
            partyLine2,                                                          // 11 party member 2 / AP
            border                                                               // 12 animated border
        };

        for (int i = 0; i < lines.length; i++) {
            Team team = board.getTeam("l" + i);
            if (team == null) continue;
            team.prefix(legacy(lines[i]));
            team.suffix(Component.empty());
        }
    }

    // Helpers

    /** Renders one party member row: class icon + (truncated) name + mini HP bar. */
    private static String memberLine(UUID memberUuid, boolean flash) {
        if (memberUuid == null) return "\u00a78  \u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500";
        org.bukkit.entity.Player mp = Bukkit.getPlayer(memberUuid);
        if (mp == null || !mp.isOnline()) return "\u00a78  \u00a77(\u96e2\u7dda)";
        Player cp = Player.get(mp);
        if (cp == null) return "\u00a78  \u00a7f" + mp.getName();

        String icon  = cp.getPlayerClass() != null ? classIcon(cp.getPlayerClass().className()) : "\u00a77\u2746";
        String color = classColor(cp);
        String name  = mp.getName().length() > 6 ? mp.getName().substring(0, 6) + "\u2026" : mp.getName();
        double h = cp.getCurrentHealth(), mh = cp.getMaxHealth();
        boolean crit = mh > 0 && h / mh < 0.3;
        String bar = (crit && flash)
                ? "\u00a7c\u00a7l\u2588\u2588\u2588\u2588"
                : hpFill(h, mh) + miniBar(h, mh, 4, hpFill(h, mh));
        return "\u00a78 " + icon + " " + color + name + " \u00a78[" + bar + "\u00a78]";
    }

    /** AP alert: gold/green pulse when AP available; dash row otherwise. */
    private static String apLine(int ap, boolean flash) {
        if (ap <= 0) return "\u00a78  \u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500";
        return flash ? "\u00a7a\u00a7l\u25b6 \u00a7r\u00a7aAP\u53ef\u5206\u914d: \u00a7e\u00a7l" + ap
                     : "\u00a7e\u00a7l\u25b6 \u00a7r\u00a7eAP\u53ef\u5206\u914d: \u00a7a\u00a7l" + ap;
    }

    /** Produces a position that bounces 0 to (max-1) to 0 with one step per {@code step} ticks. */
    private static int bouncePos(int tick, int step, int max) {
        int period = max * 2 - 2;
        int raw    = (tick / step) % period;
        return raw < max ? raw : period - raw;
    }

    /** Wave shimmer: each character of {@code name} gets a colour from WAVE[], offset by tick. */
    private static String waveName(String name, int tick) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            sb.append(WAVE[(i + tick) % WAVE.length]).append("\u00a7l").append(name.charAt(i));
        }
        return sb.toString();
    }

    /** Inner divider: a coloured dot travels across {@code width} dashes. */
    private static String travelDiv(int pos, int width) {
        StringBuilder sb = new StringBuilder("\u00a78");
        for (int i = 0; i < width; i++) {
            sb.append(i == pos ? "\u00a7e\u25c8\u00a78" : "\u2500");
        }
        return sb.toString();
    }

    /** 10-block EXP bar (filled, empty), no percentage number. */
    private static String expBar(double pct) {
        int filled = Math.min(10, (int) (pct / 10.0));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) sb.append(i < filled ? "\u00a7a\u2588" : "\u00a78\u2591");
        return sb.toString();
    }

    /** Fixed-width progress bar with a given fill colour. */
    private static String miniBar(double cur, double max, int width, String fillColor) {
        int filled = max > 0 ? Math.max(0, Math.min(width, (int) Math.round(cur / max * width))) : 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < width; i++) sb.append(i < filled ? fillColor + "\u2588" : "\u00a78\u2591");
        return sb.toString();
    }

    /** HP fill colour: green to orange to red as health drops. */
    private static String hpFill(double cur, double max) {
        if (max <= 0) return "\u00a7c";
        double r = cur / max;
        return r >= 0.6 ? "\u00a7a" : r >= 0.3 ? "\u00a76" : "\u00a7c";
    }

    private static String classColor(Player cp) {
        if (cp.getPlayerClass() == null) return "\u00a77";
        return switch (cp.getPlayerClass().className()) {
            case "BladeMaster"    -> "\u00a76";
            case "Berserker"      -> "\u00a7c";
            case "ShadowAssassin" -> "\u00a75";
            case "WindRanger"     -> "\u00a7a";
            case "Arbalestier"    -> "\u00a7e";
            case "Elementalist"   -> "\u00a7b";
            case "SoulReaper"     -> "\u00a74";
            default               -> "\u00a7f";
        };
    }

    private static String classIcon(String name) {
        return switch (name) {
            case "BladeMaster"    -> "\u00a76\u2694";
            case "Berserker"      -> "\u00a7c\u26a1";
            case "ShadowAssassin" -> "\u00a75\u2746";
            case "WindRanger"     -> "\u00a7a\u00bb";
            case "Arbalestier"    -> "\u00a7e\u2192";
            case "Elementalist"   -> "\u00a7b\u2738";
            case "SoulReaper"     -> "\u00a74\u263d";
            default               -> "\u00a77\u2746";
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

    /** Returns the personal sidebar scoreboard for the given player, or {@code null} if not yet created. */
    public static Scoreboard getBoard(UUID uuid) {
        return boards.get(uuid);
    }

    /** Call on player quit to release the board reference and easing state. */
    public static void remove(UUID uuid) {
        boards.remove(uuid);
        easedHp.remove(uuid);
        easedMp.remove(uuid);
        easedSt.remove(uuid);
    }

    /**
     * Resets every online player to the server's main scoreboard and clears all state.
     * Should be called from {@code Core#onDisable()}.
     */
    public static void clearAll() {
        for (UUID uuid : boards.keySet()) {
            org.bukkit.entity.Player p = Bukkit.getPlayer(uuid);
            if (p != null) p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
        boards.clear();
        easedHp.clear();
        easedMp.clear();
        easedSt.clear();
    }
}
