package com.hunterkilltree.keycloak_be.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // all endpoint in this will not require token
    private final String[] PUBLIC_ENDPOINTS = {"/register"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> request.requestMatchers(PUBLIC_ENDPOINTS)
                .permitAll()
                .anyRequest()
                .authenticated()); // all request require authentication

        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(Customizer.withDefaults()).authenticationEntryPoint(new JwtAuthenticationEntryPoint()));

        // disable csrf because we use restful API and FE is react app js
        // so we don't need to check csrf
        // if we not disable, method post, put will return error
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }
}
