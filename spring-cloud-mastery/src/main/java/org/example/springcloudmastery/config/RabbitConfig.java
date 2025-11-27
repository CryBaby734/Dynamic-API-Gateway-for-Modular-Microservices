//package org.example.springcloudmastery.config;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConverter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitConfig {
//
//    public static final String USER_EXCHANGE = "user.exchange";
//    public static final String USER_CREATED_QUEUE = "user.created.queue";
//    public static final String USER_CREATED_ROUTING_KEY = "user.created";
//
//
//    @Bean
//    public TopicExchange userExchange() {
//        return new TopicExchange(USER_EXCHANGE);
//    }
//
//
//
//    @Bean
//    public Queue userCreatedQueue() {
//        return new Queue(USER_CREATED_QUEUE, true);
//    }
//
//    @Bean
//    public Binding userCreatedBinding(Queue userCreatedQueue, TopicExchange userExchange) {
//       return BindingBuilder.bind(userCreatedQueue)
//               .to(userExchange)
//               .with(USER_CREATED_ROUTING_KEY);
//    }
//
//    @Bean
//    public MessageConverter messageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
//
//    @Bean
//    public AmqpTemplate amqpTemplate(ConnectionFactory cf) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(cf);
//        rabbitTemplate.setMessageConverter(messageConverter());
//        rabbitTemplate.setBeforePublishPostProcessors(message -> {
//            message.getMessageProperties().getHeaders().remove("__TypeId__");
//            return message;
//        });
//        return rabbitTemplate;
//    }
//
//}
