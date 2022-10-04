package me.arjona.hydracore.warp;

import com.mongodb.client.model.ReplaceOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.arjona.hydracore.Core;
import me.arjona.hydracore.utilities.LocationUtil;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
@Getter @Setter
@AllArgsConstructor
public class Warp {

    public final String name;
    public final String creator;
    public final String server;
    public final Location location;
    public String displayName;

    public void save() {
        Document document = new Document();

        document.put("name", name);
        document.put("creator", creator);
        document.put("server", server);
        document.put("location", LocationUtil.serialize(location));
        document.put("displayName", displayName);

        Core.get().getWarpManager().getCollection().replaceOne(new Document("name", name), document, new ReplaceOptions().upsert(true));
    }

    public void teleport(Player player) {
        player.teleport(location);
    }
}
