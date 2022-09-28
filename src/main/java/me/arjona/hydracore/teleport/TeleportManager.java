package me.arjona.hydracore.teleport;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import me.arjona.hydracore.Core;
import me.arjona.hydracore.utilities.CC;
import me.arjona.hydracore.utilities.TaskUtil;
import me.arjona.hydracore.utilities.redis.impl.Payload;
import me.arjona.hydracore.utilities.redis.util.RedisMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.UUID;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
@Getter
public class TeleportManager {

    private final Cache<UUID, TPInfo> cachePetition;
    private final Cache<UUID, TPInfo> cacheAccept;

    public TeleportManager() {
        this.cachePetition = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(30))
                /*.expireAfterAccess(Duration.ofSeconds(1))*/
                .maximumSize(10)
                .build();
        this.cacheAccept = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(10))
                /*.expireAfterAccess(Duration.ofSeconds(1))*/
                .maximumSize(1)
                .build();

        /*cachePetition.put("test", new TPInfo("test", UUID.randomUUID(), "testtarget", UUID.randomUUID(), "testserver"));

        TaskUtil.runTimer(() -> {
            System.out.println(cachePetition.size());
            System.out.println(cachePetition.getIfPresent("test").getSenderName());
            cachePetition.asMap().forEach((key, value) -> {
                Bukkit.broadcastMessage("Key: " + key + " Value: " + value);
            });
        }, 20L, 20L);*/
    }

    public void sendPetition(UUID senderUUID, String senderName, Player target, String server) {
        cachePetition.put(target.getUniqueId(), new TPInfo(senderName, senderUUID, target.getName(), target.getUniqueId(), server));

        target.sendMessage(CC.translate("&a" + senderName + " &7te ha enviado una petición de teleportación, escribe &a/tpaccept §7para aceptarla."));
    }
    
    public void sentPetition(Player target) {
        target.sendMessage(CC.translate("&cHas enviado una petición de teleportación."));
    }
}
