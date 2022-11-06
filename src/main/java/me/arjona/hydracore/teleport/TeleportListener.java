package me.arjona.hydracore.teleport;

import me.arjona.hydracore.Core;
import org.bukkit.Bukkit;
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
public class TeleportListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        TPInfo tpInfo = Core.get().getTeleportManager().getCacheAccept().getIfPresent(player.getUniqueId());
        if (tpInfo != null) {
            System.out.println("TPInfo");
            Player target = Bukkit.getPlayer(tpInfo.getTargetUUID());
            if (target != null) {
                System.out.println("Teleporting " + player.getName() + " to " + target.getName());
                player.teleport(target.getLocation());
            }
        }
    }
}
