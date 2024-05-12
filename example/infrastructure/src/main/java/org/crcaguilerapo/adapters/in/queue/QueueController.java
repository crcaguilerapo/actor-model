package org.crcaguilerapo.adapters.in.queue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.crcaguilerapo.dtos.MessageDto;
import org.crcaguilerapo.dtos.PaymentDto;
import org.crcaguilerapo.usecase.PaymentUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sqs.model.Message;

import javax.inject.Inject;

public class QueueController {

    private static final Logger logger = LoggerFactory.getLogger(QueueController.class);

    private final ObjectMapper objectMapper;

    private final PaymentUseCase paymentUseCase;

    @Inject
    public QueueController(
            ObjectMapper objectMapper,
            PaymentUseCase paymentUseCase
    ) {
        this.objectMapper = objectMapper;
        this.paymentUseCase = paymentUseCase;
    }

    public Message process(Message message) {
        try {
            MessageDto m = objectMapper.readValue(message.body(), MessageDto.class);

            switch (m.eventType()) {
                case PAYMENT:
                    TypeReference<MessageDto<PaymentDto>> type = new TypeReference<>() {};
                    MessageDto<PaymentDto> m1 = objectMapper.readValue(message.body(), type);
                    paymentUseCase.pay(m1.payload());
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return message;
    }


}
