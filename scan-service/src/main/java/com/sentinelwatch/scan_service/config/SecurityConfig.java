package com.sentinelwatch.scan_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // AGREGA ESTO: Habilita el soporte para proxies (como el Gateway)
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))

            // 1. Desactivamos CSRF para habilitar WebSockets y AJAX
            .csrf(csrf -> csrf.disable()) 
            
            .authorizeHttpRequests(auth -> auth
                // 2. Permitimos acceso a registro, login y recursos estáticos
                .requestMatchers("/register", "/login", "/css/**", "/js/**").permitAll()
                // 3. Permitimos el túnel de WebSockets
                .requestMatchers("/ws-sentinel/**").permitAll()
                // 4. Todo lo demás requiere estar logueado
                .anyRequest().authenticated()
            ) // Cerramos el bloque de authorizeHttpRequests
            
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
        
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
