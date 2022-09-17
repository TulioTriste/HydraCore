package me.arjona.hydracore.profile;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
public class ProfileManager {

    @Getter public Map<UUID, Profile> profiles = Maps.newHashMap();

    public Profile getProfile(UUID uuid) {
        return profiles.get(uuid);
    }

    public Profile getProfile(String name) {
        for (Profile profile : profiles.values()) {
            if (profile.getName().equalsIgnoreCase(name)) return profile;
        }
        return null;
    }
}
