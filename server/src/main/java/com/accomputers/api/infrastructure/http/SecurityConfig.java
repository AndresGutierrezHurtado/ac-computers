package com.accomputers.api.infrastructure.http;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// Responses & Filters
import com.accomputers.api.infrastructure.http.responses.CustomAccessDeniedHandler;
import com.accomputers.api.infrastructure.http.responses.CustomAuthenticationEntryPoint;
import com.accomputers.api.infrastructure.security.JwtAuthenticationFilter;
import com.accomputers.api.infrastructure.logging.LoggingContextFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RateLimiter rateLimiter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final LoggingContextFilter loggingContextFilter;

    @Autowired
    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            RateLimiter rateLimiter,
            CustomAuthenticationEntryPoint authenticationEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler,
            LoggingContextFilter loggingContextFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.rateLimiter = rateLimiter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.loggingContextFilter = loggingContextFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Auth endpoints
                        .requestMatchers("/auth/**").permitAll()

                        // Contact form
                        .requestMatchers(HttpMethod.POST, "/contact").permitAll()

                        // User management
                        .requestMatchers(HttpMethod.GET, "/users/**").hasAnyRole("SUPERUSER", "ADMINISTRATOR", "VIEWER")
                        .requestMatchers("/users/**").hasRole("SUPERUSER")

                        // Products (public catalog)
                        .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/products/recommendations").permitAll()

                        // Products (management)
                        .requestMatchers(HttpMethod.POST, "/products").hasAnyRole("SUPERUSER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PUT, "/products/**").hasAnyRole("SUPERUSER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.DELETE, "/products/**").hasAnyRole("SUPERUSER", "ADMINISTRATOR")
                        .anyRequest().authenticated())
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(rateLimiter, JwtAuthenticationFilter.class)
                .addFilterAfter(loggingContextFilter, JwtAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
