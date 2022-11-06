package me.arjona.hydracore.profile;

import me.arjona.hydracore.Core;
import me.arjona.hydracore.utilities.CC;
import me.arjona.hydracore.utilities.TaskUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
public class ProfileListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
            if (!Core.get().getProfileManager().getProfiles().containsKey(event.getPlayer().getUniqueId())) {
                Profile profile = new Profile(event.getPlayer().getUniqueId(), event.getPlayer().getName());
                try {
                    // Load Profile
                    profile.load();
                } catch (Exception e) {
                    Core.get().getProfileManager().getProfiles().remove(event.getPlayer().getUniqueId());
                    event.disallow(
                            PlayerLoginEvent.Result.KICK_OTHER,
                            CC.translate("&cAn error occurred while loading your profile. Please try again later."));
                    throw new IllegalArgumentException("Player Profile could not be created, contact Arjona#0643");
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = Core.get().getProfileManager().getProfile(player.getUniqueId());
        profile.setBalance(profile.getBalance(), null, true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = Core.get().getProfileManager().getProfile(player.getUniqueId());
        profile.setOnline(false);
        // SAVE
        TaskUtil.runAsync(profile::save);

        Core.get().getProfileManager().getProfiles().remove(player.getUniqueId());
    }
}
