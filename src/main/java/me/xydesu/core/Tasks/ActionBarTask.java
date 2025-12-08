package me.xydesu.core.Tasks;

import me.xydesu.core.Player.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ActionBarTask extends BukkitRunnable {

    @Override
    public void run() {
        for (org.bukkit.entity.Player bukkitPlayer : Bukkit.getOnlinePlayers()) {
            Player customPlayer = Player.get(bukkitPlayer);

            int currentHealth = (int) customPlayer.getCurrentHealth();
            int maxHealth = (int) customPlayer.getMaxHealth();
            int currentMana = (int) customPlayer.getCurrentMana();
            int maxMana = (int) customPlayer.getMaxMana();

            Component Health = Component.text()
                    .append(Component.text("\uE000"))
                    .build();

            Component hp_progress = Component.text(getHpFillGlyph((double) currentHealth /maxHealth));
            Component mana_progress = Component.text(getManaFillGlyph((double) currentMana /maxMana));

            Component Negative = Component.text("\uF801");
            Component Negative_Half = Component.text("\uF805");
            Component Negative_one = Component.text("\uF806");

            Component Mana = Component.text()
                    .append(Component.text("\uE001"))
                    .build();

            //bukkitPlayer.sendActionBar(actionBar);
            //bukkitPlayer.sendActionBar(hpComponent.append(separator).append(manaComponent).append(Negative).append(Health).append(Negative).append(Mana));
            //bukkitPlayer.sendMessage(getHpFillGlyph(currentHealth));
            bukkitPlayer.sendActionBar(Health
                    .append(Negative)
                    .append(hp_progress)
                    .append(Negative)
                    .append(Negative_one)
                    .append(Negative_one)
                    .append(Mana)
                    .append(Negative)
                    .append(mana_progress)
            );
            /*Component testBar = Component.text()
                    .append(Component.text("\uE000")) // 第一層背景
                    .append(Component.text("\uF801")) // 負空間 (假設你是 -182)
                    .append(Component.text("\uE100").color(NamedTextColor.RED)) // 第二層背景 (變紅色以利區分)
                    .build();

            bukkitPlayer.sendActionBar(testBar);*/
        }
    }

    private String getHpFillGlyph(double percentage) {
        int index = (int) (percentage * 100);
        if (index < 0) index = 0;
        if (index > 100) index = 100;
        // \uE010 is base.
        char base = '\uE100';
        return String.valueOf((char)(base + index));
    }

    private String getManaFillGlyph(double percentage) {
        int index = (int) (percentage * 100);
        if (index < 0) index = 0;
        if (index > 100) index = 100;
        // \uE010 is base.
        char base = '\uE165';
        return String.valueOf((char)(base + index));
    }
}
