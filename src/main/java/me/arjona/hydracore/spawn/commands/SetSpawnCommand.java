package me.arjona.hydracore.spawn.commands;

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
public class SetSpawnCommand extends BaseCommand {

    @Command(name = "setspawn", permission = "hydracore.command.setspawn")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Core.get().getSpawnManager().set(player.getLocation());
        player.sendMessage(CC.translate("&cSpawn seteado correctamente!"));
    }
}
