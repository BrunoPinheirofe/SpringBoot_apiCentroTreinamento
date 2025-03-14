package com.syntaxsquad.ltd.apiCentroTreinamento.dto;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Exercicio;

import lombok.Data;
import lombok.Getter;


@Data
public class ExercicioDtoResponse {
    private Long id;
    private String nome;
    private Integer series;
    private Integer repeticoes;

    public ExercicioDtoResponse(Exercicio exercicio) {
        this.id = exercicio.getId();
        this.nome = exercicio.getNome();
        this.series = exercicio.getSeries();
        this.repeticoes = exercicio.getRepeticoes();
    }
}
