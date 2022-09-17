package me.arjona.hydracore.utilities.redis.listener;

import com.google.common.cache.CacheLoader;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import me.arjona.hydracore.Core;
import me.arjona.hydracore.utilities.redis.impl.Payload;
import me.arjona.hydracore.utilities.redis.util.RedisMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.JedisPubSub;

public class RedisListener extends JedisPubSub {

    private final Core plugin = Core.get();

    @Override
    public void onMessage(String channel, String message) {
        RedisMessage redisMessage = new Gson().fromJson(message, RedisMessage.class);
        /*
        A switch is made to assign the action for each type of Payload, it can also be done by "if"
         */
        switch (redisMessage.getPayload()) {
            case REMOVE_SPAWN_REPLICA: {
                if (plugin.getSpawnManager().getSpawnLocation() != null) {
                    plugin.getSpawnManager().remove();
                    Core.get().getRedisManager().write(
                            new RedisMessage(Payload.REMOVE_SPAWN_RESPONSE)
                                .setParam("SENDER", redisMessage.getParam("SENDER"))
                                .setParam("SERVER", Core.get().getServerName())
                                .toJSON());
                    System.out.println("[HydraCore] The Spawn has been removed by " + redisMessage.getParam("SENDER") + " from " + redisMessage.getParam("SERVER"));
                }
                break;
            }
            case REMOVE_SPAWN_RESPONSE: {
                if (redisMessage.getParam("SERVER").equals(Core.get().getServerName())) {
                    Player player = Bukkit.getPlayer(redisMessage.getParam("SENDER"));
                    if (player != null) {
                        player.sendMessage("The Spawn has been removed in " + redisMessage.getParam("SERVER"));
                    }
                }
                break;
            }
            case SEND_SPAWN_REPLICA: {
                if (plugin.getSpawnManager().getSpawnLocation() != null) {
                    Core.get().getRedisManager().write(
                            new RedisMessage(Payload.SEND_SPAWN_RESPONSE)
                                    .setParam("SENDER", redisMessage.getParam("SENDER"))
                                    .setParam("SERVER_SPAWN", Core.get().getServerName())
                                    .setParam("SERVER_SEND", redisMessage.getParam("SERVER"))
                                    .toJSON());

                    Core.get().getSpawnManager().getCacheBuilder().put("Hydra", redisMessage.getParam("SENDER"));
                }
                break;
            }
            case SEND_SPAWN_RESPONSE: {
                if (redisMessage.getParam("SERVER_SEND").equals(Core.get().getServerName())) {
                    Player player = Bukkit.getPlayer(redisMessage.getParam("SENDER"));
                    if (player != null) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Connect");
                        out.writeUTF(redisMessage.getParam("SERVER_SPAWN"));
                        player.sendPluginMessage(Core.get(), "BungeeCord", out.toByteArray());
                    }
                }
                break;
            }
            default: {
                plugin.getLogger().info("[Redis] The message was received, but there was no response");
            }
            break;
        }
    }
}
