// org.example.springcloudmastery.events.UserCreatedEvent
package org.example.springcloudmastery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedEvent {
    private Long id;
    private String username;
    private String role;
}