package org.example.dynamicroutingcore.validator;

import org.example.dynamicroutingcore.dto.RouteRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {

    public void validate(RouteRequest req) {

        if (req.getPredicates() == null)
            throw new IllegalArgumentException("Predicates cannot be null");

        if (req.getFilters() == null)
            throw new IllegalArgumentException("Filters cannot be null");

        // --------------------------
        // VALIDATE PREDICATES
        // --------------------------
        for (var predicate : req.getPredicates()) {

            // Path predicate — Spring Cloud Gateway style
            if (predicate.getName().equals("Path")) {

                // args == Map<String,String>
                var args = predicate.getArgs();

                if (args == null || !args.containsKey("_genkey_0")) {
                    throw new IllegalArgumentException("Path predicate requires '_genkey_0' (example: '/test/**')");
                }
            }
        }

        // --------------------------
        // VALIDATE FILTERS
        // --------------------------
        for (var f : req.getFilters()) {

            // StripPrefix
            if (f.getName().equals("StripPrefix")) {
                if (!f.getArgs().containsKey("parts"))
                    throw new IllegalArgumentException("StripPrefix requires 'parts'");
            }

            // RewritePath
            if (f.getName().equals("RewritePath")) {

                // Ты используешь gateway-style args: _genkey_0, _genkey_1
                var args = f.getArgs();

                if (args == null ||
                        !args.containsKey("_genkey_0") ||
                        !args.containsKey("_genkey_1")) {
                    throw new IllegalArgumentException("RewritePath requires '_genkey_0' (regex) AND '_genkey_1' (replacement)");
                }
            }

            // Rate Limiter
            if (f.getName().equals("RequestRateLimiter")) {
                if (!f.getArgs().containsKey("redis-rate-limiter.replenishRate") ||
                        !f.getArgs().containsKey("redis-rate-limiter.burstCapacity")) {

                    throw new IllegalArgumentException(
                            "RateLimiter requires redis-rate-limiter.replenishRate + redis-rate-limiter.burstCapacity");
                }
            }
        }
    }
}