package com.syntaxsquad.ltd.apiCentroTreinamento.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "treinos")
public class Treino {
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
  
    @OneToMany(mappedBy = "treino", cascade = CascadeType.ALL)
    private List<Exercicio> exercicios;

} 