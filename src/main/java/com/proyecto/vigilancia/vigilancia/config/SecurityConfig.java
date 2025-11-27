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
        .requestMatchers("/api/login", "/dashboard","/camaras", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
        .anyRequest().authenticated()
    )
    .formLogin(form -> form
        .loginPage("/login")       //página de login
        .permitAll()
    )
    .logout(logout -> logout
        .logoutUrl("/logout")
        .permitAll()
    );
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Aquí ya no devolvemos null, usamos tu servicio real
    @Bean
    public UserDetailsService userDetailsService() {
        return usuarioDetailsService;
    }
}
