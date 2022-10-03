package me.arjona.hydracore.leaderboard;

import me.arjona.hydracore.Core;
import me.arjona.hydracore.profile.Profile;
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
public class TestCommand extends BaseCommand {

    @Command(name = "test")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        for (Profile profile : Core.get().getLeaderboardManager().getTop()) {
            player.sendMessage("§a" + profile.getName() + " §7- §a" + profile.getBalance());
        }
    }
}
