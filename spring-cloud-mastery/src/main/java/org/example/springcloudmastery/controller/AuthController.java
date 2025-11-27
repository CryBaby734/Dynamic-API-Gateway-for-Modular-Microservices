package org.example.springcloudmastery.controller;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.springcloudmastery.dto.RegisterRequest;
import org.example.springcloudmastery.dto.AuthRequest;
import org.example.springcloudmastery.security.AuthenticationService;
import org.example.springcloudmastery.security.BlacklistService;
import org.example.springcloudmastery.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Auth Controller", description = "API for Auth")
@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final BlacklistService blacklistService;


    public AuthController(AuthenticationService authenticationService, JwtService jwtService, BlacklistService blacklistService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.blacklistService = blacklistService;
    }


    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> login (@RequestBody AuthRequest authRequest) {
        return authenticationService.login(authRequest);
    }

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> register (@RequestBody RegisterRequest registerRequest) {
        return authenticationService.register(registerRequest);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout (@RequestHeader("Authorization") String header){
        if (!header.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        String token = header.substring(7);
        long exp = jwtService.extractAllClaims(token).getExpiration().getTime();
        long now = System.currentTimeMillis();

        long ttl = Math.max(1, (exp - now) / 1000);

        blacklistService.blacklistToken(token, ttl);
        return ResponseEntity.ok("Logged successful");
    }

}
