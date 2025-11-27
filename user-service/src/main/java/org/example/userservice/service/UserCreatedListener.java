//package org.example.userservice.service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.example.userservice.config.RabbitConfig;
//import org.example.userservice.dto.UserCreatedEvent;
//import org.example.userservice.entity.User;
//import org.example.userservice.repository.UserRepository;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class UserCreatedListener {
//
//    private final UserRepository userRepository;
//
//    @RabbitListener(queues = RabbitConfig.USER_CREATED_QUEUE)
//    public void handleUserCreated(UserCreatedEvent event) {
//        log.info("Received user created event: {}", event);
//
//        // ⚠️ Искусственно кидаем ошибку для проверки DLQ
//        if ("errorUser".equalsIgnoreCase(event.getUsername())) {
//            throw new RuntimeException("Simulated failure while processing user: " + event.getUsername());
//        }
//
//        User user = new User();
//        user.setId(event.getId());
//        user.setUsername(event.getUsername());
//        user.setRole(event.getRole());
//        userRepository.save(user);
//
//        log.info("User saved to local DB: {}", user);
//    }
//}
