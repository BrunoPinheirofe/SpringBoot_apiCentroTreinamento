package com.syntaxsquad.ltd.apiCentroTreinamento.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Data
@Entity
@Table(name = "instrutores")
public class Instrutor {
    @Id
    private String matricula;

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

    @OneToOne
    private User user;

    @PrePersist
    protected void onCreate() {
        this.matricula = generateMatricula();
    }

    private String generateMatricula() {
        LocalDate date = LocalDate.now();
        int randomDigits = new Random().nextInt(9000) + 1000; // generates a 4-digit number
        return date.toString().replace("-", "") + randomDigits;
    }
}