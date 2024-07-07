package com.ecommerce.microservices.kafka.order;

import com.ecommerce.microservices.kafka.payment.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;


// Should consist same attributes as send by the producer to kafka

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        Customer customer,

        // products customer purchased
        List<Product> products
) {
}
