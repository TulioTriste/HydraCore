package me.arjona.hydracore;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.arjona.hydracore.spawn.commands.SetSpawnCommand;
import me.arjona.hydracore.spawn.commands.SpawnCommand;
import me.arjona.hydracore.profile.ProfileManager;
import me.arjona.hydracore.spawn.SpawnManager;
import me.arjona.hydracore.utilities.FileConfig;
import me.arjona.hydracore.utilities.commands.CommandManager;
import me.arjona.hydracore.utilities.menu.MenuListener;
import me.arjona.hydracore.utilities.redis.Redis;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

@Getter
public class Core extends JavaPlugin {

    private FileConfig mainConfig, databaseConfig;
    public MongoDatabase mongoDatabase;
    private Redis redisManager;
    private ProfileManager profileManager;
    private SpawnManager spawnManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        initConfigs();
        initDatabase();
        initManagers();
        initCommands();
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initManagers() {
        profileManager = new ProfileManager();
        spawnManager = new SpawnManager();
    }

    private void initConfigs() {
        mainConfig = new FileConfig(this, "config.yml");
        databaseConfig = new FileConfig(this, "database.yml");
    }

    private void initCommands() {
        new CommandManager(this, Collections.emptyList());
        new SetSpawnCommand();
        new SpawnCommand();
    }

    private void initDatabase() {
        // MongoDB
        try {
            if (databaseConfig.getBoolean("MONGO.AUTHENTICATION.ENABLED")) {
                mongoDatabase = new MongoClient(
                        new ServerAddress(
                                databaseConfig.getString("MONGO.HOST"),
                                databaseConfig.getInt("MONGO.PORT")
                        ),
                        MongoCredential.createCredential(
                                databaseConfig.getString("MONGO.AUTHENTICATION.USERNAME"),
                                databaseConfig.getString("MONGO.AUTHENTICATION.DATABASE"),
                                databaseConfig.getString("MONGO.AUTHENTICATION.PASSWORD").toCharArray()
                        ),
                        MongoClientOptions.builder().build()
                ).getDatabase(databaseConfig.getString("MONGO.DATABASE"));
            } else {
                mongoDatabase = new MongoClient(databaseConfig.getString("MONGO.HOST"), databaseConfig.getInt("MONGO.PORT"))
                        .getDatabase(databaseConfig.getString("MONGO.DATABASE"));
            }
        } catch (Exception e) {
            System.out.println("The PandaCore plugin was disabled as it failed to connect to the MongoDB");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
        }

        // Redis
        this.redisManager = new Redis();
        redisManager.connect();
    }

    public static Core get() {
        return getPlugin(Core.class);
    }
}
