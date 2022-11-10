package me.arjona.hydracore.placeholderapi;

import lombok.RequiredArgsConstructor;
import me.arjona.hydracore.Core;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class PlaceholderAPI extends PlaceholderExpansion {

    private final Core plugin;

    @Override
    public String getIdentifier() {
        return "hydracore";
    }

    @Override
    public String getAuthor() {
        return "Arjona";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        if (p == null) return "";
        if (params.equals("balance")) {
            return String.valueOf(plugin.getProfileManager().getProfile(p.getUniqueId()).getBalance());
        }
        return super.onPlaceholderRequest(p, params);
    }
}
