package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Exercicio;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Treino;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TreinoDtoResponse {
    private Long id;
    private String nome;
    private String grupoMuscular;
    private Float cargaSugerida;
    private List<String> alunos;
    private List<String> exercicios;

    public TreinoDtoResponse(Treino treino) {
        this.id = treino.getId();
        this.nome = treino.getNome();
        this.grupoMuscular = treino.getGrupoMuscular();
        this.cargaSugerida = treino.getCargaSugerida();
        
        // Convertendo a lista de alunos para uma lista de nomes
        this.alunos = treino.getAlunos().stream()
                .map(Aluno::getNome)  // Aqui você pode ajustar para o campo que desejar
                .collect(Collectors.toList());

        // Convertendo a lista de exercícios para uma lista de nomes
        this.exercicios = treino.getExercicios().stream()
                .map(Exercicio::getNome)  // Aqui você pode ajustar para o campo que desejar
                .collect(Collectors.toList());
    }
}
