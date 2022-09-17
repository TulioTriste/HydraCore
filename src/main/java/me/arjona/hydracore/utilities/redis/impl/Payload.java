package me.arjona.hydracore.utilities.redis.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Payload {

    GET_SPAWN_REPLICA,
    GET_SPAWN_RESPONSE,
    SEND_SPAWN_REPLICA,
    SEND_SPAWN_RESPONSE;
}
