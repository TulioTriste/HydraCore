package me.arjona.hydracore.utilities.redis.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Payload {

    SPAWN_REPLICA,
    SPAWN_RESPONSE;
}
