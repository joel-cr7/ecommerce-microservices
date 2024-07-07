package com.ecommerce.microservices.payment;


import com.ecommerce.microservices.notification.PaymentNotificationProducer;
import com.ecommerce.microservices.notification.PaymentNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentNotificationProducer paymentNotificationProducer;
    private final PaymentMapper paymentMapper;

    public Integer createPayment(PaymentRequest paymentRequest) {
        // persist to database
        Payment payment = paymentRepository.save(paymentMapper.toPayment(paymentRequest));


        //send payment notification confirmation --> notification-ms(kafka)
        PaymentNotificationRequest paymentNotificationRequest = new PaymentNotificationRequest(
                paymentRequest.orderReference(),
                paymentRequest.amount(),
                paymentRequest.paymentMethod(),
                paymentRequest.customer().firstName(),
                paymentRequest.customer().lastName(),
                paymentRequest.customer().email()
        );

        paymentNotificationProducer.sendPaymentNotification(paymentNotificationRequest);

        return payment.getId();
    }
}
