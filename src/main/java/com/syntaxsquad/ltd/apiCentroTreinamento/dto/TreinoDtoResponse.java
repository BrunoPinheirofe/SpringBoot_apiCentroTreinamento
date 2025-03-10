package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import java.util.List;

public class TreinoDtoResponse {
    private Long id;
    private String nome;
    private String grupoMuscular;
    private Float cargaSugerida;
    private List<ExercicioResponseDto> exercicios; // Lista de exercícios

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

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }

    public Float getCargaSugerida() {
        return cargaSugerida;
    }

    public void setCargaSugerida(Float cargaSugerida) {
        this.cargaSugerida = cargaSugerida;
    }

    public List<ExercicioResponseDto> getExercicios() {
        return exercicios;
    }

    public void setExercicios(List<ExercicioResponseDto> exercicios) {
        this.exercicios = exercicios;
    }
}
