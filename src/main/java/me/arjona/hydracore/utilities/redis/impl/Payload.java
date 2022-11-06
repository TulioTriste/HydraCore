package me.arjona.hydracore.utilities.redis.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Payload {

    REMOVE_SPAWN_REPLICA,
    REMOVE_SPAWN_RESPONSE,
    SEND_SPAWN_REPLICA,
    SEND_SPAWN_RESPONSE,
    TPA_REPLICA,
    TPA_RESPONSE,
    TPA_ACCEPT_REPLICA,
    TPA_ACCEPT_RESPONSE,
    BALANCE_EDIT,
    WARP_TELEPORT_REPLICA,
    WARP_TELEPORT_RESPONSE,
    CREATE_WARP,
    DELETE_WARP,
    REDIS_LOG_MESSAGE,
    DEPOSIT_REPLICA,
    DEPOSIT_RESPONSE
}
