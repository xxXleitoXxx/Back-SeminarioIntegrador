package org.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, CustomUserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // 👇 Siempre con "/" inicial
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/horario/**").hasRole("RECEPCIONISTA")
                        .requestMatchers("/api/v1/tipoclases**").hasRole("RECEPCIONISTA")
                        .requestMatchers("/api/v1/alumnos").hasRole("RECEPCIONISTA")
                        .requestMatchers("/api/v1/claseAlumno/asistencia/**").hasRole("RECEPCIONISTA")
                        .requestMatchers("/api/v1/dias").hasRole("RECEPCIONISTA")
                        .requestMatchers("/api/v1/localidades").hasRole("RECEPCIONISTA")
                        .requestMatchers("/api/v1/cronograma").hasRole("RECEPCIONISTA")
                        .requestMatchers("/api/v1/alumnos/**").hasRole("RECEPCIONISTA")
                        .requestMatchers("/api/v1/claseAlumno/**").hasRole("RECEPCIONISTA")
                        .requestMatchers("/api/v1/clases").hasRole("RECEPCIONISTA")
                        .requestMatchers("/api/v1/clases/**").hasRole("RECEPCIONISTA")
                        .requestMatchers("/api/v1/fichaMedica/**").hasRole("RECEPCIONISTA")
                        .requestMatchers("/api/v1/Inscripcion/**").hasRole("RECEPCIONISTA")
                        .requestMatchers("/api/v1/InscripcionProfesor/**").hasRole("RECEPCIONISTA")
                        .requestMatchers("/api/v1/**").hasRole("ADMIN")
                        .anyRequest().authenticated()

                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*")); // cualquier origen
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
