package com.example.fraudalert.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component("fraudDependencies")
public class DependencyHealthIndicator implements HealthIndicator {
    private final RedisConnectionFactory redis;
    private final RestClient ml;
    public DependencyHealthIndicator(RedisConnectionFactory redis, @Value("${fraudalert.ml-service-url}") String mlUrl) {
        this.redis=redis; this.ml=RestClient.builder().baseUrl(mlUrl).build();
    }
    @Override public Health health() {
        Health.Builder b = Health.up();
        try { redis.getConnection().ping(); b.withDetail("redis", "UP"); }
        catch (Exception e) { b.down().withDetail("redis", "DOWN"); }
        try { ml.get().uri("/health").retrieve().toBodilessEntity(); b.withDetail("ml-service", "UP"); }
        catch (Exception e) { b.down().withDetail("ml-service", "DOWN"); }
        b.withDetail("event-queue", "LOCAL_HTTP_SIMULATION");
        return b.build();
    }
}
