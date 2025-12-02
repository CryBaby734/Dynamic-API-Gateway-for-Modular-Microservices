package org.example.dynamicroutingcore.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PredicateDefinition implements Serializable {

    private String name;
    private Map<String, String> args;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PredicateDefinition that = (PredicateDefinition) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(args, that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, args);
    }
}