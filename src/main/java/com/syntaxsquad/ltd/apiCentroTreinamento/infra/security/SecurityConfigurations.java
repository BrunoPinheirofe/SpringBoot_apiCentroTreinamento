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
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
public class SecurityConfigurations {

    private final JwtFilter jwtFilter;

    public SecurityConfigurations(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
                    // Autorização para GET de exercícios, turmas, presenças, etc. para ALUNO, TREINADOR e ADMIN
                    .requestMatchers(HttpMethod.GET, "/api/exercicios/**")
                        .hasAnyAuthority(UserRole.ALUNO.name(), UserRole.TREINADOR.name(), UserRole.ADMIN.name())
                    .requestMatchers(HttpMethod.GET, "/api/alunos/**")
                        .hasAnyAuthority(UserRole.ALUNO.name(), UserRole.TREINADOR.name(), UserRole.ADMIN.name()) // Alunos também podem acessar
                    .requestMatchers(HttpMethod.GET, "/api/turmas/**")
                        .hasAnyAuthority(UserRole.ALUNO.name(), UserRole.TREINADOR.name(), UserRole.ADMIN.name()) // Turmas
                    .requestMatchers(HttpMethod.GET, "/api/presencas/**")
                        .hasAnyAuthority(UserRole.ALUNO.name(), UserRole.TREINADOR.name(), UserRole.ADMIN.name()) // Presenças
                    .requestMatchers(HttpMethod.GET, "/api/treinos")
                        .hasAnyAuthority(UserRole.ALUNO.name(), UserRole.TREINADOR.name(), UserRole.ADMIN.name()) // Treinos
                    .requestMatchers(HttpMethod.GET, "/api/anamneses/**")
                        .hasAnyAuthority(UserRole.ALUNO.name(), UserRole.TREINADOR.name(), UserRole.ADMIN.name()) // Anamneses
                    .requestMatchers(HttpMethod.GET, "/api/planos/**")
                        .hasAnyAuthority(UserRole.ALUNO.name(), UserRole.TREINADOR.name(), UserRole.ADMIN.name()) // Planos
                  
                    // Autorização de admin
                    .requestMatchers("/api/administradores/**")
                        .hasAuthority(UserRole.ADMIN.name())
                    .requestMatchers("/api/alunos/**")
                        .hasAuthority(UserRole.ADMIN.name())
                    .requestMatchers("/api/turmas/**")
                        .hasAuthority(UserRole.ADMIN.name())
                    .requestMatchers("/api/presencas/**")
                        .hasAuthority(UserRole.ADMIN.name())
                    .requestMatchers("/api/treinos/**")
                        .hasAuthority(UserRole.ADMIN.name())
                    .requestMatchers("/api/anamneses/**")
                        .hasAuthority(UserRole.ADMIN.name())
                    .requestMatchers("/api/planos/**")
                        .hasAuthority(UserRole.ADMIN.name())
                    .requestMatchers("/api/exercicios/**")
                        .hasAuthority(UserRole.ADMIN.name())
                    .requestMatchers("/api/instrutores/**")
                        .hasAuthority(UserRole.ADMIN.name())
                    .requestMatchers("/api/mercado-pago/**")
                        .hasAuthority(UserRole.ADMIN.name())
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

  

    
}
