package com.reports.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1) Deshabilito CSRF (solo para APIs REST sin cookies)
            .csrf(csrf -> csrf.disable())

            // 2) Configuro CORS usando mi bean de abajo
            .cors(Customizer.withDefaults())

            // 3) Permito todas las peticiones (ninguna requiere login)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/test/**").permitAll()    // Endpoint de prueba
                .anyRequest().permitAll()
            );

        return http.build();
    }

    /**
     * CORS muy laxo para desarrollo: permite cualquier origen, header y método.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();        // Permite múltiples orígenes
        cfg.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",  // Origen común para desarrollo frontend
                "http://localhost:5173",  // Confirmado por el usuario
                "https://finnancy-front.netlify.app", // Origen de producción
                "https://pnaltsw.site",
                "http://finnancy-frontend.us-east-2.elasticbeanstalk.com" // Elastic Beanstalk frontend
        ));
        cfg.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")); // Especifica los métodos
        cfg.setAllowedHeaders(Arrays.asList("*"));             // cualquier cabecera
        cfg.setAllowCredentials(true);                         // si usaras cookies/autorización

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg); // Aplica la configuración a todas las rutas
        return source;
    }
}
