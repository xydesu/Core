package me.xydesu.core.Mob.Mobs;

import me.xydesu.core.Mob.CustomMob;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

public class SuperZombie extends CustomMob {

    @Override
    public String getID() {
        return "SUPER_ZOMBIE";
    }

    @Override
    public String getName() {
        return "超級殭屍";
    }

    @Override
    public EntityType getType() {
        return EntityType.ZOMBIE;
    }

    @Override
    public double getMaxHealth() {
        return 500;
    }

    @Override
    public int getLevel() {
        return 10;
    }

    @Override
    public void onSpawn(LivingEntity entity) {
        if (entity instanceof Zombie zombie) {
            // 裝備設定
            zombie.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
            zombie.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
            zombie.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
            zombie.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
            zombie.getEquipment().setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));
            
            // 防止殭屍在白天燃燒
            zombie.setShouldBurnInDay(false);
        }
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> drops = new ArrayList<>();
        tryDrop(drops, new ItemStack(Material.DIAMOND, 1), 0.5); // 50% 機率掉落鑽石
        tryDrop(drops, new ItemStack(Material.ROTTEN_FLESH, 5), 1.0); // 100% 機率掉落腐肉
        return drops;
    }
}
