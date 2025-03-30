package com.syntaxsquad.ltd.apiCentroTreinamento.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
@Entity
@Table(name = "turmas")
public class Turma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "Horário é obrigatório")
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Horário deve estar no formato HH:mm")
    @Column(nullable = false)
    private String horario;

    @NotBlank(message = "Dia da semana é obrigatório")
    @Pattern(regexp = "^(Segunda|Terça|Quarta|Quinta|Sexta|Sábado|Domingo)$", message = "Dia da semana inválido")
    @Column(name = "dia_semana", nullable = false)
    private String diaSemana;

    // Relacionamento com Instrutor
    @NotNull(message = "Instrutor é obrigatório")
    @ManyToOne
    @JoinColumn(name = "instrutor_id", nullable = false)
    private Instrutor instrutor;

    // Relacionamento com Alunos
    @ManyToMany
    @JoinTable(name = "turma_alunos", joinColumns = @JoinColumn(name = "turma_id"), inverseJoinColumns = @JoinColumn(name = "aluno_id"))
    private List<Aluno> alunos;
}