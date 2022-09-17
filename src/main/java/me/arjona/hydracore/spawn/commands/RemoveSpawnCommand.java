package me.arjona.hydracore.spawn.commands;

import me.arjona.hydracore.Core;
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
public class RemoveSpawnCommand extends BaseCommand {

    @Command(name = "removespawn", permission = "hydracore.command.removespawn")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (Core.get().getSpawnManager().getSpawnLocation() != null) {
            Core.get().getSpawnManager().remove();
            player.sendMessage("Spawn removed!");
            return;
        }

        String json = new RedisMessage(Payload.REMOVE_SPAWN_REPLICA)
                .setParam("SENDER", player.getName())
                .toJSON();
        Core.get().getRedisManager().write(json);
        player.sendMessage("Spawn removed!");
    }
}
