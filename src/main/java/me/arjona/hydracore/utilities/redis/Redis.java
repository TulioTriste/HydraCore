package me.arjona.hydracore.utilities.redis;

import lombok.Getter;
import me.arjona.hydracore.Core;
import me.arjona.hydracore.utilities.TaskUtil;
import me.arjona.hydracore.utilities.redis.listener.RedisListener;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Getter
public class Redis {

    JedisPool jedisPool;

    RedisListener redisListener;

    private final String ip = Core.get().getDatabaseConfig().getString("REDIS.HOST");

    private final int port = Core.get().getDatabaseConfig().getInt("REDIS.PORT");

    private final String password = Core.get().getDatabaseConfig().getString("REDIS.AUTHENTICATION.PASSWORD");

    private final boolean auth = Core.get().getDatabaseConfig().getBoolean("REDIS.AUTHENTICATION.ENABLED");

    @Getter private boolean active = false;

    public void connect() {
        try {
            Core.get().getLogger().info("Connecting to redis");
            this.jedisPool = new JedisPool(ip, port);
            Jedis jedis = this.jedisPool.getResource();
            if (auth)
                if (password != null || !password.equals(""))
                    jedis.auth(this.password);
            this.redisListener = new RedisListener();
            (new Thread(() -> jedis.subscribe(this.redisListener, "hydra-core"))).start();
            jedis.connect();
            active = true;
            Core.get().getLogger().info("Successfully redis connection.");
        } catch (Exception e) {
            Core.get().getLogger().info("Error in redis connection.");
            active = false;
        }
    }

    public void disconnect() {
        this.redisListener.unsubscribe();
        jedisPool.destroy();
    }

    public void write(String json){
        try (Jedis jedis = this.jedisPool.getResource()) {
            if (auth) {
                if (password != null || !password.equals(""))
                    jedis.auth(this.password);
            }
            TaskUtil.runAsync(() -> jedis.publish("hydra-core", json));
        }
    }
}
