package com.syntaxsquad.ltd.apiCentroTreinamento.dto;



import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public class TreinoDtoRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Grupo muscular é obrigatório")
    private String grupoMuscular;

    @PositiveOrZero(message = "Carga sugerida não pode ser negativa")
    private Float cargaSugerida;

    @NotEmpty(message = "A lista de IDs dos exercícios não pode ser vazia")
    private List<Long> exerciciosIds; // Lista de IDs dos exercícios

    // Getters e Setters
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

    public List<Long> getExerciciosIds() {
        return exerciciosIds;
    }

    public void setExerciciosIds(List<Long> exerciciosIds) {
        this.exerciciosIds = exerciciosIds;
    }
}
