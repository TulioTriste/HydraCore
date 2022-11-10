package me.arjona.hydracore.utilities;

import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.arjona.hydracore.Core;
import me.arjona.hydracore.utilities.redis.impl.Payload;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public class TeleportUtil {

    @Getter private final Map<UUID, Location> teleportMap = Maps.newHashMap();
    private final Core plugin = Core.get();
    private final int teleportDelay = plugin.getMainConfig().getInt("TELEPORT_DELAY");

    public void addTeleport(Player player, Location location) {
        teleportMap.put(player.getUniqueId(), location);

        // Teleport directly if they can bypass
        if (player.hasPermission("hydracore.teleport.bypass")) {
            player.teleport(location);
            player.sendMessage(CC.translate("&aYou have been teleported to your location."));
            return;
        }

        AtomicInteger seconds = new AtomicInteger(teleportDelay);
        Bukkit.getScheduler().runTaskTimer(plugin, task -> {
            if (!teleportMap.containsKey(player.getUniqueId())) {
                task.cancel();
                return;
            }
            if (seconds.get() == 0) {
                task.cancel();
                teleportMap.remove(player.getUniqueId());
                player.teleport(location);
                player.sendMessage(CC.translate("&aYou have been teleported to your location."));
                return;
            }

            if (seconds.get() == teleportDelay) {
                player.sendMessage(CC.translate("&aYou will be teleported to your location in &e" + seconds.get() + " &aseconds. " +
                        "Please dont move, if you move the teleport has been cancelled."));
            } else {
                player.sendMessage(CC.translate("&7Teleporting in &e" + seconds.get() + " &7seconds."));
            }
            seconds.decrementAndGet();
        }, 0L, 20L);
    }

    public void addTeleport(Player player, String s, boolean isServer) {
        teleportMap.put(player.getUniqueId(), null);

        // Teleport directly if they can bypass
        if (player.hasPermission("hydracore.teleport.bypass")) {
            if (isServer) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF(s);
                player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
            } else {
                plugin.getRedisManager().write(s);
            }
            return;
        }

        AtomicInteger seconds = new AtomicInteger(teleportDelay);
        Bukkit.getScheduler().runTaskTimer(plugin, task -> {
            if (!teleportMap.containsKey(player.getUniqueId())) {
                task.cancel();
                return;
            }
            if (seconds.get() == 0) {
                task.cancel();
                teleportMap.remove(player.getUniqueId());
                player.sendMessage(CC.translate("&aYou have been teleported to your location."));
                if (isServer) {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Connect");
                    out.writeUTF(s);

                    player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                }
                else plugin.getRedisManager().write(s);
                return;
            }

            if (seconds.get() == teleportDelay) {
                player.sendMessage(CC.translate("&aYou will be teleported to your location in &e" + seconds.get() + " &aseconds. " +
                        "Please dont move, if you move the teleport has been cancelled."));
            } else {
                player.sendMessage(CC.translate("&7Teleporting in &e" + seconds.get() + " &7seconds."));
            }
            seconds.decrementAndGet();
        }, 0L, 20L);
    }
}
