package me.arjona.hydracore.spawn;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import lombok.Getter;
import me.arjona.hydracore.Core;
import me.arjona.hydracore.utilities.LocationUtil;
import org.bukkit.Location;

import java.time.Duration;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
@Getter
public class SpawnManager {

    private Location spawnLocation;
    private final Cache<String, String> cacheBuilder;

    public SpawnManager() {
        this.cacheBuilder = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(30))
                .expireAfterAccess(Duration.ofSeconds(1))
                .maximumSize(100)
                .build();
        if (Core.get().getMainConfig().getConfiguration().get("SPAWN_LOCATION") != null) {
            spawnLocation = LocationUtil.deserialize(Core.get().getMainConfig().getString("SPAWN_LOCATION"));
        }
    }

    public void set(Location location) {
        Core.get().getMainConfig().getConfiguration().set("SPAWN_LOCATION", LocationUtil.serialize(location));
        Core.get().getMainConfig().save();
        spawnLocation = location;
    }

    public void remove() {
        Core.get().getMainConfig().getConfiguration().set("SPAWN_LOCATION", null);
        Core.get().getMainConfig().save();
        spawnLocation = null;
    }
}
