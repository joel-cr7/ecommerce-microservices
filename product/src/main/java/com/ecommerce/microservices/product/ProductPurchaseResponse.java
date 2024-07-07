package com.ecommerce.microservices.product;

import java.math.BigDecimal;

public record ProductPurchaseResponse(
        Integer productid,
        String name,
        String description,
        BigDecimal price,
        double quantity
) {
}
