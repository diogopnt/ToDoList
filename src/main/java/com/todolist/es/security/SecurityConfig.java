package com.todolist.es.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // Desabilitar CSRF
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.OPTIONS, "/api/v1/**").permitAll() // Permitir OPTIONS sem autenticação
                .requestMatchers("/api/v1/**").authenticated() // Proteger todas as rotas da API
                .anyRequest().permitAll()  // Permitir todas as outras requisições
                .and()
                .httpBasic(); // Usar autenticação básica

        return http.build();
    }
}

