package com.ecommerce.microservices.kafka;


import com.ecommerce.microservices.customer.CustomerResponse;
import com.ecommerce.microservices.order.PaymentMethod;
import com.ecommerce.microservices.product.ProductPurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

// The data to be sent to kafka broker
public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,

        // products customer purchased
        List<ProductPurchaseResponse> products
) {
}
