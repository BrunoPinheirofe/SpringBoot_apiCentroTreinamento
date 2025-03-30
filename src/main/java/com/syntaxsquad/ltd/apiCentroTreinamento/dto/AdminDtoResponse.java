package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import java.time.LocalDate;

import com.syntaxsquad.ltd.apiCentroTreinamento.enums.SexoEnum;
import com.syntaxsquad.ltd.apiCentroTreinamento.serializers.Matricula;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDtoResponse {
    private Matricula matricula;
    private String nome;
    private String sobrenome;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    private LocalDate dataCadastro;

    public AdminDtoResponse(
    Matricula matricula, 
    String nome, 
    String sobrenome, 
    String email, 
    String telefone, 
    LocalDate dataNascimento, 
    LocalDate dataCadastro)

    {
        this.matricula = matricula;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.dataCadastro = dataCadastro;
       
    }

}
