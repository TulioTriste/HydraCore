package me.arjona.hydracore.leaderboard;

import com.google.common.collect.Sets;
import lombok.Getter;
import me.arjona.hydracore.Core;
import me.arjona.hydracore.profile.Profile;
import me.arjona.hydracore.utilities.TaskUtil;
import org.bson.Document;

import java.util.*;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
@Getter
public class LeaderboardManager {

    private Set<Profile> profiles = Sets.newConcurrentHashSet();
    private final LinkedList<Profile> top = new LinkedList<>();

    public LeaderboardManager() {
        TaskUtil.runTimerAsync(() -> {
            profiles = getAllProfiles();
        }, 100, 20*60*5);

        TaskUtil.runTimerAsync(() -> {
            profiles.stream().sorted((o1, o2) -> Integer.compare(o2.getBalance(), o1.getBalance())).limit(10).forEach(top::add);
        }, 300, 20*60*5);
    }

    private Set<Profile> getAllProfiles() {
        Set<Profile> profiles = Sets.newConcurrentHashSet();

        for (Document document : Core.get().getProfileManager().getCollection().find()) {
            Profile profile = new Profile(UUID.fromString(document.getString("uuid")), document.getString("name"));
            profile.load();
            profiles.add(profile);
        }

        return profiles;
    }
}
