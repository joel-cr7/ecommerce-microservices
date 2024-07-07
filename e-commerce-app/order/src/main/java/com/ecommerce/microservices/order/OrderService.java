package com.ecommerce.microservices.order;


import com.ecommerce.microservices.customer.CustomerClient;
import com.ecommerce.microservices.customer.CustomerResponse;
import com.ecommerce.microservices.exception.BusinessException;
import com.ecommerce.microservices.kafka.OrderConfirmation;
import com.ecommerce.microservices.kafka.OrderProducer;
import com.ecommerce.microservices.orderline.OrderLineRequest;
import com.ecommerce.microservices.orderline.OrderLineService;
import com.ecommerce.microservices.payment.PaymentClient;
import com.ecommerce.microservices.payment.PaymentRequest;
import com.ecommerce.microservices.product.ProductClient;
import com.ecommerce.microservices.product.ProductPurchaseRequest;
import com.ecommerce.microservices.product.ProductPurchaseResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;

    private final CustomerClient customerClient;
    private final ProductClient productClient;  // this is used for REST Template implementation
    private final PaymentClient paymentClient;


    public Integer createOrder(OrderRequest orderRequest) {
        // check the customer --> interact with customer-service (using Feign client)
        CustomerResponse customer = customerClient.findCustomerById(orderRequest.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer " +
                        "exists with provided ID:: "+orderRequest.customerId()));


        //purchase the product --> interact with product-service (using REST Template)
        List<ProductPurchaseResponse> purchasedProducts = productClient.purchaseProducts(orderRequest.products());


        //persist the order
        Order order = orderRepository.save(orderMapper.toOrder(orderRequest));
        

        //persist the orderlines
        for(ProductPurchaseRequest purchaseRequest: orderRequest.products()){
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }


        // start payment process --> interact with payment-service (using Feign client)
        PaymentRequest paymentRequest = new PaymentRequest(
                orderRequest.amount(),
                orderRequest.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);


        //send order confirmation --> notification-ms(kafka)
        OrderConfirmation orderConfirmation = new OrderConfirmation(
                orderRequest.reference(),
                orderRequest.amount(),
                orderRequest.paymentMethod(),
                customer,
                purchasedProducts
        );
        orderProducer.sendOrderConfirmation(orderConfirmation);


        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    public OrderResponse findById(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::toOrderResponse)
                .orElseThrow(() -> new EntityNotFoundException("No order found with provided ID:: " + orderId));
    }
}
