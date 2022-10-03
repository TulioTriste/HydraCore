package me.arjona.hydracore.profile.balance;

import me.arjona.hydracore.Core;
import me.arjona.hydracore.profile.Profile;
import me.arjona.hydracore.utilities.CC;
import me.arjona.hydracore.utilities.commands.BaseCommand;
import me.arjona.hydracore.utilities.commands.Command;
import me.arjona.hydracore.utilities.commands.CommandArgs;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
public class DepositCommand extends BaseCommand {

    @Command(name = "deposit", aliases = {"pay"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();
        Profile profile = Core.get().getProfileManager().getProfile(player.getUniqueId());

        if (args.length == 0 || args.length == 1) {
            player.sendMessage(CC.translate("&cUsage: /" + command.getLabel() + " <player> <amount>"));
            return;
        }

        Player target = Core.get().getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cInvalid number."));
            return;
        }

        if (amount <= 0) {
            player.sendMessage(CC.translate("&cInvalid number."));
            return;
        }

        if (profile.getBalance() < amount) {
            player.sendMessage(CC.translate("&cYou don't have enough money."));
            return;
        }

        profile.setBalance(profile.getBalance() - amount, "&aYou have deposited {amount} to " + target.getName());

        Profile targetProfile = Core.get().getProfileManager().getProfile(target.getUniqueId());
        targetProfile.setBalance(targetProfile.getBalance()+amount, "&aYou have received {amount} from " + player.getName());
    }
}
