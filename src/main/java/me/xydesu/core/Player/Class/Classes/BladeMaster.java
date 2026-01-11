package me.xydesu.core.Player.Class.Classes;

import me.xydesu.core.Player.Class.ClassManager;
import me.xydesu.core.Player.Player;

import java.util.List;

public class BladeMaster extends ClassManager {
    @Override
    public String className() {
        return "BladeMaster";
    }

    @Override
    public String classDisplay() {
        return "劍豪";
    }

    @Override
    public List<String> classDescription() {
        return List.of(
                "劍豪是以迅捷和精準著稱的戰士，",
                "擅長使用各種劍術來擊敗敵人。",
                "他們能夠在戰鬥中迅速移動，",
                "並以致命的劍技削弱對手。"
        );
    }

    @Override
    public void castSkill1(Player player) {
        player.getBukkitPlayer().sendMessage("§a[技能] §7你使用了 §f劍氣斬§7！");
    }

    @Override
    public void castSkill2(Player player) {
        player.getBukkitPlayer().sendMessage("§a[技能] §7你使用了 §f瞬步§7！");
    }

    @Override
    public void castSkill3(Player player) {
        player.getBukkitPlayer().sendMessage("§a[技能] §7你使用了 §f劍刃風暴§7！");
    }

    @Override
    public void castUltimate(Player player) {
        player.getBukkitPlayer().sendMessage("§a[技能] §7你使用了 §6終極劍技§7！");
    }
}
