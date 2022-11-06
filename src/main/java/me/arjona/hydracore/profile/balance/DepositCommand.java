package me.arjona.hydracore.profile.balance;

import me.arjona.hydracore.Core;
import me.arjona.hydracore.profile.Profile;
import me.arjona.hydracore.utilities.CC;
import me.arjona.hydracore.utilities.TaskUtil;
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

        Player target = Core.get().getServer().getPlayer(args[0]);
        if (target == null) {
            profile.setWaitingDepositResponse(true);
            Core.get().getRedisManager().write(new RedisMessage(Payload.DEPOSIT_REPLICA)
                    .setParam("SERVER", Core.get().getServerName())
                    .setParam("PLAYER", player.getName())
                    .setParam("TARGET", args[0])
                    .setParam("AMOUNT", String.valueOf(amount))
                    .toJSON());

            TaskUtil.runLater(() -> {
                if (profile.isWaitingDepositResponse()) {
                    profile.setWaitingDepositResponse(false);
                    player.sendMessage(CC.translate("&cPlayer not found."));
                }
            }, 20L);
        } else {
            if (profile.getBalance() < amount) {
                player.sendMessage(CC.translate("&cYou don't have enough money."));
                return;
            }

            profile.setBalance(-amount, "&aYou have deposited {amount} to " + target.getName(), false);

            Profile targetProfile = Core.get().getProfileManager().getProfile(target.getUniqueId());
            targetProfile.setBalance(amount, "&aYou have received {amount} from " + player.getName(), false);
        }
    }
}
