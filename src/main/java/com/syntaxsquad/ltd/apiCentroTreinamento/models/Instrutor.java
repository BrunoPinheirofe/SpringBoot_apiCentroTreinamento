package com.syntaxsquad.ltd.apiCentroTreinamento.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "instrutores")
public class Instrutor{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "Especialidade é obrigatória")
    @Size(min = 2, max = 100, message = "Especialidade deve ter entre 2 e 100 caracteres")
    @Column(nullable = false)
    private String especialidade;

    // Relacionamento com Turma
    @OneToMany(mappedBy = "instrutor")
    private List<Turma> turmas;
} 