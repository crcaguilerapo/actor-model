package org.crcaguilerapo.dtos;

public record PaymentDto(int originAccountNumber, int destinationAccountNumber, int amount) {
}
