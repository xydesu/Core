package me.xydesu.core.Tasks;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 這個類別現在是一個管理器。
 * 它負責監聽玩家加入/退出以維護 BossBar，
 * 並且有一個定時任務來循環更新這些 Bar。
 */
public class PlayerHudTask extends BukkitRunnable implements Listener {

    // --- Resource Pack Glyphs ---
    private static final String NEGATIVE_SHIFT = "\uF801\uF801\uF801"; // Shift left ~300px
    private static final String NEGATIVE_BACK = "\uF801"; // Shift back ~100px (adjust based on image width)
    private static final String HP_FRAME = "\uE000";

    private static final TextColor NO_SHADOW_COLOR = TextColor.color(0xFFFFFE);    

    
    // 關鍵：用來儲存每個玩家對應的唯一 BossBar
    private final Map<UUID, BossBar> playerBars = new HashMap<>();
    // 用來緩存上一次的顯示內容，避免重複發送封包
    private final Map<UUID, String> lastContent = new HashMap<>();

    /**
     * 這是定時任務運行的部分
     */
    @Override
    public void run() {
        // 遍歷所有在線玩家
        for (Player player : Bukkit.getOnlinePlayers()) {
            // 確保玩家有 BossBar (處理 reload 或其他情況)
            if (!playerBars.containsKey(player.getUniqueId())) {
                createBar(player);
            }
            updatePlayerHud(player);
        }
    }

    /**
     * 更新單個玩家的 HUD 邏輯
     */
    private void updatePlayerHud(Player player) {
        // 1. 從 Map 中獲取該玩家 persistent 的 BossBar
        BossBar hpBar = playerBars.get(player.getUniqueId());

        // 如果某種原因沒找到 (例如數據尚未加載完成)，就跳過
        if (hpBar == null) return;

        // 獲取自定義玩家數據
        me.xydesu.core.Player.Player customPlayer = me.xydesu.core.Player.Player.get(player);
        if (customPlayer == null) return;

        int currentHp = (int) customPlayer.getCurrentHealth();
        int maxHp = (int) customPlayer.getMaxHealth();
        int currentMana = (int) customPlayer.getCurrentMana();
        int maxMana = (int) customPlayer.getMaxMana();
        int currentStamina = (int) customPlayer.getCurrentStamina();
        int maxStamina = (int) customPlayer.getMaxStamina();

        // 檢查數值是否變動，如果沒變動則不更新 (優化效能)
        String contentKey = currentHp + ":" + maxHp + ":" + currentMana + ":" + maxMana + ":" + currentStamina + ":" + maxStamina;
        if (contentKey.equals(lastContent.get(player.getUniqueId()))) {
            return;
        }
        lastContent.put(player.getUniqueId(), contentKey);

        // 2. 組合標題 Component
        // 使用文字顯示 血量、魔力、體力
        // 加上材質包圖標
        String hpFill = getHpFillGlyph((double) currentHp / maxHp);
        
        Component hudTitle = Component.text()
                .append(Component.text(NEGATIVE_SHIFT)) // Move to top-left
                .append(Component.text(HP_FRAME, NO_SHADOW_COLOR)) // Frame
                .append(Component.text(NEGATIVE_BACK)) // Move back to overlay fill
                .append(Component.text(hpFill, NO_SHADOW_COLOR)) // Fill
                /*.append(Component.text("  ")) // Spacing
                .append(Component.text(currentHp + "/" + maxHp + "❤", NamedTextColor.RED))
                .append(Component.text("   "))
                .append(Component.text(currentMana + "/" + maxMana + "✎", NamedTextColor.AQUA))
                .append(Component.text("   "))
                .append(Component.text(currentStamina + "/" + maxStamina + "⚡", NamedTextColor.YELLOW))*/
                .build();

        // 3. 更新現有 BossBar 的標題
        hpBar.name(hudTitle);
        hpBar.progress(0f); // 確保隱藏原版條
    }

    private String getHpFillGlyph(double percentage) {
        int index = (int) (percentage * 10);
        if (index < 0) index = 0;
        if (index > 10) index = 10;
        // \uE010 is base.
        char base = '\uE010';
        return String.valueOf((char)(base + index));
    }

    private void createBar(Player player) {
        BossBar bar = BossBar.bossBar(
                Component.empty(),
                0f,
                BossBar.Color.WHITE,
                BossBar.Overlay.PROGRESS
        );
        playerBars.put(player.getUniqueId(), bar);
        player.showBossBar(bar);




    }
}