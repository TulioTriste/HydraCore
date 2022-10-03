package me.arjona.hydracore.profile;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import me.arjona.hydracore.Core;
import me.arjona.hydracore.utilities.CC;
import me.arjona.hydracore.utilities.TaskUtil;
import me.arjona.hydracore.utilities.redis.impl.Payload;
import me.arjona.hydracore.utilities.redis.util.RedisMessage;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
@Getter @Setter
public class Profile {

    private final UUID uuid;
    private final String name;
    private int balance = 0;
    private boolean online = false;

    public Profile(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

        Core.get().getProfileManager().getProfiles().put(uuid, this);
    }

    public void save() {
        Document document = new Document();
        document.put("uuid", uuid.toString());

        if (getPlayer() != null) document.put("name", getPlayer().getName());
        else document.put("name", name);

        document.put("balance", balance);

        Core.get().getProfileManager().getCollection().replaceOne(Filters.eq("uuid", uuid.toString()), document, new ReplaceOptions().upsert(true));
    }

    public void load() {
        Document document = Core.get().getProfileManager().getCollection().find(Filters.eq("uuid", uuid.toString())).first();

        if (document == null) {
            this.save();
            return;
        }

        setBalance(document.getInteger("balance"));
        Core.get().getEcon().depositPlayer(name, balance);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    /*public String getBalance() {
        return Core.get().getEcon().format(Core.get().getEcon().getBalance(getOfflinePlayer()));
    }*/

    public int getBalance() {
        return balance;
        /*return Core.get().getEcon().getBalance(getOfflinePlayer());*/
    }

    public void setBalance(int balance, String message) {
        double difference = this.balance - balance;

        EconomyResponse response;

        if (difference > 0) {
            response = Core.get().getEcon().depositPlayer(getOfflinePlayer(), difference);
        } else {
            response = Core.get().getEcon().withdrawPlayer(getOfflinePlayer(), -difference);
        }
        TaskUtil.runAsync(this::save);
        if (getPlayer() != null) {
            this.balance = balance;
            if (message != null) {
                if (response.transactionSuccess()) {
                    getPlayer().sendMessage(CC.translate(message));
                } else {
                    getPlayer().sendMessage(CC.translate("&cAn error has occurred."));
                }
            }
        } else {
            Core.get().getRedisManager().write(new RedisMessage(Payload.BALANCE_EDIT)
                    .setParam("UUID", uuid.toString())
                    .setParam("AMOUNT", String.valueOf(balance))
                    .setParam("MESSAGEMODE", message != null ? "TRUE" : "FALSE")
                    .setParam("MESSAGE", message != null ? message : "")
                    .toJSON());
        }
        /*Core.get().getEcon().withdrawPlayer(getOfflinePlayer(), getBalanceInt());*/
    }

    public void incrementBalance(int amount) {
        balance+=amount;

        /*Core.get().getEcon().depositPlayer(getOfflinePlayer(), amount);*/
    }

    public void decrementBalance(int amount) {
        balance-=amount;

        /*Core.get().getEcon().withdrawPlayer(getOfflinePlayer(), amount);*/
    }
}
