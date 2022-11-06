package me.arjona.hydracore.profile.balance;

import me.arjona.hydracore.Core;
import me.arjona.hydracore.profile.Profile;
import me.arjona.hydracore.utilities.CC;
import me.arjona.hydracore.utilities.commands.BaseCommand;
import me.arjona.hydracore.utilities.commands.Command;
import me.arjona.hydracore.utilities.commands.CommandArgs;
import me.arjona.hydracore.utilities.redis.impl.Payload;
import me.arjona.hydracore.utilities.redis.util.RedisMessage;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
public class SetSubCommand extends BaseCommand {

    @Command(name = "balance.set", aliases = {"bal.set", "money.set"}, permission = "hydracore.balance.admin")
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

        if (amount < 0) {
            player.sendMessage(CC.translate("&cYou cant set a negative balance."));
            return;
        }

        target.setBalance(amount, "&aYou have set {player_name}'s balance to {amount}", true);
    }
}
