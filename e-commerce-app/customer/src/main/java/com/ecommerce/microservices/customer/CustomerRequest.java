package com.ecommerce.microservices.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

// add validations to this class parameters
public record CustomerRequest(
        String id,

        @NotNull(message = "Customer firstname is required")
        String firstName,

        @NotNull(message = "Customer lastname is required")
        String lastName,

        @NotNull(message = "Customer email is required")
        @Email(message = "Customer email is not a valid email")
        String email,

        Address address
)  {

}
