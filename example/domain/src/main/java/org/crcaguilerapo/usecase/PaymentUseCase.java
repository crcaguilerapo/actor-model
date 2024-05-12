package org.crcaguilerapo.usecase;

import org.crcaguilerapo.dtos.PaymentDto;
import org.crcaguilerapo.port.out.IAccountPort;

public class PaymentUseCase {

    private final IAccountPort accountRepository;

    public PaymentUseCase(IAccountPort accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void pay(PaymentDto message) {
        accountRepository.pay(message);
    }
}
