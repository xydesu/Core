package me.xydesu.core.Item.Items.Weapons.SCYTHE;

import me.xydesu.core.Item.Item;
import me.xydesu.core.Item.Rarity;
import me.xydesu.core.Item.ToolType;
import me.xydesu.core.Utils.DamageCalc;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

public class Reaper_of_Fate extends Item {

    private static final Map<UUID, Long> cooldowns = new HashMap<>();

    @Override
    public String getID() {
        return "Reaper_of_Fate";
    }

    @Override
    public String getName() {
        return "命運收割者";
    }

    @Override
    public Material getMaterial() {
        return Material.NETHERITE_HOE;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.SCYTHE;
    }

    @Override
    public int getRequiredLevel() {
        return 80;
    }

    @Override
    public double getDamage() {
        return 22;
    }

    @Override
    public double getAttackSpeed() {
        return 0.6;
    }

    @Override
    public double getCritChance() {
        return 0.15;
    }

    @Override
    public double getLifeSteal() {
        return 3.0;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.MYTHIC;
    }

    @Override
    public boolean hasAOE(){
        return true;
    }

    @Override
    public double getRange() {
        return 5;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction().isRightClick()) {
            org.bukkit.entity.Player p = event.getPlayer();
            me.xydesu.core.Player.Player cp = me.xydesu.core.Player.Player.get(p);
            
            // Mana check
            if (cp.getCurrentMana() < 30) {
                p.sendMessage("§c魔力不足！");
                return;
            }
            
            // Cooldown
            long now = System.currentTimeMillis();
            if (cooldowns.containsKey(p.getUniqueId())) {
                long lastUse = cooldowns.get(p.getUniqueId());
                if (now - lastUse < 45000) {
                    p.sendMessage("§c技能冷卻中！ (" + ((45000 - (now - lastUse)) / 1000) + "s)");
                    return;
                }
            }
            
            cp.setCurrentMana(cp.getCurrentMana() - 30);
            cooldowns.put(p.getUniqueId(), now);
            
            // Effect
            p.swingMainHand();
            p.getWorld().spawnParticle(org.bukkit.Particle.SWEEP_ATTACK, p.getLocation().add(0, 1, 0), 5);
            
            List<LivingEntity> targets = new ArrayList<>();
            for (org.bukkit.entity.Entity e : p.getNearbyEntities(8, 8, 8)) {
                if (e instanceof LivingEntity target && e != p) {
                    if (p.getLocation().getDirection().dot(target.getLocation().subtract(p.getLocation()).toVector().normalize()) > 0.5) {
                        targets.add(target);
                    }
                }
            }
            
            for (LivingEntity target : targets) {
                double damage = DamageCalc.getDamage(p, p.getInventory().getItemInMainHand(), target, true).damage * 1.5;
                target.damage(damage, p);
                target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
            }
            
            p.sendMessage("§c§l命運召喚！");
        }
    }

    @Override
    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add("<dark_gray>🔻 額外體力消耗：+20%</dark_gray>");
        lore.add("");
        lore.add("<gold><b>III. 核心機制 (Unique Effects)</b></gold>");
        lore.add("");
        lore.add("<yellow><b>🌑 被動：亡魂抽取 (Soul Drain)</b></yellow>");
        lore.add("<aqua>每次擊殺敵對生物，將有 <yellow>25%</yellow> 機率抽取其亡魂，為使用者恢復 <green>5 點生命值</green> 並短暫提升攻擊速度 <yellow>(+10% 於 3 秒)</yellow>。</aqua>");
        lore.add("");
        lore.add("<red><b>💀 主動：命運召喚 (Call of Fate)</b></red>");
        lore.add("<gray>消耗 30 點魔力。揮舞鐮刀劃出一道巨大的死亡弧線，對前方 8 格範圍內所有目標造成 <red><b>150%</b></red> 武器傷害，並使其承受 <red><b>「虛弱 II」</b></red> 效果 5 秒。</gray>");
        lore.add("<<blue>⏳ 冷卻時間：<aqua>45 秒</aqua></blue>");
        lore.add("");
        lore.add("<gold><b>IV. 史詩傳說 (Epic Lore)</b></gold>");
        lore.add("");
        lore.add("<dark_gray><i>「這柄巨鐮不切割生命，它只收割時間。握持它的手，便掌握了終結的權柄。」</i></dark_gray>");
        lore.add("");
        lore.add("<gray>傳說中，這是負責平衡生死的遠古神祇的遺留物。它並非由凡間鋼鐵鑄造，而是由終焉之地的嘆息與虛無之界的寒冰凝結而成。每當世界陷入極度混亂時，它便會尋找一位飽受折磨的靈魂作為其持有者，引導其完成歷史上註定的終結儀式。</gray>");
        return lore;
    }
}
