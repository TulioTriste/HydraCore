package me.arjona.hydracore.profile.balance;

import me.arjona.hydracore.Core;
import me.arjona.hydracore.profile.Profile;
import me.arjona.hydracore.utilities.CC;
import me.arjona.hydracore.utilities.commands.BaseCommand;
import me.arjona.hydracore.utilities.commands.Command;
import me.arjona.hydracore.utilities.commands.CommandArgs;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        double response = Core.get().getEcon().getBalance(target);
        player.sendMessage(CC.translate("&a" + target.getName() + "'s balance is: " + response));
    }
}
