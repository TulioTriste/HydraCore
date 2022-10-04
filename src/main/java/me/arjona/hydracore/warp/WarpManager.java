package me.arjona.hydracore.warp;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Sets;
import com.mongodb.client.MongoCollection;
import lombok.Getter;
import me.arjona.hydracore.Core;
import me.arjona.hydracore.utilities.LocationUtil;
import me.arjona.hydracore.utilities.TaskUtil;
import me.arjona.hydracore.utilities.redis.impl.Payload;
import me.arjona.hydracore.utilities.redis.util.RedisMessage;
import org.bson.Document;
import org.checkerframework.checker.units.qual.K;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
@Getter
public class WarpManager {

    private final MongoCollection<Document> collection = Core.get().getMongoDatabase().getCollection("warps");

    public final Set<Warp> warps = Sets.newConcurrentHashSet();
    private final Cache<UUID, String> cacheBuilder;

    public WarpManager() {
        this.cacheBuilder = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(30))
                .maximumSize(100)
                .build();
        for (Document document : collection.find()) {
            Warp warp = new Warp(document.getString("name"), document.getString("creator"),
                    document.getString("server"), LocationUtil.deserialize(document.getString("location")), "&7" + document.getString("name"));
            warp.setDisplayName(document.getString("displayName"));
            warps.add(warp);
        }
    }

    public void createWarp(Warp warp) {
        TaskUtil.runAsync(warp::save);
        warps.add(warp);

        Core.get().getRedisManager().write(new RedisMessage(Payload.CREATE_WARP)
                .setParam("NAME", warp.getName())
                .setParam("CREATOR", warp.getCreator())
                .setParam("SERVER", warp.getServer())
                .setParam("LOCATION", LocationUtil.serialize(warp.getLocation()))
                .setParam("DISPLAYNAME", warp.getDisplayName())
                .toJSON());
    }

    public void deleteWarp(Warp warp) {
        TaskUtil.runAsync(() -> collection.deleteOne(new Document("name", warp.getName())));
        warps.remove(warp);

        Core.get().getRedisManager().write(new RedisMessage(Payload.DELETE_WARP)
                .setParam("NAME", warp.getName())
                .toJSON());
    }

    public void saveAll() {
        TaskUtil.runAsync(() -> {
            for (Warp warp : warps) {
                warp.save();
            }
        });
    }

    public Warp getByName(String warp) {
        for (Warp warp1 : warps) {
            if (warp1.getName().equalsIgnoreCase(warp)) {
                return warp1;
            }
        }
        return null;
    }
}
