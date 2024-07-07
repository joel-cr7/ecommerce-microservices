package com.ecommerce.microservices.handler;

import java.util.Map;

public record CustomErrorResponse(
        Map<String, String> errors
) {
}
