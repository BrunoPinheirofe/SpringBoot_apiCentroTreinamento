package com.syntaxsquad.ltd.apiCentroTreinamento.models;

import java.time.LocalDate;
import java.util.Random;
import com.syntaxsquad.ltd.apiCentroTreinamento.enums.SexoEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

@Data
@Entity
@Table(name = "alunos")
public class Aluno {
    @Id
    private String matricula;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "Sobrenome é obrigatório")
    @Size(min = 2, max = 100, message = "Sobrenome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false)
    private String sobrenome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "^\\(\\d{2}\\)\\s\\d{4,5}-\\d{4}$", message = "Telefone deve estar no formato (99) 99999-9999")
    @Column(nullable = false)
    private String telefone;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    @Column(nullable = false)
    private LocalDate dataNascimento;

    @NotNull(message = "Data de cadastro é obrigatória")
    @Column(nullable = false)
    private LocalDate dataCadastro = LocalDate.now();

    @Min(value = 0, message = "Idade não pode ser negativa")
    @Max(value = 120, message = "Idade não pode ser maior que 120")
    @Column(nullable = false)
    private int idade;

    @NotNull(message = "Gênero é obrigatório")
    @Enumerated(EnumType.STRING)
    private SexoEnum genero;

    @Size(max = 500, message = "Observação não pode ter mais que 500 caracteres")
    @Column(nullable = true)
    private String observacao;

    @OneToOne
    @PrimaryKeyJoinColumn
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
