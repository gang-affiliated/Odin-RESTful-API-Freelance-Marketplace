package com.odine.Freelance.Marketplace.API.freelancer.messaging;

import com.odine.Freelance.Marketplace.API.config.RabbitMqConfig;
import com.odine.Freelance.Marketplace.API.freelancer.service.FreelancerEvaluationService;
import java.util.Objects;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FreelancerEvaluationConsumer {
    private final FreelancerEvaluationService freelancerEvaluationService;

    public FreelancerEvaluationConsumer(FreelancerEvaluationService freelancerEvaluationService) {
        this.freelancerEvaluationService = freelancerEvaluationService;
    }

    @RabbitListener(queues = RabbitMqConfig.EVALUATION_QUEUE)
    public void handleEvaluationMessage(String freelancerIdPayload) {
        Long freelancerId = Long.parseLong(Objects.requireNonNull(freelancerIdPayload, "freelancerIdPayload cannot be null"));
        freelancerEvaluationService.evaluateFreelancer(freelancerId);
    }
}
