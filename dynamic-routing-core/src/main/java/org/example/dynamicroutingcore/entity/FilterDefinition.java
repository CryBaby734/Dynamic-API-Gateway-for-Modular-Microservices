package org.example.dynamicroutingcore.entity;

import lombok.*;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterDefinition {
    private String name;
    private Map<String, String> args;
}