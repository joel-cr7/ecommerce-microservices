package com.ecommerce.microservices.email;

import lombok.Getter;

public enum EmailTemplates {

    // thymeleaf automatically picks templates from 'templates' folder under resources

    PAYMENT_CONFIRMATION("payment-confirmation.html", "Payment successfully processed"),
    ORDER_CONFIRMATION("order-confirmation.html", "Order placed successfully");

    @Getter
    private final String template;

    @Getter
    private final String subject;

    EmailTemplates(String template, String subject){
        this.template = template;
        this.subject = subject;
    }

}
