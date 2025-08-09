package com.springboot.backend.brahian.usersapp.users_backend.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Configuración de Spring Security para el sistema de autenticación y autorización.
 * Esta clase define qué rutas están protegidas y cuáles son públicas.
 */
@Configuration
public class SpringSecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad.
     * Define qué peticiones requieren autenticación y cuáles son públicas.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(HttpMethod.GET, "/api/users", "/api/users/page/{page}").permitAll();
            auth.requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyRole("ADMIN", "USER");
            auth.requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN");
            auth.requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasRole("ADMIN");
            auth.requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN");
            
            // Todas las demás rutas requieren autenticación
            auth.anyRequest().authenticated();
        })
        // Deshabilita CSRF ya que usamos autenticación stateless (sin sesiones)
        .csrf(csrf -> csrf.disable())
        
        // Configuración stateless: no mantiene sesiones en el servidor
        // Cada petición debe incluir credenciales de autenticación
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        
        // Habilita autenticación HTTP Basic (usuario:contraseña en headers)
        .httpBasic(Customizer.withDefaults()).build();
    }
}
