package com.ecommerce.microservices.product;


import com.ecommerce.microservices.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductClient {

    @Value("${application.config.product-url}")
    private String productUrl;
    private final RestTemplate restTemplate;    // custom bean is created for RestTemplate in the config package


    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> requestBody){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        // prepare the request for RestTemplate with body and headers
        HttpEntity<List<ProductPurchaseRequest>> requestEntity = new HttpEntity<>(requestBody, httpHeaders);

        // required to parse the response from microservice to List<ProductPurchaseResponse>
        ParameterizedTypeReference<List<ProductPurchaseResponse>> responseType =
                new ParameterizedTypeReference<>() { };

        ResponseEntity<List<ProductPurchaseResponse>> responseEntity = restTemplate.exchange(
                productUrl + "/purchase",
                HttpMethod.POST,
                requestEntity,
                responseType
        );

        if(responseEntity.getStatusCode().isError()){
            throw new BusinessException("An error occurred while processing the product purchase: "
                    + responseEntity.getStatusCode());
        }

        return responseEntity.getBody();
    }

}
