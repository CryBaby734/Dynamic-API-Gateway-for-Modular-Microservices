package org.example.springcloudmastery;// package org.example.springcloudmastery.config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Slf4j
@Configuration
class ProbeConfig {
  @Bean ApplicationRunner probe(Environment env) {
    return args -> log.info(">>> jwt.secret.present = {}", env.getProperty("jwt.secret") != null);
  }
}