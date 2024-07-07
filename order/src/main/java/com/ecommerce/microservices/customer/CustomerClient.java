package com.ecommerce.microservices.customer;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

// get url from application.yml config file
@FeignClient(name = "customer-service", url = "${application.config.customer-url}")
public interface CustomerClient {
    @GetMapping("/{customer-id}")
    Optional<CustomerResponse> findCustomerById(@PathVariable("customer-id") String customerId);

}
