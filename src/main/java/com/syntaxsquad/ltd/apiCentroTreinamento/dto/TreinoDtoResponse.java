package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

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
    private List<AlunoDTO> alunos;  // Lista de alunos com nome e matrícula
    private List<ExercicioDtoResponse> exercicios;  // Lista de exercícios com nome e id

    public TreinoDtoResponse(Treino treino) {
        this.id = treino.getId();
        this.nome = treino.getNome();
        this.grupoMuscular = treino.getGrupoMuscular();
        this.cargaSugerida = treino.getCargaSugerida();

        // Mapear alunos
        this.alunos = treino.getAlunos().stream()
                .map(aluno -> new AlunoDTO(aluno))
                .collect(Collectors.toList());

        // Mapear exercícios
        this.exercicios = treino.getExercicios().stream()
                .map(exercicio -> new ExercicioDtoResponse(exercicio))
                .collect(Collectors.toList());
    }
}