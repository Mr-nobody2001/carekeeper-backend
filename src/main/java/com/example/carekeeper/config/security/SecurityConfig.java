package com.example.carekeeper.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Profile ativo: "dev" ou "prod"
    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    // 🔹 Ignora totalmente H2 console apenas em dev
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        if ("dev".equalsIgnoreCase(activeProfile)) {
            return web -> web.ignoring().requestMatchers("/**"); // libera tudo
        }
        // Em prod, não ignora nada
        return web -> {};
    }

    // 🔹 Segurança da API
    @Bean
    public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        if ("dev".equalsIgnoreCase(activeProfile)) {
            // Ambiente dev: libera tudo
            http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        } else {
            // Ambiente prod: segurança real
            http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/autenticacao/login").permitAll()
                    .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic.disable());
        }
        return http.build();
    }
}
