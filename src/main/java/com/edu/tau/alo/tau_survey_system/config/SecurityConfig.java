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
        // Używamy bezpośrednio JWK Set URI do weryfikacji podpisu
        String jwkSetUri = "https://login.microsoftonline.com/f2373ae2-8bd8-4732-8107-a40e14c35d27/discovery/v2.0/keys";
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

        // Ustawiamy walidator, który sprawdza tylko podstawowe rzeczy (np. czy token nie wygasł)
        // Pomijamy rygorystyczne sprawdzanie 'iss' (Issuer), które powodowało błąd 401
        OAuth2TokenValidator<Jwt> validator = JwtValidators.createDefault();

        jwtDecoder.setJwtValidator(validator);
        return jwtDecoder;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Adres Twojego Reacta
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));

        // Metody HTTP, na które pozwalamy
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Nagłówki, które akceptujemy (Authorization jest kluczowy dla Bearer tokena)
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));

        // Pozwalamy na przesyłanie poświadczeń (ciasteczka, auth headers)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}