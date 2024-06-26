package me.arjona.hydracore.utilities.redis.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import me.arjona.hydracore.Core;
import me.arjona.hydracore.profile.Profile;
import me.arjona.hydracore.utilities.CC;
import me.arjona.hydracore.utilities.LocationUtil;
import me.arjona.hydracore.utilities.TeleportUtil;
import me.arjona.hydracore.utilities.redis.impl.Payload;
import me.arjona.hydracore.utilities.redis.util.RedisMessage;
import me.arjona.hydracore.warp.Warp;
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

                    plugin.getSpawnManager().getCacheBuilder().put(redisMessage.getParam("SENDER"), redisMessage.getParam("SERVER"));
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
                String targetName = redisMessage.getParam("TARGET");
                Player target = Bukkit.getPlayer(targetName);
                if (target != null) {
                    String server = redisMessage.getParam("SERVER");
                    UUID senderUuid = UUID.fromString(redisMessage.getParam("SENDERUUID"));
                    String senderName = redisMessage.getParam("SENDERNAME");
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
                        player.sendMessage(CC.translate("&aThe teleport request has been accepted"));

                        TeleportUtil.addTeleport(player, redisMessage.getParam("TARGETSV"), true);
                    } else {
                        plugin.getRedisManager().write(new RedisMessage(Payload.TPA_ACCEPT_RESPONSE_DENIED)
                                .setParam("PLAYERUUID", redisMessage.getParam("SENDERUUID"))
                                .setParam("TARGETNAME", redisMessage.getParam("TARGETNAME"))
                                .setParam("SERVER", redisMessage.getParam("TARGETSV"))
                                .toJSON());
                    }
                }
                break;
            }
            case TPA_ACCEPT_RESPONSE_DENIED: {
                if (redisMessage.getParam("SERVER").equals(plugin.getServerName())) {
                    Player player = Bukkit.getPlayer(UUID.fromString(redisMessage.getParam("PLAYERUUID")));
                    if (player != null) {
                        player.sendMessage("§c" + redisMessage.getParam("TARGETNAME") + " is not online");
                    }
                }
                break;
            }
            case BALANCE_EDIT: {
                Player player = Bukkit.getPlayer(UUID.fromString(redisMessage.getParam("UUID")));
                if (player != null) {
                    Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
                    profile.setVaultBalance(Integer.parseInt(redisMessage.getParam("AMOUNT")), null,
                            Boolean.parseBoolean(redisMessage.getParam("SETMODE")));

                    if (redisMessage.getParam("MESSAGEMODE").equals("TRUE")) {
                        player.sendMessage(CC.translate(redisMessage.getParam("MESSAGE")
                                .replace("{amount}", String.valueOf(profile.getBalance()))
                                .replace("{player_name}", player.getName())));
                    }
                }
                break;
            }
            case WARP_TELEPORT_REPLICA: {
                if (redisMessage.getParam("SERVER").equals(plugin.getServerName())) {
                    String warpName = redisMessage.getParam("WARPNAME");
                    if (plugin.getWarpManager().getByName(warpName) != null) {
                        plugin.getWarpManager().getCacheBuilder().put(UUID.fromString(redisMessage.getParam("PLAYERUUID")), warpName);
                        plugin.getRedisManager().write(new RedisMessage(Payload.WARP_TELEPORT_RESPONSE)
                                .setParam("SERVER", redisMessage.getParam("SENDSERVER"))
                                .setParam("WARPNAME", warpName)
                                .setParam("PLAYERUUID", redisMessage.getParam("PLAYERUUID"))
                                .setParam("ERROR", "FALSE")
                                .setParam("WARPSERVER", plugin.getServerName())
                                .toJSON());
                    } else {
                        plugin.getRedisManager().write(new RedisMessage(Payload.WARP_TELEPORT_RESPONSE)
                                .setParam("SERVER", redisMessage.getParam("SERVER"))
                                .setParam("WARPNAME", warpName)
                                .setParam("PLAYER_UUID", redisMessage.getParam("PLAYER_UUID"))
                                .setParam("ERROR", "TRUE")
                                .setParam("WARPSERVER", plugin.getServerName())
                                .toJSON());
                    }
                }
                break;
            }
            case WARP_TELEPORT_RESPONSE: {
                if (redisMessage.getParam("SERVER").equals(plugin.getServerName())) {
                    Player player = Bukkit.getPlayer(UUID.fromString(redisMessage.getParam("PLAYERUUID")));
                    if (player != null) {
                        if (redisMessage.getParam("ERROR").equals("FALSE")) {
                            TeleportUtil.addTeleport(player, redisMessage.getParam("WARPSERVER"), true);
                        } else {
                            player.sendMessage(CC.translate("&cError trying to teleport to warp " + redisMessage.getParam("WARPNAME")));
                        }
                    }
                }
                break;
            }
            case CREATE_WARP: {
                if (!redisMessage.getParam("SERVER").equals(plugin.getServerName())) {
                    Warp warp = new Warp(redisMessage.getParam("NAME"), redisMessage.getParam("CREATOR"),
                            redisMessage.getParam("SERVER"), LocationUtil.deserialize(redisMessage.getParam("LOCATION")),
                            "&7" + redisMessage.getParam("NAME"));
                    warp.setDisplayName(redisMessage.getParam("DESCRIPTION"));
                    plugin.getWarpManager().getWarps().add(warp);
                }
                break;
            }
            case DELETE_WARP: {
                if (!redisMessage.getParam("SERVER").equals(plugin.getServerName())) {
                    Warp warp = plugin.getWarpManager().getByName(redisMessage.getParam("NAME"));
                    if (warp != null) {
                        plugin.getWarpManager().getWarps().remove(warp);
                    }
                }
                break;
            }
            case REDIS_LOG_MESSAGE: {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.hasPermission("hydracore.redislog")) {
                        onlinePlayer.sendMessage(CC.translate(redisMessage.getParam("MESSAGE")));
                    }
                }
                break;
            }
            case DEPOSIT_REPLICA: {
                Player target = Bukkit.getPlayer(redisMessage.getParam("TARGET"));
                if (target != null) {
                    Profile profile = plugin.getProfileManager().getProfile(target.getUniqueId());
                    int amount = Integer.parseInt(redisMessage.getParam("AMOUNT"));
                    profile.setVaultBalance(amount, "&aYou have received " + amount + " from " + redisMessage.getParam("PLAYER"), false);
                    plugin.getRedisManager().write(new RedisMessage(Payload.DEPOSIT_RESPONSE)
                            .setParam("SERVER", redisMessage.getParam("SERVER"))
                            .setParam("PLAYER", redisMessage.getParam("PLAYER"))
                            .setParam("TARGET", redisMessage.getParam("TARGET"))
                            .setParam("AMOUNT", redisMessage.getParam("AMOUNT"))
                            .toJSON());
                }
                break;
            }
            case DEPOSIT_RESPONSE: {
                if (redisMessage.getParam("SERVER").equals(plugin.getServerName())) {
                    Player player = Bukkit.getPlayer(redisMessage.getParam("PLAYER"));
                    int amount = Integer.parseInt(redisMessage.getParam("AMOUNT"));
                    if (player != null) {
                        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
                        profile.setWaitingDepositResponse(false);
                        profile.setVaultBalance(-amount, CC.translate("&aYou have deposited " + redisMessage.getParam("AMOUNT") + " to " + redisMessage.getParam("TARGET")), false);
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
