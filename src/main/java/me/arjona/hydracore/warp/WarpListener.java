package me.arjona.hydracore.warp;

import me.arjona.hydracore.Core;
import me.arjona.hydracore.utilities.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
public class WarpListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (Core.get().getWarpManager().getCacheBuilder().getIfPresent(player.getUniqueId()) != null) {
            Warp warp = Core.get().getWarpManager().getByName(Core.get().getWarpManager().getCacheBuilder().getIfPresent(player.getUniqueId()));
            player.teleport(warp.getLocation());
            player.sendMessage(CC.translate("&aTeleported to &e" + warp.getName() + "&a."));
        }
    }
}
