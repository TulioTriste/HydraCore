package me.arjona.hydracore.utilities.redis.listener;

import com.google.gson.Gson;
import me.arjona.hydracore.Core;
import me.arjona.hydracore.utilities.redis.util.RedisMessage;
import redis.clients.jedis.JedisPubSub;

public class RedisListener extends JedisPubSub {

    private final Core plugin = Core.get();

    @Override
    public void onMessage(String channel, String message) {
        RedisMessage redisMessage = new Gson().fromJson(message, RedisMessage.class);
        /*
        A switch is made to assign the action for each type of Payload, it can also be done by "if"
         */
        /*switch (redisMessage.getPayload()) {
            case STAFF_JOIN: {
                if (!Payload.getBoolean(Payload.STAFF_JOIN)) break;
                String player = redisMessage.getParam("PLAYER");
                String prefix = redisMessage.getParam("PREFIX");
                String server = redisMessage.getParam("SERVER");
                Bukkit.getOnlinePlayers().forEach(players -> {
                    if (players.hasPermission("pandacore.staff.join")) {
                        players.sendMessage(ChatUtil.translate(langConfig.getString(Payload.STAFF_JOIN.getSection()
                                .replace("{server}", server)
                                .replace("{prefix}", prefix)
                                .replace("{player}", player))));
                    }
                });
            }
            break;
            case STAFF_LEAVE: {
                if (!Payload.getBoolean(Payload.STAFF_LEAVE)) break;
                String player = redisMessage.getParam("PLAYER");
                String prefix = redisMessage.getParam("PREFIX");
                String server = redisMessage.getParam("SERVER");
                Bukkit.getOnlinePlayers().forEach(players -> {
                    if (players.hasPermission("pandacore.staff.leave")) {
                        players.sendMessage(ChatUtil.translate(langConfig.getString(Payload.STAFF_LEAVE.getSection())
                                .replace("{server}", server)
                                .replace("{prefix}", prefix)
                                .replace("{player}", player)));
                    }
                });
            }
            break;
            case STAFF_CHAT: {
                if (!Payload.getBoolean(Payload.STAFF_CHAT)) break;
                String player = redisMessage.getParam("PLAYER");
                String prefix = redisMessage.getParam("PREFIX");
                String server = redisMessage.getParam("SERVER");
                String message1 = redisMessage.getParam("MESSAGE");
                Bukkit.getOnlinePlayers().forEach(players -> {
                    if (players.hasPermission("pandacore.staff.chat")) {
                        players.sendMessage(ChatUtil.translate(langConfig.getString(Payload.STAFF_CHAT.getSection())
                                .replace("{server}", server)
                                .replace("{prefix}", prefix)
                                .replace("{player}", player)
                                .replace("{message}", message1)));
                    }
                });
            }
            break;
            case ADMIN_CHAT: {
                if (!Payload.getBoolean(Payload.ADMIN_CHAT)) break;
                String player = redisMessage.getParam("PLAYER");
                String prefix = redisMessage.getParam("PREFIX");
                String server = redisMessage.getParam("SERVER");
                String message1 = redisMessage.getParam("MESSAGE");
                Bukkit.getOnlinePlayers().forEach(players -> {
                    if (players.hasPermission("pandacore.admin")) {
                        players.sendMessage(ChatUtil.translate(langConfig.getString(Payload.ADMIN_CHAT.getSection())
                                .replace("{server}", server)
                                .replace("{prefix}", prefix)
                                .replace("{player}", player)
                                .replace("{message}", message1)));
                    }
                });
            }
            break;
            case LOAD_SERVER: {
                if (!Payload.getBoolean(Payload.LOAD_SERVER)) break;
                String server = redisMessage.getParam("SERVER");
                Bukkit.getOnlinePlayers().forEach(players -> {
                    if (players.hasPermission("pandacore.admin") || players.isOnline()) {
                        players.sendMessage(ChatUtil.translate(langConfig.getString(Payload.LOAD_SERVER.getSection())
                                .replace("{server}", server)));
                    }
                });
            }
            break;
            case CLOSE_SERVER: {
                if (!Payload.getBoolean(Payload.CLOSE_SERVER)) break;
                String server = redisMessage.getParam("SERVER");
                if (Core.SERVER_NAME.equals(server)) {
                    Bukkit.shutdown();
                }
            }
            break;
            case ENABLE_WHITELIST: {
                if (!Payload.getBoolean(Payload.ENABLE_WHITELIST)) break;
                String server = redisMessage.getParam("SERVER");
                if (Core.SERVER_NAME.equals(server)) Bukkit.setWhitelist(true);
            }
            break;
            case DISABLE_WHITELIST: {
                if (!Payload.getBoolean(Payload.DISABLE_WHITELIST)) break;
                String server = redisMessage.getParam("SERVER");
                if (Core.SERVER_NAME.equals(server)) Bukkit.setWhitelist(false);
            }
            break;
            default: {
                plugin.getLogger().info("[Redis] The message was received, but there was no response");
            }
            break;
        }*/
    }
}
