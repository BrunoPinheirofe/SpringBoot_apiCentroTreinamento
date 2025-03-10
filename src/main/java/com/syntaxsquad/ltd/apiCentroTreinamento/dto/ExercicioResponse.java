package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import lombok.Data;

@Data
public class ExercicioResponse {
    private Long id;
    private String nome;
    private int repeticoes;
    private int series;

}
