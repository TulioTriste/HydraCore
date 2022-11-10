package me.arjona.hydracore.spawn.commands;

import me.arjona.hydracore.Core;
import me.arjona.hydracore.utilities.TeleportUtil;
import me.arjona.hydracore.utilities.commands.BaseCommand;
import me.arjona.hydracore.utilities.commands.Command;
import me.arjona.hydracore.utilities.commands.CommandArgs;
import me.arjona.hydracore.utilities.redis.impl.Payload;
import me.arjona.hydracore.utilities.redis.util.RedisMessage;
import org.bukkit.entity.Player;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
public class SpawnCommand extends BaseCommand {

    @Command(name = "spawn", permission = "hydracore.command.spawn")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (Core.get().getSpawnManager().getSpawnLocation() != null) {
            TeleportUtil.addTeleport(player, Core.get().getSpawnManager().getSpawnLocation());
            /*player.teleport(Core.get().getSpawnManager().getSpawnLocation());*/
            return;
        }

        String json = new RedisMessage(Payload.SEND_SPAWN_REPLICA)
                .setParam("SENDER", player.getName())
                .setParam("SERVER", Core.get().getServerName())
                .toJSON();
        TeleportUtil.addTeleport(player, json, false);
    }
}
