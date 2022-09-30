package me.arjona.hydracore.profile;

import com.google.common.collect.Maps;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import me.arjona.hydracore.Core;
import me.arjona.hydracore.profile.task.ProfilesThread;
import org.bson.Document;

import java.util.Map;
import java.util.UUID;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
@Getter
public class ProfileManager {

    private final MongoCollection<Document> collection = Core.get().getMongoDatabase().getCollection("profiles");
    private final Map<UUID, Profile> profiles = Maps.newHashMap();

    public ProfileManager() {
        // Save Profiles
        new ProfilesThread().start();
    }

    public Profile getProfile(UUID uuid) {
        return profiles.get(uuid);
    }

    public Profile getProfile(String name) {
        for (Profile profile : profiles.values()) {
            if (profile.getName().equalsIgnoreCase(name)) return profile;
        }
        return null;
    }

    public Profile getOfflineProfile(UUID uuid) {
        Document document = collection.find(Filters.eq("uuid", uuid.toString())).first();
        if (document == null) return null;
        Profile profile = new Profile(uuid, document.getString("name"));
        profile.load();
        return profile;
    }

    public Profile getOfflineProfile(String name) {
        Document document = collection.find(Filters.eq("name", name)).first();
        if (document == null) return null;
        Profile profile = new Profile(UUID.fromString(document.getString("uuid")), name);
        profile.load();
        return profile;
    }

    public void saveAll() {
        for (Profile value : profiles.values()) {
            value.save();
        }
    }
}
