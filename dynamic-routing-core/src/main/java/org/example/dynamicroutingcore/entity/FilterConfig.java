package org.example.dynamicroutingcore.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class FilterConfig implements Serializable {

    private String name;
    private Map<String, String> args;
}