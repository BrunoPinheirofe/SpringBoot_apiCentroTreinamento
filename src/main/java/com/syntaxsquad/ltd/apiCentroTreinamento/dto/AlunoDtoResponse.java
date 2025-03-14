package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import java.time.LocalDate;

import com.syntaxsquad.ltd.apiCentroTreinamento.enums.SexoEnum;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;

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
    
    public AlunoDtoResponse( Aluno aluno ) {
        this.matricula = aluno.getMatricula();
        this.nome = aluno.getNome();
        this.sobrenome = aluno.getSobrenome();
        this.email = aluno.getEmail();
        this.telefone = aluno.getTelefone();
        this.dataNascimento = aluno.getDataNascimento();
        this.dataCadastro = aluno.getDataCadastro();
        this.idade = aluno.getIdade();
        this.genero = aluno.getGenero();
        this.observacao = aluno.getObservacao();
    }

}
