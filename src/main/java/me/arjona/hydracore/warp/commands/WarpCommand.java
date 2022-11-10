package me.arjona.hydracore.warp.commands;

import me.arjona.hydracore.Core;
import me.arjona.hydracore.utilities.CC;
import me.arjona.hydracore.utilities.TeleportUtil;
import me.arjona.hydracore.utilities.commands.BaseCommand;
import me.arjona.hydracore.utilities.commands.Command;
import me.arjona.hydracore.utilities.commands.CommandArgs;
import me.arjona.hydracore.utilities.redis.impl.Payload;
import me.arjona.hydracore.utilities.redis.util.RedisMessage;
import me.arjona.hydracore.warp.Warp;
import org.bukkit.entity.Player;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
public class WarpCommand extends BaseCommand {

    @Command(name = "warp", permission = "hydracore.command.warp")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage("Usage: /" + command.getLabel() + " <name>");
            return;
        }

        if (args[0].equalsIgnoreCase("list")) {
            player.sendMessage(CC.translate("&2&lWarp list"));
            player.sendMessage(CC.CHAT_BAR);
            for (Warp warp : Core.get().getWarpManager().getWarps()) {
                player.sendMessage(CC.translate(" &7- &a" + warp.getName() + " &7(&a" + warp.getServer() + "&7)"));
            }
            player.sendMessage(CC.CHAT_BAR);
            return;
        }

        String name = args[0];
        Warp warp = Core.get().getWarpManager().getByName(name);
        if (warp == null) {
            player.sendMessage(CC.translate("&cA warp with that name doesn't exists."));
            return;
        }

        if (warp.getServer().equals(Core.get().getServerName())) {
            player.sendMessage(CC.translate("&aTeleporting to &e" + warp.getName() + "&a."));
            TeleportUtil.addTeleport(player, warp.getLocation());
        } else {
            Core.get().getRedisManager().write(new RedisMessage(Payload.WARP_TELEPORT_REPLICA)
                    .setParam("SENDSERVER", Core.get().getServerName())
                    .setParam("WARPNAME", warp.getName())
                    .setParam("SERVER", warp.getServer())
                    .setParam("PLAYERUUID", player.getUniqueId().toString())
                    .toJSON());
        }
    }
}
