package me.arjona.hydracore.teleport;

import me.arjona.hydracore.Core;
import me.arjona.hydracore.utilities.CC;
import me.arjona.hydracore.utilities.TeleportUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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
            Player target = Bukkit.getPlayer(tpInfo.getTargetUUID());
            if (target != null) {
                player.teleport(target.getLocation());
            }
        }
    }

    @EventHandler
    public void onTryMove(PlayerMoveEvent event) {
        if (TeleportUtil.getTeleportMap().containsKey(event.getPlayer().getUniqueId())) {
            Player player = event.getPlayer();
            TeleportUtil.getTeleportMap().remove(player.getUniqueId());
            player.sendMessage(CC.translate("&cYou have moved, teleport cancelled."));
        }
    }
}
