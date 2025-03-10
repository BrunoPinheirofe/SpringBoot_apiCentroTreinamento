package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import java.time.LocalDate;

import com.syntaxsquad.ltd.apiCentroTreinamento.enums.SexoEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlunoDtoResponse {


    private String matricula;
    private String nome;
    private String sobrenome;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    private LocalDate dataCadastro;
    private int idade;
    private SexoEnum genero;
    private String observacao;


    public AlunoDtoResponse(
    String matricula, 
    String nome, 
    String sobrenome, 
    String email, 
    String telefone, 
    LocalDate dataNascimento, 
    LocalDate dataCadastro, 
    int idade, 
    SexoEnum genero, 
    String observacao
)
    {
        this.matricula = matricula;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.dataCadastro = dataCadastro;
        this.idade = idade;
        this.genero = genero;
        this.observacao = observacao;
    }
    

}
