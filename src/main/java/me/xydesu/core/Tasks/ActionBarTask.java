package me.xydesu.core.Tasks;

import me.xydesu.core.Player.Party.Party;
import me.xydesu.core.Player.Party.PartyManager;
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

            Component manaComponent = Component.text("✎ MP: ", NamedTextColor.GRAY)
                    .append(Component.text((int) currentMana + " / " + (int) maxMana, NamedTextColor.AQUA)
                            .decoration(TextDecoration.BOLD, false));

            Component actionBar = hpComponent.append(separator).append(manaComponent);

            // Append party members' HP
            Party party = PartyManager.getParty(bukkitPlayer.getUniqueId());
            if (party != null && party.size() > 1) {
                for (java.util.UUID memberUUID : party.getMembers()) {
                    if (memberUUID.equals(bukkitPlayer.getUniqueId())) continue;
                    org.bukkit.entity.Player memberBukkit = Bukkit.getPlayer(memberUUID);
                    if (memberBukkit == null) continue;
                    Player memberCustom = Player.get(memberBukkit);
                    int mHp = (int) memberCustom.getCurrentHealth();
                    int mMaxHp = (int) memberCustom.getMaxHealth();
                    TextColor mColor = ((double) mHp / mMaxHp < 0.3) ? NamedTextColor.DARK_RED : NamedTextColor.RED;
                    actionBar = actionBar
                            .append(separator)
                            .append(Component.text(memberBukkit.getName() + " ❤", NamedTextColor.GRAY))
                            .append(Component.text(mHp + "/" + mMaxHp, mColor));
                }
            }

            bukkitPlayer.sendActionBar(actionBar);
        }
    }

}

