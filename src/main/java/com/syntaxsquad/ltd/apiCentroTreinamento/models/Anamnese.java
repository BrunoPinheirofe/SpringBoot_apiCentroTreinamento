package com.syntaxsquad.ltd.apiCentroTreinamento.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import com.syntaxsquad.ltd.apiCentroTreinamento.enums.ClassificacaoIMC;


@Data
@Entity
@Table(name = "anamneses")
public class Anamnese {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Data de cadastro é obrigatória")
    @Column(nullable = false)
    private LocalDate dataCadastro = LocalDate.now();

    // Histórico de Saúde
    @Size(max = 1000, message = "Histórico de saúde não pode ter mais que 1000 caracteres")
    @Column(name = "historico_saude", columnDefinition = "TEXT")
    private String historicoSaude;

    @Size(max = 1000, message = "Histórico familiar não pode ter mais que 1000 caracteres")
    @Column(name = "historico_familiar", columnDefinition = "TEXT")
    private String historicoFamiliar;

    @NotBlank(message = "Medicamentos é obrigatório")
    private String medicamentos;

    @Size(max = 500, message = "Alergias não pode ter mais que 500 caracteres")
    private String alergias;

    @Size(max = 500, message = "Cirurgias não pode ter mais que 500 caracteres")
    private String cirurgias;

    @Size(max = 500, message = "Lesões não pode ter mais que 500 caracteres")
    private String lesoes;

    private Boolean praticaAtividade;

    @Size(max = 100, message = "Atividade não pode ter mais que 100 caracteres")
    private String qualAtividade;

    @NotBlank(message = "Objetivo do treino é obrigatório")
    @Size(max = 500, message = "Objetivo não pode ter mais que 500 caracteres")
    @Column(name = "objetivo_treino", columnDefinition = "TEXT")
    private String objetivoTreino;

    @Size(max = 500, message = "Restrições não pode ter mais que 500 caracteres")
    private String restricoes;

    @Size(max = 500, message = "Observações não pode ter mais que 500 caracteres")
    private String observacoes;

    // Relacionamento com Aluno (One-to-One pois cada aluno tem uma anamnese)
    @NotNull(message = "Aluno é obrigatório")
    @OneToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    // Relacionamento com o Instrutor que fez a anamnese
    @NotNull(message = "Instrutor é obrigatório")
    @ManyToOne
    @JoinColumn(name = "instrutor_id", nullable = false)
    private Instrutor instrutor;
}

