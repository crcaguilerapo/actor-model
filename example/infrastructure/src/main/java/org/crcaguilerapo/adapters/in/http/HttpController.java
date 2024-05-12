package org.crcaguilerapo.adapters.in.http;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.annotation.Get;
import org.crcaguilerapo.dtos.PaymentDto;
import org.crcaguilerapo.usecase.PaymentUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public final class HttpController {

  private static final Logger logger = LoggerFactory.getLogger(HttpController.class);

  private final PaymentUseCase paymentUseCase;

  @Inject
  public HttpController(PaymentUseCase paymentUseCase) {
    this.paymentUseCase = paymentUseCase;
  }

  @Get("/pay")
  public HttpResponse pay(PaymentDto paymentDto) {
    paymentUseCase.pay(paymentDto);
    return HttpResponse.ofJson(
            HttpStatus.OK,
            new Object()
    );
  }

}