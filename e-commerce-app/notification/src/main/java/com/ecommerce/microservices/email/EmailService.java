package com.ecommerce.microservices.email;


import com.ecommerce.microservices.kafka.order.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendPaymentSuccessEmail(String destinationEmail, String customerName, BigDecimal amount,
                                        String orderReference) throws MessagingException {

        // create the MIME message object
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_RELATED,
                StandardCharsets.UTF_8.name()
        );
        messageHelper.setFrom("sayhijoel@gmail.com");
        final String templateName = EmailTemplates.PAYMENT_CONFIRMATION.getTemplate();
        messageHelper.setSubject(EmailTemplates.PAYMENT_CONFIRMATION.getSubject());

        // create map of the email body variables
        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("customerName", customerName);
        templateVariables.put("amount", amount);
        templateVariables.put("orderReference", orderReference);

        // create Thymeleaf context to hold the variables to be filled in the html template for email
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateVariables);

        // populate the html template with variables, set it into the message and send mail
        try {
            String htmlTemplate = templateEngine.process(templateName, thymeleafContext);
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info(String.format("INFO - Email successfully sent to %s with template %s", destinationEmail, templateName));
        }
        catch (MessagingException e){
            log.warn("WARNING - Cannot send email to {}", destinationEmail);
        }
    }


    @Async
    public void sendOrderConfirmationEmail(String destinationEmail, String customerName, BigDecimal amount,
                                           String orderReference, List<Product> products) throws MessagingException {

        // create the MIME message object
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_RELATED,
                StandardCharsets.UTF_8.name()
        );
        messageHelper.setFrom("sayhijoel@gmail.com");
        final String templateName = EmailTemplates.ORDER_CONFIRMATION.getTemplate();
        messageHelper.setSubject(EmailTemplates.ORDER_CONFIRMATION.getSubject());

        // create map of the email body variables
        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("customerName", customerName);
        templateVariables.put("totalAmount", amount);
        templateVariables.put("orderReference", orderReference);
        templateVariables.put("products", products);

        // create Thymeleaf context to hold the variables to be filled in the html template for email
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateVariables);

        // populate the html template with variables, set it into the message and send mail
        try {
            String htmlTemplate = templateEngine.process(templateName, thymeleafContext);
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info(String.format("INFO - Email successfully sent to %s with template %s", destinationEmail, templateName));
        }
        catch (MessagingException e){
            log.warn("WARNING - Cannot send email to {}", destinationEmail);
        }
    }

}
