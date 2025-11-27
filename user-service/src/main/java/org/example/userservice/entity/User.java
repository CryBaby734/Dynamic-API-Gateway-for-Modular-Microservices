package org.example.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    // ВАЖНО: id нам уже прислал auth-service, поэтому БЕЗ @GeneratedValue
    @Id
    private Long id;

    private String username;

    private String role;   // у тебя в событии есть role

    private String email;  // в событии пока нет, но можно оставить nullable
}