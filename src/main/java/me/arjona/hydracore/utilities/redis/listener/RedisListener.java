package me.arjona.hydracore.utilities.redis.listener;

import com.google.common.cache.CacheLoader;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import me.arjona.hydracore.Core;
import me.arjona.hydracore.teleport.TPInfo;
import me.arjona.hydracore.utilities.CC;
import me.arjona.hydracore.utilities.redis.impl.Payload;
import me.arjona.hydracore.utilities.redis.util.RedisMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.JedisPubSub;

import java.util.UUID;

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
                    plugin.getRedisManager().write(
                            new RedisMessage(Payload.REMOVE_SPAWN_RESPONSE)
                                .setParam("SENDER", redisMessage.getParam("SENDER"))
                                .setParam("SERVER", plugin.getServerName())
                                .toJSON());
                    System.out.println("[HydraCore] The Spawn has been removed by " + redisMessage.getParam("SENDER") + " from " + redisMessage.getParam("SERVER"));
                }
                break;
            }
            case REMOVE_SPAWN_RESPONSE: {
                if (redisMessage.getParam("SERVER").equals(plugin.getServerName())) {
                    Player player = Bukkit.getPlayer(redisMessage.getParam("SENDER"));
                    if (player != null) {
                        player.sendMessage("The Spawn has been removed in " + redisMessage.getParam("SERVER"));
                    }
                }
                break;
            }
            case SEND_SPAWN_REPLICA: {
                if (plugin.getSpawnManager().getSpawnLocation() != null) {
                    plugin.getRedisManager().write(
                            new RedisMessage(Payload.SEND_SPAWN_RESPONSE)
                                    .setParam("SENDER", redisMessage.getParam("SENDER"))
                                    .setParam("SERVERSPAWN", plugin.getServerName())
                                    .setParam("SERVERSEND", redisMessage.getParam("SERVER"))
                                    .toJSON());

                    plugin.getSpawnManager().getCacheBuilder().put("Hydra", redisMessage.getParam("SENDER"));
                }
                break;
            }
            case SEND_SPAWN_RESPONSE: {
                if (redisMessage.getParam("SERVERSEND").equals(plugin.getServerName())) {
                    Player player = Bukkit.getPlayer(redisMessage.getParam("SENDER"));
                    if (player != null) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Connect");
                        out.writeUTF(redisMessage.getParam("SERVERSPAWN"));
                        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                    }
                }
                break;
            }
            case TPA_REPLICA: {
                String server = redisMessage.getParam("SERVER");
                UUID senderUuid = UUID.fromString(redisMessage.getParam("SENDERUUID"));
                String senderName = redisMessage.getParam("SENDERNAME");
                String targetName = redisMessage.getParam("TARGET");
                Player target = Bukkit.getPlayer(targetName);
                if (target != null) {
                    // TPA to another server
                    plugin.getTeleportManager().sendPetition(senderUuid, senderName, target, server);

                    plugin.getRedisManager().write(new RedisMessage(Payload.TPA_RESPONSE)
                            .setParam("SENDERUUID", senderUuid.toString())
                            .setParam("TARGET", targetName)
                            .setParam("SENDSERVER", server)
                            .setParam("TARGETSERVER", plugin.getServerName())
                            .toJSON());

                }
                break;
            }
            case TPA_RESPONSE: {
                if (redisMessage.getParam("SENDSERVER").equals(plugin.getServerName())) {
                    Player player = Bukkit.getPlayer(UUID.fromString(redisMessage.getParam("SENDERUUID")));
                    if (player != null) {
                        plugin.getTeleportManager().sentPetition(player);
                    }
                }
                break;
            }
            case TPA_ACCEPT_REPLICA: {
                if (redisMessage.getParam("SERVER").equals(plugin.getServerName())) {
                    Player player = Bukkit.getPlayer(UUID.fromString(redisMessage.getParam("SENDERUUID")));
                    if (player != null) {
                        plugin.getTeleportManager().getCacheAccept().put(player.getUniqueId(),
                                new TPInfo(redisMessage.getParam("SENDERNAME"),
                                        UUID.fromString(redisMessage.getParam("SENDERUUID")),
                                        redisMessage.getParam("TARGETNAME"),
                                        UUID.fromString(redisMessage.getParam("TARGETUUID")),
                                        redisMessage.getParam("SERVER")));

                        plugin.getRedisManager().write(new RedisMessage(Payload.TPA_ACCEPT_RESPONSE)
                                .setParam("PLAYERUUID", redisMessage.getParam("SENDERUUID"))
                                .setParam("SERVER", redisMessage.getParam("TARGETSV"))
                                .setParam("SENDSERVER", redisMessage.getParam("SERVER"))
                                .setParam("CAN_ACCEPT", "TRUE")
                                .toJSON());
                    } else {
                        plugin.getRedisManager().write(new RedisMessage(Payload.TPA_ACCEPT_RESPONSE)
                                .setParam("PLAYERUUID", redisMessage.getParam("SENDERUUID"))
                                .setParam("TARGETNAME", redisMessage.getParam("TARGETNAME"))
                                .setParam("SERVER", redisMessage.getParam("TARGETSV"))
                                .setParam("SENDSERVER", redisMessage.getParam("SERVER"))
                                .setParam("CANACCEPT", "FALSE")
                                .toJSON());
                    }
                }
                break;
            }
            case TPA_ACCEPT_RESPONSE: {
                if (redisMessage.getParam("SERVER").equals(plugin.getServerName())) {
                    Player player = Bukkit.getPlayer(UUID.fromString(redisMessage.getParam("PLAYERUUID")));
                    if (player != null) {
                        if (redisMessage.getParam("CANACCEPT").equals("FALSE")) {
                            player.sendMessage("§c" + redisMessage.getParam("TARGETNAME") + " is not online");
                        } else {
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("Connect");
                            out.writeUTF(redisMessage.getParam("SENDSERVER"));

                            player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());

                            System.out.println("TPA_ACCEPT_RESPONSE: " + redisMessage.getParam("SENDSERVER"));
                        }
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
