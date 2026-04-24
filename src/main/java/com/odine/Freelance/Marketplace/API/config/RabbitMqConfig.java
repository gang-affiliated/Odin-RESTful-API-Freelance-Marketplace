package com.odine.Freelance.Marketplace.API.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String EVALUATION_QUEUE = "freelancer.evaluation.queue";
    public static final String EVALUATION_EXCHANGE = "freelancer.evaluation.exchange";
    public static final String EVALUATION_ROUTING_KEY = "freelancer.evaluation";

    @Bean
    public Queue evaluationQueue() {
        return new Queue(EVALUATION_QUEUE, true);
    }

    @Bean
    public DirectExchange evaluationExchange() {
        return new DirectExchange(EVALUATION_EXCHANGE);
    }

    @Bean
    public Binding evaluationBinding(Queue evaluationQueue, DirectExchange evaluationExchange) {
        return BindingBuilder.bind(evaluationQueue).to(evaluationExchange).with(EVALUATION_ROUTING_KEY);
    }
}
