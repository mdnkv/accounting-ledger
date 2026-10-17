package dev.mednikov.accounting.ledger.shared.config;

import dev.mednikov.accounting.ledger.shared.authorization.AuthorizationFilter;
import dev.mednikov.accounting.ledger.shared.authorization.AuthorizationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class AuthorizationConfig {

    private final AuthorizationProvider authorizationProvider;

    public AuthorizationConfig(AuthorizationProvider authorizationProvider) {
        this.authorizationProvider = authorizationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/error/**").permitAll()
                        .anyRequest().authenticated()
                )
                // Register our custom filter before the standard authentication stage
                .addFilterBefore(new AuthorizationFilter(authManager), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.authenticationProvider(authorizationProvider);
        return authManagerBuilder.build();
    }
}
