package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.UserDto;
import org.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> {
                    UserDto dto = new UserDto();
                    dto.setUsername(user.getUsername());
                    dto.setEmail(user.getEmail());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}