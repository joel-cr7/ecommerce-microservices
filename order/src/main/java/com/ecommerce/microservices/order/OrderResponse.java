package com.ecommerce.microservices.order;


import com.ecommerce.microservices.orderline.OrderLine;
import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
        Integer id,
        String reference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        String customerId
) {
}
