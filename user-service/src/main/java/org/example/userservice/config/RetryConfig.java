//package org.example.userservice.config;
//
//import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.retry.interceptor.RetryInterceptorBuilder;
//import org.springframework.retry.interceptor.RetryOperationsInterceptor;
//
//@Configuration
//public class RetryConfig {
//
//
//
//    @Bean
//    public RetryOperationsInterceptor retryInterceptor() {
//        return RetryInterceptorBuilder.stateless()
//                .maxAttempts(3)
//                .backOffOptions(2000, 1.0, 2000) // начальная пауза, множитель, макс пауза
//                .build();
//    }
//}