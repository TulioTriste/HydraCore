package me.arjona.hydracore.profile.balance;

import me.arjona.hydracore.Core;
import me.arjona.hydracore.profile.Profile;
import me.arjona.hydracore.utilities.CC;
import me.arjona.hydracore.utilities.commands.BaseCommand;
import me.arjona.hydracore.utilities.commands.Command;
import me.arjona.hydracore.utilities.commands.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
public class BalanceCommand extends BaseCommand {

    @Command(name = "balance", aliases = {"bal", "money"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();
        Profile profile = Core.get().getProfileManager().getProfile(player.getUniqueId());

        if (args.length == 0) {
            player.sendMessage(CC.translate("&aYour balance is: " + profile.getBalance()));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target != null) {
            Profile targetProfile = Core.get().getProfileManager().getProfile(target.getUniqueId());
            player.sendMessage(CC.translate("&a" + target.getName() + "'s balance is: " + targetProfile.getBalance()));
        } else {
            Profile targetProfile = Core.get().getProfileManager().getOfflineProfile(args[0]);
            if (targetProfile != null) {
                player.sendMessage(CC.translate("&a" + targetProfile.getName() + "'s balance is: " + targetProfile.getBalance()));
            } else {
                player.sendMessage(CC.translate("&cPlayer not found."));
            }
        }
    }
}
