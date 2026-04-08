package me.xydesu.core.Tasks;

import me.xydesu.core.Mob.CustomMob;
import me.xydesu.core.Mob.Region;
import me.xydesu.core.Mob.RegionManager;
import me.xydesu.core.Utils.Keys;
import me.xydesu.core.Utils.PDC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Periodically teleports custom-mob name displays to their owner entity and
 * spawns new custom mobs in configured {@link Region}s when the mob count
 * inside a region is below the configured maximum.
 */
public class CustomMobTask extends BukkitRunnable {

    @Override
    public void run() {
        // Keep name display entities on top of their mobs
        CustomMob.getNameDisplays().forEach((entity, display) -> display.teleport(entity));

        // Region spawning
        for (Region region : RegionManager.getRegions()) {
            if (!region.shouldSpawn()) continue;

            World world = Bukkit.getWorld(region.getWorldName());
            if (world == null) continue;

            CustomMob template = CustomMob.get(region.getMobId());
            if (template == null) continue;

            // Count how many of this mob type are already alive inside the region
            long count = world.getEntities().stream()
                    .filter(e -> e instanceof LivingEntity)
                    .filter(e -> PDC.has(e, Keys.CUSTOM_MOB, PersistentDataType.BOOLEAN))
                    .filter(e -> region.getMobId().equals(PDC.get(e, Keys.ID, PersistentDataType.STRING, null)))
                    .filter(e -> isInsideRegion(e, region))
                    .count();

            if (count >= region.getMaxMobs()) continue;

            Location spawnLoc = region.getRandomSpawnLocation(world);
            // Place the mob on solid ground
            spawnLoc = world.getHighestBlockAt(spawnLoc).getLocation().add(0, 1, 0);
            template.spawn(spawnLoc);
        }
    }

    private boolean isInsideRegion(Entity entity, Region region) {
        if (!entity.getWorld().getName().equals(region.getWorldName())) return false;
        Location loc = entity.getLocation();
        double dx = loc.getX() - region.getCenterX();
        double dz = loc.getZ() - region.getCenterZ();
        return Math.sqrt(dx * dx + dz * dz) <= region.getRadius();
    }
}
