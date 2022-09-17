package me.arjona.hydracore.spawn;

import me.arjona.hydracore.Core;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
public class SpawnListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (Core.get().getSpawnManager().getSpawnLocation() != null
                && Core.get().getSpawnManager().getCacheBuilder().getIfPresent("Hydra") != null
                && Objects.equals(Core.get().getSpawnManager().getCacheBuilder().getIfPresent("Hydra"), player.getName())) {
            player.teleport(Core.get().getSpawnManager().getSpawnLocation());
        }
    }
}
