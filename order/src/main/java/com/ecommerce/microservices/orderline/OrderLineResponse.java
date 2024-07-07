package com.ecommerce.microservices.orderline;

public record OrderLineResponse(
        Integer id,
        double quantity
) {
}
