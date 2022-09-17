package me.arjona.hydracore.profile;

import lombok.Getter;
import lombok.Setter;
import me.arjona.hydracore.Core;

import java.util.UUID;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
@Getter @Setter
public class Profile {

    private final UUID uuid;
    private final String name;
    private boolean online = false;

    public Profile(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

        Core.get().getProfileManager().getProfiles().put(uuid, this);
    }
}
