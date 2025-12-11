package org.example.dynamicroutingcore.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;
import org.hibernate.annotations.Type;

import java.util.HashMap;

@Getter @Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterArgs extends HashMap<String, Object> {}