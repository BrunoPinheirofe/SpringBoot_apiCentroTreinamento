package com.syntaxsquad.ltd.apiCentroTreinamento.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


import com.syntaxsquad.ltd.apiCentroTreinamento.serializers.Matricula;

@Data
@Entity
@Table(name = "instrutores")
public class Instrutor {
    @Id
    @Embedded
    private Matricula matricula;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "Sobrenome é obrigatório")
    @Size(min = 2, max = 100, message = "Sobrenome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false)
    private String sobrenome;

    @NotBlank(message = "Especialidade é obrigatória")
    @Size(min = 2, max = 100, message = "Especialidade deve ter entre 2 e 100 caracteres")
    @Column(nullable = false)
    private String especialidade;


    @Size(min = 8, max = 20, message = "Telefone deve ter entre 8 e 20 caracteres")
    private String telefone;

    @Size(min = 8, max = 20, message = "Celular deve ter entre 8 e 20 caracteres")
    private LocalDate dataNascimento;

    @Column(nullable = false)
    private String email;

    // Relacionamento com Turma
    @OneToMany(mappedBy = "instrutor")
    private List<Turma> turmas;

    @OneToOne
    @JoinColumn(name = "email_user", referencedColumnName = "email")
    private User user;

    @PrePersist
    protected void onCreate() {
        this.matricula = Matricula.gerarMatricula();
    }


}