package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import lombok.Data;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Treino;



@Data
public class ExercicioRequestDto {
    private String nome;
    private int repeticoes;
    private int series;

}

