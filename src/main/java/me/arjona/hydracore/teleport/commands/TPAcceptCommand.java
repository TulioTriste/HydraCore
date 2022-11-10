package me.arjona.hydracore.teleport.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.arjona.hydracore.Core;
import me.arjona.hydracore.teleport.TPInfo;
import me.arjona.hydracore.utilities.CC;
import me.arjona.hydracore.utilities.commands.BaseCommand;
import me.arjona.hydracore.utilities.commands.Command;
import me.arjona.hydracore.utilities.commands.CommandArgs;
import me.arjona.hydracore.utilities.redis.impl.Payload;
import me.arjona.hydracore.utilities.redis.util.RedisMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
public class TPAcceptCommand extends BaseCommand {

    @Command(name = "tpaccept")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        TPInfo tpInfo = Core.get().getTeleportManager().getCachePetition().getIfPresent(player.getUniqueId());
        if (tpInfo != null) {
            Core.get().getTeleportManager().getCacheAccept().put(tpInfo.getSenderUUID(),
                    new TPInfo(tpInfo.getSenderName(), tpInfo.getSenderUUID(), player.getName(), player.getUniqueId(), Core.get().getServerName()));

            Player sender = Bukkit.getPlayer(tpInfo.getSenderUUID());
            if (sender != null) {
                sender.sendMessage(CC.translate("&aThe teleport request has been accepted"));
                player.teleport(sender.getLocation());
            } else {
                Core.get().getRedisManager().write(new RedisMessage(Payload.TPA_ACCEPT_REPLICA)
                        .setParam("SENDERNAME", tpInfo.getSenderName())
                        .setParam("SENDERUUID", tpInfo.getSenderUUID().toString())
                        .setParam("TARGETNAME", tpInfo.getTargetName())
                        .setParam("TARGETUUID", tpInfo.getTargetUUID().toString())
                        .setParam("SERVER", tpInfo.getServer())
                        .setParam("TARGETSV", Core.get().getServerName())
                        .toJSON());
            }

            player.sendMessage(CC.translate("&aYou have accepted the teleport request."));
        } else {
            player.sendMessage(CC.translate("&cNo tienes ninguna petición de teleportación pendiente."));
        }
    }
}
