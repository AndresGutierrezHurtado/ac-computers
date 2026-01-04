package com.accomputers.api.infrastructure.http;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.accomputers.api.infrastructure.http.responses.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

@Component
public class RateLimiter extends OncePerRequestFilter {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        ObjectMapper objectMapper = new ObjectMapper();
        Bucket bucket = this.buckets.computeIfAbsent(ip, this::newBucket);

        if (bucket.tryConsume(1)) {
            long remainingTokens = bucket.getAvailableTokens();
            response.setHeader("X-RateLimit-Remaining", String.valueOf(remainingTokens));
            response.setHeader("X-RateLimit-Limit", "60");
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(429);
            response.setHeader("X-RateLimit-Remaining", "0");
            response.setHeader("X-RateLimit-Limit", "60");

            // JSON response
            response.setContentType(MediaType.parseMediaTypeOnly("application/json").toString());
            ResponseDTO<Void> responseDTO = new ResponseDTO<Void>("Too many requests", false);
            objectMapper.writeValue(response.getWriter(), responseDTO);
        }
    }

    private Bucket newBucket(String key) {
        return Bucket.builder()
                .addLimit(Bandwidth.builder().capacity(40).refillGreedy(40, Duration.ofMinutes(1)).build())
                .build();
    }

}
