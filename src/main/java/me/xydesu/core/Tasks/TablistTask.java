package me.xydesu.core.Tasks;

import me.xydesu.core.Player.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TablistTask extends BukkitRunnable {

    // Header title cycles through these colour gradients (§6=gold, §e=yellow, §f=white)
    private static final String[][] TITLE_FRAMES = {
        { "§6§l✦ §e§lCoreRPG §6§l✦", "§7§o⁌ 傳奇冒險的世界 ⁍" },
        { "§e§l✧ §f§lCoreRPG §e§l✧", "§7§o⁌ 傳奇冒險的世界 ⁍" },
        { "§6§l✦ §e§lCoreRPG §6§l✦", "§8§o⁌ 傳奇冒險的世界 ⁍" },
        { "§e§l✧ §f§lCoreRPG §e§l✧", "§8§o⁌ 傳奇冒險的世界 ⁍" }
    };

    private int tick = 0;

    @Override
    public void run() {
        tick++;
        int frame  = tick % TITLE_FRAMES.length;
        int online = Bukkit.getOnlinePlayers().size();

        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
            Player cp = Player.get(p);
            updateTablist(p, cp, frame, online);
        }
    }

    private void updateTablist(org.bukkit.entity.Player p, Player cp, int frame, int online) {
        String classColor = classColor(cp);
        String className  = cp.getPlayerClass() != null
                ? cp.getPlayerClass().classDisplay() : "§7無職業";
        int lv  = cp.getLevel();
        int hp  = (int) cp.getCurrentHealth(), mhp = (int) cp.getMaxHealth();
        int mp  = (int) cp.getCurrentMana(),   mmp = (int) cp.getMaxMana();

        String hpColor = mhp > 0 && (double) hp / mhp >= 0.6 ? "§a"
                : mhp > 0 && (double) hp / mhp >= 0.3 ? "§6" : "§c";

        // ── Header ────────────────────────────────────────────────────────────
        String titleLine = TITLE_FRAMES[frame][0];
        String subLine   = TITLE_FRAMES[frame][1];
        Component header = legacy(
                "\n" +
                "  " + titleLine + "  \n" +
                "  " + subLine   + "  \n"
        );

        // ── Footer ────────────────────────────────────────────────────────────
        Component footer = legacy(
                "\n" +
                "§8◈§7──────────────────────§8◈\n" +
                "§f" + p.getName()
                        + " §8│ " + classColor + className
                        + " §8│ §aLv.§f" + lv + "\n" +
                hpColor + "❤ §f" + hp + "§7/§f" + mhp
                        + "  §b✎ §f" + mp + "§7/§f" + mmp + "\n" +
                "§7在線玩家: §f" + online + "\n" +
                "§8◈§7──────────────────────§8◈\n"
        );

        p.sendPlayerListHeaderAndFooter(header, footer);

        // ── Player list name (shown next to the ping icon in tab) ─────────────
        Component listName = legacy(
                classColor + "§l[" + lv + "]§r §f" + p.getName()
        );
        p.playerListName(listName);
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
