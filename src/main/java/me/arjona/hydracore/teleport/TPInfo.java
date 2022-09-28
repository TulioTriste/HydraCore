package me.arjona.hydracore.teleport;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
@Getter
@RequiredArgsConstructor
public class TPInfo {

    private final String senderName;
    private final UUID senderUUID;
    private final String targetName;
    private final UUID targetUUID;
    private final String server;
}
