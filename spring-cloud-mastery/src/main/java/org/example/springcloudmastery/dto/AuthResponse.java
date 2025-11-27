package org.example.springcloudmastery.dto;

public class AuthResponse {
    private final String token;
    private final String role;

    public AuthResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }
}
