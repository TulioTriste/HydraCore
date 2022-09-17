package me.arjona.hydracore.spawn;

import lombok.Getter;
import me.arjona.hydracore.Core;
import me.arjona.hydracore.utilities.LocationUtil;
import org.bukkit.Location;

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

    public SpawnManager() {
        if (Core.get().getMainConfig().getConfiguration().get("SPAWN_LOCATION") != null) {
            spawnLocation = LocationUtil.deserialize(Core.get().getMainConfig().getString("SPAWN_LOCATION"));
        }
    }

    public void set(Location location) {
        Core.get().getMainConfig().getConfiguration().set("SPAWN_LOCATION", LocationUtil.serialize(location));
        spawnLocation = location;
    }

    public void remove() {
        Core.get().getMainConfig().getConfiguration().set("SPAWN_LOCATION", null);
        spawnLocation = null;
    }
}
