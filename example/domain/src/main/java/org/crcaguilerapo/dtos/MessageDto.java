package org.crcaguilerapo.dtos;

public record MessageDto<T>(String from, String to, EventType eventType, T payload) {
}
