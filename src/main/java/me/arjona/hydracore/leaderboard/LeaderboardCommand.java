package me.arjona.hydracore.leaderboard;

import me.arjona.hydracore.Core;
import me.arjona.hydracore.profile.Profile;
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
public class LeaderboardCommand extends BaseCommand {

    @Command(name = "baltop", aliases = {"topbal", "balancetop", "topbalance"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate("&2&lBalance Leaderboard"));
        player.sendMessage("");
        for (Profile profile : Core.get().getLeaderboardManager().getTop()) {
            player.sendMessage(CC.translate("&2" + profile.getName() + " &7- &a$&7" + profile.getBalance()));
        }
        player.sendMessage(CC.CHAT_BAR);
    }
}
