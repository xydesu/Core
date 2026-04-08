package me.xydesu.core.Mob;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Central registry for all {@link Region} spawn zones.
 *
 * <p>Add entries to the static initialiser block to define regions.
 * The {@link me.xydesu.core.Tasks.MobSpawnTask} reads this registry
 * to know where and what to spawn.
 */
public class RegionManager {

    private static final List<Region> regions = new ArrayList<>();

    static {
        // Example region: spawn SuperZombies near world origin
        regions.add(new Region(
                "overworld_zombies",   // regionId
                "world",               // worldName  (must match server world name)
                0, 64, 0,              // center X, Y, Z
                30,                    // radius (blocks)
                "SUPER_ZOMBIE",        // mobId (matches CustomMob.getID())
                5,                     // maxMobs concurrently in this region
                10                     // spawn interval (task runs between attempts)
        ));
    }

    public static List<Region> getRegions() {
        return Collections.unmodifiableList(regions);
    }

    /**
     * Registers a new spawn region at runtime.
     *
     * @param region the region to add
     */
    public static void register(Region region) {
        regions.add(region);
    }
}
