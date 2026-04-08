package me.xydesu.core.Mob;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Defines a rectangular spawn region where a specific {@link CustomMob} type
 * will be automatically spawned by {@link me.xydesu.core.Tasks.MobSpawnTask}.
 */
public class Region {

    private final String regionId;
    private final String worldName;
    private final double centerX;
    private final double centerY;
    private final double centerZ;
    private final double radius;
    private final String mobId;
    private final int maxMobs;
    private final int spawnIntervalTicks; // how often (in task runs) this region tries to spawn

    private int tickCounter = 0;

    public Region(String regionId, String worldName,
            double centerX, double centerY, double centerZ,
            double radius, String mobId, int maxMobs, int spawnIntervalTicks) {
        this.regionId = regionId;
        this.worldName = worldName;
        this.centerX = centerX;
        this.centerY = centerY;
        this.centerZ = centerZ;
        this.radius = radius;
        this.mobId = mobId;
        this.maxMobs = maxMobs;
        this.spawnIntervalTicks = spawnIntervalTicks;
    }

    public String getRegionId() { return regionId; }
    public String getWorldName() { return worldName; }
    public String getMobId() { return mobId; }
    public int getMaxMobs() { return maxMobs; }
    public double getRadius() { return radius; }
    public double getCenterX() { return centerX; }
    public double getCenterY() { return centerY; }
    public double getCenterZ() { return centerZ; }

    /**
     * Returns a random location within this region's horizontal circle
     * at the region's base Y level.
     *
     * @param world the Bukkit world
     * @return a random {@link Location} within the spawn area
     */
    public Location getRandomSpawnLocation(World world) {
        double angle = Math.random() * 2 * Math.PI;
        double dist = Math.random() * radius;
        double x = centerX + Math.cos(angle) * dist;
        double z = centerZ + Math.sin(angle) * dist;
        return new Location(world, x, centerY, z);
    }

    /**
     * Returns whether this region should attempt a spawn on this tick.
     * Increments the internal counter and resets when the interval is reached.
     */
    public boolean shouldSpawn() {
        tickCounter++;
        if (tickCounter >= spawnIntervalTicks) {
            tickCounter = 0;
            return true;
        }
        return false;
    }
}
