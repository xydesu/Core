package me.xydesu.core.Mob.Mobs;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.xydesu.core.Mob.CustomMob;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;

public class TestPlayer extends CustomMob {

    @Override
    public String getID() {
        return "TEST_PLAYER";
    }

    @Override
    public String getName() {
        return "Player";
    }

    @Override
    public EntityType getType() {
        return EntityType.ZOMBIE;
    }

    @Override
    public double getMaxHealth() {
        return 1000;
    }

    @Override
    public int getLevel() {
        return 100;
    }

    @Override
    public void onSpawn(LivingEntity entity) {
        entity.setCustomNameVisible(false);
        ((Zombie) entity).setShouldBurnInDay(false);

        PlayerDisguise playerDisguise = new PlayerDisguise(getName());
        playerDisguise.setNameVisible(false);
        DisguiseAPI.disguiseToAll(entity, playerDisguise);
    }
}
