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

    // Unique invisible entries used as per-line anchors (one color-code each).
    // We need 13 entries for 13 lines; §0-§c covers exactly 13 values.
    private static final String[] ENTRIES = {
        "§0", "§1", "§2", "§3", "§4", "§5", "§6",
        "§7", "§8", "§9", "§a", "§b", "§c"
    };

    private static final Map<UUID, Scoreboard> boards = new HashMap<>();

    @Override
    public void run() {
        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
            updateBoard(p, Player.get(p));
        }
    }

    private void updateBoard(org.bukkit.entity.Player p, Player cp) {
        // Create the board and objective once per player
        if (!boards.containsKey(p.getUniqueId())) {
            Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
            Component title = Component.text("✦ CoreRPG ✦")
                    .color(TextColor.fromHexString("#FFD700"))
                    .decoration(TextDecoration.BOLD, true);
            Objective obj = sb.registerNewObjective("stats", Criteria.DUMMY, title);
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);

            // Pre-register teams and assign fixed scores (positions never change)
            for (int i = 0; i < ENTRIES.length; i++) {
                Team team = sb.registerNewTeam("l" + i);
                team.addEntry(ENTRIES[i]);
                obj.getScore(ENTRIES[i]).setScore(ENTRIES.length - i);
            }

            boards.put(p.getUniqueId(), sb);
            p.setScoreboard(sb);
        }

        Scoreboard board = boards.get(p.getUniqueId());
        Objective obj = board.getObjective("stats");
        if (obj == null) return;

        // ── Gather stats ─────────────────────────────────────────────────────
        String className = cp.getPlayerClass() != null
                ? cp.getPlayerClass().classDisplay() : "§7未選擇";
        int lv = cp.getLevel();
        int hp = (int) cp.getCurrentHealth(), mhp = (int) cp.getMaxHealth();
        int mp = (int) cp.getCurrentMana(),   mmp = (int) cp.getMaxMana();
        int st = (int) cp.getCurrentStamina(), mst = (int) cp.getMaxStamina();
        int str = (int) cp.getStrength(),      def = (int) cp.getDefense();
        double crit    = cp.getCritChance() * 100;
        double critDmg = cp.getCritDamage() * 100 - 100;
        int ap = cp.getAttributePoints();
        double expPct = lv >= 100 ? 100.0 : cp.getExp() / cp.getRequiredExp() * 100;

        // ── Build lines (index 0 = top, index 12 = bottom) ───────────────────
        String[] lines = {
            "§8─────────────────",                                          //  0
            "§e▸ §f" + p.getName(),                                         //  1
            "§7等級 §a" + lv + " §8│ §f" + className,                      //  2
            expBar(expPct),                                                   //  3
            "§8─────────────────",                                          //  4
            hpColor(hp, mhp) + "❤ §f" + hp + "§7/§f" + mhp,               //  5
            "§b✎ §f" + mp  + "§7/§f" + mmp,                                //  6
            "§a⚡ §f" + st  + "§7/§f" + mst,                               //  7
            "§8─────────────────",                                          //  8
            "§6⚔ §f" + str + "  §9♦ §f" + def,                            //  9
            String.format("§eCrit §f%.0f%% §8│ §6+%.0f%%", crit, critDmg), // 10
            ap > 0 ? "§a▶ AP 可分配: §f" + ap : "§7AP: §f0",               // 11
            "§8─────────────────"                                           // 12
        };

        // Update only the text content (team prefix) each tick
        for (int i = 0; i < lines.length; i++) {
            Team team = board.getTeam("l" + i);
            if (team == null) continue;
            team.prefix(legacy(lines[i]));
            team.suffix(Component.empty());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static String expBar(double pct) {
        int filled = Math.min(10, (int) (pct / 10.0));
        StringBuilder sb = new StringBuilder("§7[");
        for (int i = 0; i < 10; i++) {
            sb.append(i < filled ? "§a█" : "§8░");
        }
        return sb.append("§7] §f").append(String.format("%.0f", pct)).append("%").toString();
    }

    private static String hpColor(int cur, int max) {
        if (max <= 0) return "§c";
        double r = (double) cur / max;
        return r < 0.3 ? "§c" : r < 0.6 ? "§6" : "§a";
    }

    private static Component legacy(String s) {
        return LegacyComponentSerializer.legacySection().deserialize(s);
    }

    /** Call on player quit to free the board reference. */
    public static void remove(UUID uuid) {
        boards.remove(uuid);
    }
}
