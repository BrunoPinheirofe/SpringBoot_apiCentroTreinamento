package com.syntaxsquad.ltd.apiCentroTreinamento.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "presencas")
public class Presenca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Data da presença é obrigatória")
    @Column(name = "data_presenca", nullable = false)
    private LocalDateTime dataPresenca;

    @NotNull(message = "Status de presença é obrigatório")
    @Column(nullable = false)
    private Boolean presente;

    @Size(max = 500, message = "Observação não pode ter mais que 500 caracteres")
    private String observacao;

    @NotNull(message = "Aluno é obrigatório")
    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @NotNull(message = "Turma é obrigatória")
    @ManyToOne
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @NotNull(message = "Instrutor é obrigatório")
    @ManyToOne
    @JoinColumn(name = "instrutor_id", nullable = false)
    private Instrutor instrutor;
} 