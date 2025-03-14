package com.syntaxsquad.ltd.apiCentroTreinamento.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.syntaxsquad.ltd.apiCentroTreinamento.dto.TreinoDtoRequest;

@Data
@NoArgsConstructor
@Entity
@Table(name = "treinos")
public class Treino {

    public Treino(TreinoDtoRequest treinoDto) {
        this.nome = treinoDto.getNome();
        this.grupoMuscular = treinoDto.getGrupoMuscular();
        this.cargaSugerida = treinoDto.getCargaSugerida();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "Grupo muscular é obrigatório")
    @Size(min = 2, max = 50, message = "Grupo muscular deve ter entre 2 e 50 caracteres")
    @Column(name = "grupo_muscular", nullable = false)
    private String grupoMuscular;

    @PositiveOrZero(message = "Carga sugerida não pode ser negativa")
    @Column(name = "carga_sugerida")
    private Float cargaSugerida;

    // Relacionamento com Exercícios
    @ManyToMany
    @JoinTable(
        name = "treino_exercicio",
        joinColumns = @JoinColumn(name = "treino_id"),
        inverseJoinColumns = @JoinColumn(name = "exercicio_id")
    )
    private List<Exercicio> exercicios;

    // Relacionamento com Alunos
    @ManyToMany
    @JoinTable(
        name = "treino_aluno",
        joinColumns = @JoinColumn(name = "treino_id"),
        inverseJoinColumns = @JoinColumn(name = "aluno_id")
    )
    private List<Aluno> alunos;

}
