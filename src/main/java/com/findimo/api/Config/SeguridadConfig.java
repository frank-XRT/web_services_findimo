package com.findimo.api.Config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SeguridadConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(@Value("${findimo.frontend.origin:}") String origen) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration sesion = new CorsConfiguration(configuration);
        sesion.setAllowedOriginPatterns(List.of());
        var origenes = new ArrayList<>(List.of("http://localhost:4200", "http://127.0.0.1:4200"));
        if (!origen.isBlank()) origenes.add(origen.strip());
        sesion.setAllowedOrigins(origenes);
        source.registerCorsConfiguration("/api/usuarios/**", sesion);
        source.registerCorsConfiguration("/api/admin/**", sesion);
        source.registerCorsConfiguration("/auditorias", sesion);
        source.registerCorsConfiguration("/api/resenas/perfil", sesion);
        source.registerCorsConfiguration("/api/solicitudes-alquiler", sesion);
        source.registerCorsConfiguration("/api/mensajes/**", sesion);
        source.registerCorsConfiguration("/api/propiedades", sesion);
        source.registerCorsConfiguration("/api/propiedades/**", sesion);
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
