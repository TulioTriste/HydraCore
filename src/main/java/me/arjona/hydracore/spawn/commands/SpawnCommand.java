package me.arjona.hydracore.spawn.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.arjona.hydracore.Core;
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
public class SpawnCommand extends BaseCommand {

    @Command(name = "spawn", permission = "hydracore.command.spawn")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        /*ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF("Survi1");
        player.sendPluginMessage(Core.get(), "BungeeCord", out.toByteArray());*/

        if (Core.get().getSpawnManager().getSpawnLocation() != null) {

        }
    }
}
