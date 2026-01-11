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

            TextColor hpColor = ((double) currentHealth / maxHealth < 0.3) ? NamedTextColor.DARK_RED
                    : NamedTextColor.RED;
            Component hpComponent = Component.text("❤ HP: ", NamedTextColor.GRAY)
                    .append(Component.text((int) currentHealth + " / " + (int) maxHealth, hpColor)
                            .decoration(TextDecoration.BOLD, false));
            Component separator = Component.text(" | ", NamedTextColor.DARK_GRAY).decoration(TextDecoration.BOLD,
                    false);

            // MP 部分
            Component manaComponent = Component.text("✎ MP: ", NamedTextColor.GRAY)
                    .append(Component.text((int) currentMana + " / " + (int) maxMana, NamedTextColor.AQUA)
                            .decoration(TextDecoration.BOLD, false));

            Component Health = Component.text()
                    .append(Component.text("\uE000"))
                    .build();

            Component Mana = Component.text()
                    .append(Component.text("\uE001"))
                    .build();

            bukkitPlayer.sendActionBar(hpComponent.append(separator).append(manaComponent).append(Health).append(Mana));
        }
    }

}
