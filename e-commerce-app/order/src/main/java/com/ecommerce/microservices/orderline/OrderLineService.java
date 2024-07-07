package com.ecommerce.microservices.orderline;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderLineService {

    private final OrderLinesRepository orderLinesRepository;
    private final OrderLineMapper orderLineMapper;

    public Integer saveOrderLine(OrderLineRequest orderLineRequest) {
        return orderLinesRepository.save(orderLineMapper.toOrderLine(orderLineRequest)).getId();
    }

    public List<OrderLineResponse> findAllByOrderId(Integer orderId) {
        return orderLinesRepository.findAllByOrderId(orderId)
                .stream()
                .map(orderLineMapper::toOrderLineResponse)
                .toList();
    }
}
