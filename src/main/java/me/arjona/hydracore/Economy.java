package me.arjona.hydracore;

import lombok.RequiredArgsConstructor;
import me.arjona.hydracore.utilities.MoneyUtil;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.util.List;

@RequiredArgsConstructor
public class Economy implements net.milkbowl.vault.economy.Economy {

    private final Core plugin;

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "hydracore";
    }

    @Override
    public String format(double amount) {
        return MoneyUtil.format(amount);
    }

    @Override
    public double getBalance(String playerName) {
        return plugin.getProfileManager().getProfile(playerName).getBalance();
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return plugin.getProfileManager().getOfflineProfile(player.getUniqueId()).getBalance();
    }

    @Override
    public double getBalance(String playerName, String world) {
        return plugin.getProfileManager().getProfile(playerName).getBalance();
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return plugin.getProfileManager().getOfflineProfile(player.getUniqueId()).getBalance();
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return plugin.getProfileManager().getProfile(playerName).withdraw(amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return plugin.getProfileManager().getOfflineProfile(player.getUniqueId()).withdraw(amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return plugin.getProfileManager().getProfile(playerName).withdraw(amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return plugin.getProfileManager().getOfflineProfile(player.getUniqueId()).withdraw(amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return plugin.getProfileManager().getProfile(playerName).deposit(amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return plugin.getProfileManager().getOfflineProfile(player.getUniqueId()).deposit(amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return plugin.getProfileManager().getProfile(playerName).deposit(amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return plugin.getProfileManager().getOfflineProfile(player.getUniqueId()).deposit(amount);
    }

    // Unused methods

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String currencyNamePlural() {
        return null;
    }

    @Override
    public String currencyNameSingular() {
        return null;
    }

    @Override
    public boolean hasAccount(String playerName) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return false;
    }

    @Override
    public boolean has(String playerName, double amount) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return false;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return false;
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return false;
    }
}
