package com.syntaxsquad.ltd.apiCentroTreinamento.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "exercicios")
public class Exercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false)
    private String nome;

    @Min(value = 1, message = "Número de repetições deve ser maior que zero")
    @Max(value = 100, message = "Número de repetições não pode ser maior que 100")
    private Integer repeticoes;
    
    @Min(value = 1, message = "Número de séries deve ser maior que zero")
    @Max(value = 10, message = "Número de séries não pode ser maior que 10")
    private Integer series;
    
    @PositiveOrZero(message = "Carga não pode ser negativa")
    private Float carga;

    // Relacionamento com Treino
    @NotNull(message = "Treino é obrigatório")
    @ManyToOne
    @JoinColumn(name = "treino_id")
    private Treino treino;
} 