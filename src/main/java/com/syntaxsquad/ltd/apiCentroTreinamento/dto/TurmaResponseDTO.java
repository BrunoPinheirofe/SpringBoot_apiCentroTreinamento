package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Instrutor;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Exercicio;  // Importando o modelo de Exercicio

import java.util.List;

public class TurmaResponseDTO {

    private Long id;
    private String nome;
    private String horario;
    private String diaSemana;
    private InstrutorDTO instrutor;
    private List<AlunoDTO> alunos;
    private List<ExercicioResponseDto> exercicios;  // Nova lista para os exercícios

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public InstrutorDTO getInstrutor() {
        return instrutor;
    }

    public void setInstrutor(InstrutorDTO instrutor) {
        this.instrutor = instrutor;
    }

    public List<AlunoDTO> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<AlunoDTO> alunos) {
        this.alunos = alunos;
    }

    public List<ExercicioResponseDto> getExercicios() {
        return exercicios;
    }

    public void setExercicios(List<ExercicioResponseDto> exercicios) {
        this.exercicios = exercicios;
    }
}
