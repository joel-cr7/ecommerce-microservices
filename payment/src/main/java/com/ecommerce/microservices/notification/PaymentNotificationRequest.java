package com.ecommerce.microservices.notification;

import com.ecommerce.microservices.payment.Customer;
import com.ecommerce.microservices.payment.PaymentMethod;

import java.math.BigDecimal;


// The data to be sent to kafka broker
public record PaymentNotificationRequest(
        String orderReference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String customerFirstName,
        String customerLastName,
        String customerEmail
) {
}
