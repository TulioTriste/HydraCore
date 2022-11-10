package me.arjona.hydracore.profile.balance.sub;

import me.arjona.hydracore.Core;
import me.arjona.hydracore.profile.Profile;
import me.arjona.hydracore.utilities.CC;
import me.arjona.hydracore.utilities.commands.BaseCommand;
import me.arjona.hydracore.utilities.commands.Command;
import me.arjona.hydracore.utilities.commands.CommandArgs;
import org.bukkit.entity.Player;

public class BalanceAddSubCommand extends BaseCommand {

    @Command(name = "balance.add", aliases = {"bal.add", "money.add"}, permission = "hydracore.balance.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0 || args.length == 1) {
            player.sendMessage(CC.translate("Usage: /" + command.getLabel() + " <player> <amount>"));
            return;
        }

        Profile target = Core.get().getProfileManager().getOfflineProfile(args[0]);
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

        if (amount < 1) {
            player.sendMessage(CC.translate("&cYou cant set a negative or null balance."));
            return;
        }

        target.setVaultBalance(amount, "&aYou have added {amount} of balance to {player_name}", false);
    }
}
