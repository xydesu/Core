package me.xydesu.core.Player.Class;

import me.xydesu.core.Player.Class.Classes.BladeMaster;
import me.xydesu.core.Player.Player;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ClassManager {

    private static final Map<String, ClassManager> classes = new HashMap<>();

    static {
        classes.put("BladeMaster", new BladeMaster());
    }

    public static ClassManager get(String name) {
        return classes.get(name);
    }

    public static List<ClassManager> getAllClasses() {
        return new ArrayList<>(classes.values());
    }

    public abstract String className();

    public abstract String classDisplay();

    public abstract List<String> classDescription();

    public abstract void castSkill1(Player player);

    public abstract void castSkill2(Player player);

    public abstract void castSkill3(Player player);

    public abstract void castUltimate(Player player);

    public void onAttack(Player player, LivingEntity target, double damage, boolean isCrit) {
    }

    public void onDefend(Player player, Entity attacker, double damage) {
    }

    public List<String> getPassives() {
        return new ArrayList<>();
    }

}
