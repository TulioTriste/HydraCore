package me.arjona.hydracore.teleport.commands;

import me.arjona.hydracore.Core;
import me.arjona.hydracore.utilities.CC;
import me.arjona.hydracore.utilities.commands.BaseCommand;
import me.arjona.hydracore.utilities.commands.Command;
import me.arjona.hydracore.utilities.commands.CommandArgs;
import me.arjona.hydracore.utilities.redis.impl.Payload;
import me.arjona.hydracore.utilities.redis.util.RedisMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
public class TPACommand extends BaseCommand {

    @Command(name = "tpa", permission = "hydracore.command.tpa")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /tpa <player>"));
            return;
        }

        if (args[0].equalsIgnoreCase(player.getName())) {
            player.sendMessage(CC.translate("&cYou can't teleport to yourself."));
            return;
        }

        Core.get().getRedisManager().write(
                new RedisMessage(Payload.TPA_REPLICA)
                .setParam("SENDERNAME", player.getName())
                .setParam("SENDERUUID", player.getUniqueId().toString())
                .setParam("TARGET", args[0])
                .setParam("SERVER", Core.get().getServerName())
                .toJSON());
    }
}
