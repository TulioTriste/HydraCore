package me.arjona.hydracore.warp.commands;

import com.google.common.collect.Maps;
import me.arjona.hydracore.Core;
import me.arjona.hydracore.utilities.CC;
import me.arjona.hydracore.utilities.commands.BaseCommand;
import me.arjona.hydracore.utilities.commands.Command;
import me.arjona.hydracore.utilities.commands.CommandArgs;
import org.bukkit.entity.Player;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
public class DeleteWarpCommand extends BaseCommand {

    @Command(name = "deletewarp", permission = "hydracore.command.deletewarp")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /" + command.getLabel() + " <name>"));
            return;
        }

        String name = args[0];

        if (Core.get().getWarpManager().getByName(name) == null) {
            player.sendMessage(CC.translate("&cA warp with that name doesn't exists."));
            return;
        }

        Core.get().getWarpManager().deleteWarp(Core.get().getWarpManager().getByName(name));
        player.sendMessage(CC.translate("&cWarp deleted!"));

        CC.redisLog("&7El Player &a" + player.getName() + " &7ha eliminado el Warp &a" + name, Maps.newHashMap());
    }
}
