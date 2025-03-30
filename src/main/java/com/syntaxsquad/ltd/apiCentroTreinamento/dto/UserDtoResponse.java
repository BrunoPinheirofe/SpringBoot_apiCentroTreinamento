package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDtoResponse {
    private String email;
    private String role;
    private String nome;
    private String sobrenome;
    private LocalDate dataNascimento;
    private String matricula;
    // outros campos possíveis...

    // Construtor que deve existir
    public UserDtoResponse(String email, String role, String nome, String sobrenome,
                           LocalDate dataNascimento, String matricula) {
        this.email = email;
        this.role = role;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dataNascimento = dataNascimento;
        this.matricula = matricula;
    }

    // outros construtores e métodos...
}