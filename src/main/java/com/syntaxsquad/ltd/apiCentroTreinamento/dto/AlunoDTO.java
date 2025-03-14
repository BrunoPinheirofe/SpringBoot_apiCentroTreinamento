package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;

public class AlunoDTO {

    private String matricula;
    private String nome;

  
    // Getters e Setters
    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    
    public AlunoDTO(Aluno dto) {
        this.matricula =dto.getMatricula();
        this.nome = dto.getNome();
    }

    public AlunoDTO() {}
}
