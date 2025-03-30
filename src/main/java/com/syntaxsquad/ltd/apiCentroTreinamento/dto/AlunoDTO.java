package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;
import com.syntaxsquad.ltd.apiCentroTreinamento.serializers.Matricula;

public class AlunoDTO {

    private Matricula matricula;
    private String nome;

  
    // Getters e Setters
    public Matricula getMatricula() {
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
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
