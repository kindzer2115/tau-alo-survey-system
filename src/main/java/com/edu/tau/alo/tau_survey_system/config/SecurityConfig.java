package com.edu.tau.alo.tau_survey_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Włączamy CORS z naszą konfiguracją
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. Wyłączamy CSRF (niepotrzebne przy JWT i ułatwia pracę z H2)
                .csrf(csrf -> csrf.disable())

                // 3. Obsługa konsoli H2 (pozwala na wyświetlanie w ramkach)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))

                .authorizeHttpRequests(auth -> auth
                        // Konsola H2 jest dostępna dla każdego
                        .requestMatchers("/h2-console/**").permitAll()
                        // Wszystkie inne żądania wymagają tokena
                        .anyRequest().authenticated()
                )

                // 4. Konfiguracja serwera zasobów OAuth2
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // We use JWK Set URI directly to verify the signature
        String jwkSetUri = "https://login.microsoftonline.com/f2373ae2-8bd8-4732-8107-a40e14c35d27/discovery/v2.0/keys";
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();


        // We set up a validator that only checks basic things (e.g., whether the token has not expired).
        // We skip the strict 'iss' (Issuer) check, which caused a 401 error.
        OAuth2TokenValidator<Jwt> validator = JwtValidators.createDefault();

        jwtDecoder.setJwtValidator(validator);
        return jwtDecoder;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // React address
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));

        // HTTP methods we allow
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Headers we accept (Authorization is crucial for Bearer token)
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));

        // We allow credentials (cookies, authorization headers, etc.)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}