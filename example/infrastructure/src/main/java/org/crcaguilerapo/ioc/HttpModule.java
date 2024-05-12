package org.crcaguilerapo.ioc;

import dagger.Provides;
import dagger.Module;
import org.crcaguilerapo.adapters.in.http.HttpController;
import org.crcaguilerapo.adapters.out.memory.AccountRepository;
import org.crcaguilerapo.port.out.IAccountPort;
import org.crcaguilerapo.usecase.PaymentUseCase;
import org.jooq.DSLContext;

import javax.inject.Singleton;

@Module
public class HttpModule {
    @Provides
    @Singleton
    IAccountPort accountPort(DSLContext ctx) {
        return new AccountRepository(ctx);
    }

    @Provides
    @Singleton
    PaymentUseCase paymentUseCase(IAccountPort accountPort) {
        return new PaymentUseCase(accountPort);
    }

    @Provides
    @Singleton
    HttpController httpController(PaymentUseCase paymentUseCase) {
        return new HttpController(paymentUseCase);
    }
}