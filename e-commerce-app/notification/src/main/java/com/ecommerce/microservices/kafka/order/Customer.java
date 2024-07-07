package com.ecommerce.microservices.kafka.order;

public record Customer(
        String id,
        String firstName,
        String lastName,
        String email
) {
}
