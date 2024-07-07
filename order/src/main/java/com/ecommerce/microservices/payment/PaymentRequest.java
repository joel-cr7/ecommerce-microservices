package com.ecommerce.microservices.payment;

import com.ecommerce.microservices.customer.CustomerResponse;
import com.ecommerce.microservices.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
