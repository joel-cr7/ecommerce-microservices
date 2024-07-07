package com.ecommerce.microservices.kafka.payment;

import java.math.BigDecimal;

// Should consist same attributes as send by the producer to kafka
public record PaymentConfirmation(
        String orderReference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String customerFirstName,
        String customerLastName,
        String customerEmail
) {
}
