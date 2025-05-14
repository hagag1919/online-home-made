package com.example.systemadmin.confg;

import com.example.systemadmin.models.PaymentFailure;
import com.example.systemadmin.models.ServiceLog;
import com.example.systemadmin.repos.IPaymentFailureRepo;
import com.example.systemadmin.repos.IServiceLogRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AdminRabbitListener {

    private final IPaymentFailureRepo payRepo;
    private final IServiceLogRepo logRepo;
    private final ObjectMapper mapper = new ObjectMapper();

    public AdminRabbitListener(IPaymentFailureRepo payRepo,
                               IServiceLogRepo logRepo) {
        this.payRepo = payRepo;
        this.logRepo = logRepo;
    }

    @RabbitListener(queues = "payment_failure_queue")
    public void handlePaymentFailure(String body) {
        try {
            JsonNode j = mapper.readTree(body);
            PaymentFailure pf = new PaymentFailure(
                    j.get("paymentId").asText(),
                    j.get("userId").asText(),
                    j.get("amount").asDouble(),
                    j.get("currency").asText(),
                    j.get("reason").asText()
            );
            payRepo.save(pf);
        } catch (Exception e) {
            // optionally log local error
        }
    }

    @RabbitListener(queues = "log_queue")
    public void handleErrorLogs(String body) {
        try {
            JsonNode j = mapper.readTree(body);
            String serviceName = j.get("service").asText();
            String message = j.get("message").asText();
            String severity = j.get("severity").asText();
            String eventData = null;
            if (j.has("eventData")) {
                eventData = j.get("eventData").toString();
            }
            ServiceLog sl = new ServiceLog(serviceName, severity, message, eventData);
            if(j.has("severity") && j.get("severity").asText().equals("Error")) {
                sl.setNotified(true);
            }
            logRepo.save(sl);
        } catch (Exception e) {
            // Optionally log the error
        }
    }

}
