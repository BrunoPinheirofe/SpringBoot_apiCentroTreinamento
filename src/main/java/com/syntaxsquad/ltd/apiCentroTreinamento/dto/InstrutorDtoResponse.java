package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import java.util.List;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Turma;
import com.syntaxsquad.ltd.apiCentroTreinamento.serializers.Matricula;

public class InstrutorDtoResponse {
    private Matricula matricula;
    private String nome;
    private String especialidade;
    private String email;
    private List<Turma> turmas;

    // Constructor with all fields
    public InstrutorDtoResponse(Matricula matricula, String nome, String especialidade, String email, List<Turma> turmas) {
        this.matricula = matricula;
        this.nome = nome;
        this.especialidade = especialidade;
        this.email = email;
        this.turmas = turmas;
    }

    // Getters and setters for each field
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

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Turma> getTurmas() {
        return turmas;
    }

    public void setTurmas(List<Turma> turmas) {
        this.turmas = turmas;
    }
}
