package me.arjona.hydracore.utilities.redis.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Payload {

    REMOVE_SPAWN_REPLICA,
    REMOVE_SPAWN_RESPONSE,
    SEND_SPAWN_REPLICA,
    SEND_SPAWN_RESPONSE;
}
