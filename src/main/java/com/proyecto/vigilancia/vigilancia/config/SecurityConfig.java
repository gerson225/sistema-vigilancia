package com.proyecto.vigilancia.vigilancia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.proyecto.vigilancia.vigilancia.security.UsuarioDetailsService;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {

    private final UsuarioDetailsService usuarioDetailsService;

    public SecurityConfig(UsuarioDetailsService usuarioDetailsService) {
        this.usuarioDetailsService = usuarioDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // ✅ PERMITIR TODAS las APIs y páginas - dejar que el interceptor maneje la auth
                .requestMatchers("/**").permitAll()  // 🔥 ESTA ES LA CLAVE
                
                // ❌ ELIMINAR cualquier .anyRequest().authenticated()
            )
            // 🔥 DESHABILITAR el formLogin de Spring Security
            .formLogin(form -> form.disable())
            // 🔥 DESHABILITAR el logout de Spring Security  
            .logout(logout -> logout.disable())
            // 🔥 DESHABILITAR autenticación básica
            .httpBasic(httpBasic -> httpBasic.disable());
        
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return usuarioDetailsService;
    }
}