package me.arjona.hydracore;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.arjona.hydracore.leaderboard.LeaderboardManager;
import me.arjona.hydracore.leaderboard.LeaderboardCommand;
import me.arjona.hydracore.profile.ProfileListener;
import me.arjona.hydracore.profile.balance.BalanceCommand;
import me.arjona.hydracore.profile.balance.DepositCommand;
import me.arjona.hydracore.profile.balance.sub.BalanceAddSubCommand;
import me.arjona.hydracore.profile.balance.sub.BalanceSetSubCommand;
import me.arjona.hydracore.spawn.SpawnListener;
import me.arjona.hydracore.spawn.commands.RemoveSpawnCommand;
import me.arjona.hydracore.spawn.commands.SetSpawnCommand;
import me.arjona.hydracore.spawn.commands.SpawnCommand;
import me.arjona.hydracore.profile.ProfileManager;
import me.arjona.hydracore.spawn.SpawnManager;
import me.arjona.hydracore.teleport.TeleportListener;
import me.arjona.hydracore.teleport.TeleportManager;
import me.arjona.hydracore.teleport.commands.TPACommand;
import me.arjona.hydracore.teleport.commands.TPAcceptCommand;
import me.arjona.hydracore.utilities.CC;
import me.arjona.hydracore.utilities.FileConfig;
import me.arjona.hydracore.utilities.commands.CommandManager;
import me.arjona.hydracore.utilities.menu.MenuListener;
import me.arjona.hydracore.utilities.redis.Redis;
import me.arjona.hydracore.warp.WarpListener;
import me.arjona.hydracore.warp.WarpManager;
import me.arjona.hydracore.warp.commands.CreateWarpCommand;
import me.arjona.hydracore.warp.commands.DeleteWarpCommand;
import me.arjona.hydracore.warp.commands.WarpCommand;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collections;

@Getter
public class Core extends JavaPlugin {

    private FileConfig mainConfig, databaseConfig;
    private MongoDatabase mongoDatabase;
    private Redis redisManager;
    private ProfileManager profileManager;
    private SpawnManager spawnManager;
    private TeleportManager teleportManager;
    private LeaderboardManager leaderboardManager;
    private WarpManager warpManager;
    private String serverName;
    private Economy econ = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        initConfigs();

        getServer().getConsoleSender().sendMessage(CC.translate("&aLoading HydraCore..."));

        setupEconomy();

        initDatabase();
        initManagers();
        initListeners();
        initCommands();
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        serverName = mainConfig.getString("SERVER_NAME");

        /*if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI(this).register();
        } else {
            getServer().getConsoleSender().sendMessage(CC.translate("&cPlaceholderAPI not found!"));
        }*/

        getServer().getConsoleSender().sendMessage(CC.translate("&aHydraCore loaded successfully!"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getConsoleSender().sendMessage(CC.translate("&cDisabling HydraCore..."));
        profileManager.saveAll();
    }

    private void initManagers() {
        profileManager = new ProfileManager();
        spawnManager = new SpawnManager();
        teleportManager = new TeleportManager();
        leaderboardManager = new LeaderboardManager();
        warpManager = new WarpManager();
    }

    private void initListeners() {
        Arrays.asList(new MenuListener(),
                    new SpawnListener(),
                    new ProfileListener(),
                    new TeleportListener(),
                    new WarpListener())
                .forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }

    private void initConfigs() {
        mainConfig = new FileConfig(this, "config.yml");
        databaseConfig = new FileConfig(this, "database.yml");
    }

    private void initCommands() {
        new CommandManager(this, Collections.emptyList());
        new SetSpawnCommand();
        new SpawnCommand();
        new RemoveSpawnCommand();
        new TPACommand();
        new TPAcceptCommand();
        new BalanceCommand();
        new DepositCommand();
        new BalanceSetSubCommand();
        new BalanceAddSubCommand();
        new LeaderboardCommand();
        new WarpCommand();
        new CreateWarpCommand();
        new DeleteWarpCommand();
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

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            getLogger().severe("Disabled due to no Vault dependency found!" + getDescription().getName());
            return;
        }

        Bukkit.getServicesManager().register(Economy.class, new me.arjona.hydracore.Economy(this), this, ServicePriority.Highest);

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().severe("Error trying to get economy provider!");
            return;
        }
        econ = rsp.getProvider();
    }

    public static Core get() {
        return getPlugin(Core.class);
    }
}
