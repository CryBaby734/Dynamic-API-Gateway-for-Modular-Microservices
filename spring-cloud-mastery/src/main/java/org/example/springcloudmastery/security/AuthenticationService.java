package org.example.springcloudmastery.security;


import lombok.RequiredArgsConstructor;

import org.example.springcloudmastery.dto.AuthRequest;
import org.example.springcloudmastery.dto.AuthResponse;
import org.example.springcloudmastery.dto.RegisterRequest;
import org.example.springcloudmastery.dto.UserCreatedEvent;
import org.example.springcloudmastery.entity.User;
import org.example.springcloudmastery.entity.Role;
import org.example.springcloudmastery.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
//    private final AmqpTemplate amqpTemplate;



    public ResponseEntity<AuthResponse> login(AuthRequest authRequest) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );


            User user = (User) authentication.getPrincipal();
            String token = jwtService.generateToken(user.getUsername(), user.getRole());

            return ResponseEntity.ok(new AuthResponse(token, user.getRole().name()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, "Invalid username or password"));
        }
    }

   public ResponseEntity<AuthResponse> register(RegisterRequest registerRequest)  {
        try{

            if(userRepository.existsByUsername(registerRequest.getUsername())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new AuthResponse(null, "User already exists"));
            }


            User user = User.builder()
                    .username(registerRequest.getUsername())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .role(Role.USER)
                    .build();

            User saved = userRepository.save(user);
            String token = jwtService.generateToken(saved.getUsername(), saved.getRole());

            UserCreatedEvent event = new UserCreatedEvent(
                    saved.getId(),
                    saved.getUsername(),
                    saved.getRole().name()
            );

//            amqpTemplate.convertAndSend(
//                    RabbitConfig.USER_EXCHANGE,
//                    RabbitConfig.USER_CREATED_ROUTING_KEY,
//                    event
//            );


            return ResponseEntity.ok(new AuthResponse(token, saved.getRole().name()));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(null, "Registration failed"));
        }
   }


}
