package com.syntaxsquad.ltd.apiCentroTreinamento.infra.security;

import com.syntaxsquad.ltd.apiCentroTreinamento.enums.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfigurations {

    private final JwtFilter jwtFilter;

    public SecurityConfigurations(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
                    // Autorização de aluno 
                    .requestMatchers(HttpMethod.GET, "/api/exercicios/**").hasAuthority(UserRole.ALUNO.name())
                    .requestMatchers(HttpMethod.GET, "/api/alunos/**").hasAuthority(UserRole.ALUNO.name())
                    .requestMatchers(HttpMethod.GET, "/api/turmas/**").hasAuthority(UserRole.ALUNO.name())
                    .requestMatchers(HttpMethod.GET, "/api/presencas/**").hasAuthority(UserRole.ALUNO.name())
                    .requestMatchers(HttpMethod.GET, "/api/treinos").hasAuthority(UserRole.ALUNO.name())
                    .requestMatchers(HttpMethod.GET, "/api/anamneses/**").hasAuthority(UserRole.ALUNO.name())
                    .requestMatchers(HttpMethod.GET, "/api/planos/**").hasAuthority(UserRole.ALUNO.name())
                    // Autorização de treinador

                    // Autorização de admin
                    .requestMatchers( "/api/administradores/**").hasAuthority(UserRole.ADMIN.name())
                    .requestMatchers( "/api/alunos/**").hasAuthority(UserRole.ADMIN.name())
                    .requestMatchers( "/api/turmas/**").hasAuthority(UserRole.ADMIN.name())
                    .requestMatchers( "/api/presencas/**").hasAuthority(UserRole.ADMIN.name())
                    .requestMatchers( "/api/treinos/**").hasAuthority(UserRole.ADMIN.name())
                    .requestMatchers( "/api/anamneses/**").hasAuthority(UserRole.ADMIN.name())
                    .requestMatchers( "/api/planos/**").hasAuthority(UserRole.ADMIN.name())
                    .requestMatchers( "/api/exercicios/**").hasAuthority(UserRole.ADMIN.name())
                
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
