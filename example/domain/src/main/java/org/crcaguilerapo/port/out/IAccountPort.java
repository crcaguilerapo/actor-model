package org.crcaguilerapo.port.out;

import org.crcaguilerapo.dtos.PaymentDto;

public interface IAccountPort {
    void pay(PaymentDto paymentDto);
}
