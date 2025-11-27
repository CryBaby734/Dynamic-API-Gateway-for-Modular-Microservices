//package org.example.userservice.config;
//
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConverter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.retry.interceptor.RetryOperationsInterceptor;
//
//@Configuration
//public class RabbitConfig {
//
//    public static final String USER_EXCHANGE = "user.exchange";
//    public static final String USER_CREATED_QUEUE = "user.created.queue";
//    public static final String USER_CREATED_ROUTING_KEY = "user.created";
//
//    public static final String USER_CREATED_DLQ = "user.created.dlq";
//    public static final String USER_CREATED_DLQ_ROUTING_KEY = "user.created.dlq";
//
//
//
//
//    // --- Exchange ---
//    @Bean
//    public TopicExchange userExchange() {
//        return new TopicExchange(USER_EXCHANGE);
//    }
//
//    // --- Основная очередь (с Dead Letter Exchange) ---
//    @Bean
//    public Queue userCreatedQueue() {
//        return QueueBuilder.durable(USER_CREATED_QUEUE)
//                .withArgument("x-dead-letter-exchange", USER_EXCHANGE)
//                .withArgument("x-dead-letter-routing-key", USER_CREATED_DLQ_ROUTING_KEY)
//                .build();
//    }
//
//    // --- Dead Letter Queue ---
//    @Bean
//    public Queue userCreatedDLQ() {
//        return QueueBuilder.durable(USER_CREATED_DLQ).build();
//    }
//
//    // --- Binding основной очереди ---
//    @Bean
//    public Binding userCreatedBinding(Queue userCreatedQueue, TopicExchange userExchange) {
//        return BindingBuilder.bind(userCreatedQueue)
//                .to(userExchange)
//                .with(USER_CREATED_ROUTING_KEY);
//    }
//
//    // --- Binding DLQ ---
//    @Bean
//    public Binding userCreatedDLQBinding(Queue userCreatedDLQ, TopicExchange userExchange) {
//        return BindingBuilder.bind(userCreatedDLQ)
//                .to(userExchange)
//                .with(USER_CREATED_DLQ_ROUTING_KEY);
//    }
//
//    // --- Конвертер сообщений (JSON) ---
//    @Bean
//    public MessageConverter messageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
//
//    // --- RabbitTemplate (для отправки сообщений) ---
//    @Bean
//    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(messageConverter());
//        return rabbitTemplate;
//    }
//
//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
//            ConnectionFactory connectionFactory,
//            RetryOperationsInterceptor retryInterceptor) {
//
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(messageConverter());
//        factory.setAdviceChain(retryInterceptor); // добавляем retry interceptor
//        factory.setConcurrentConsumers(2);
//        factory.setMaxConcurrentConsumers(5);
//        return factory;
//    }
//}