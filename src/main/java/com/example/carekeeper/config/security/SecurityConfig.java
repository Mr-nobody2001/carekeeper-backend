package com.example.carekeeper.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        if ("dev".equalsIgnoreCase(activeProfile)) {
            // 🔓 Ambiente DEV: tudo liberado + H2 console
            http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/h2-console/**").permitAll() // libera H2
                    .anyRequest().permitAll()
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable())); // permite iframe

        } else {
            // 🔒 Ambiente PROD: segurança com JWT
            http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/autenticacao/login").permitAll()
                    .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(httpBasic -> httpBasic.disable());
        }

        return http.build();
    }
}
