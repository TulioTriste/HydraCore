package me.arjona.hydracore.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
@UtilityClass
public class LocationUtil {

    public String serialize(Location location) {
        if (location == null) {
            return "null";
        }

        return Objects.requireNonNull(location.getWorld()).getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() +
                ":" + location.getYaw() + ":" + location.getPitch();
    }

    public Location deserialize(String source) {
        if (source == null) {
            return null;
        }

        String[] split = source.split(":");
        World world = Bukkit.getServer().getWorld(split[0]);

        if (world == null) {
            return null;
        }

        return new Location(world, Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
    }
}
