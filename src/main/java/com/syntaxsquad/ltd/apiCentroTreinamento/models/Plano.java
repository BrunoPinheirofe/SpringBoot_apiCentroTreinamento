package com.syntaxsquad.ltd.apiCentroTreinamento.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

import com.syntaxsquad.ltd.apiCentroTreinamento.enums.TipoPlano;

@Data
@Entity
@Table(name = "planos")
public class Plano {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false)
    private String nome;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser maior que zero")
    @Column(nullable = false)
    private BigDecimal valor;

    @Min(value = 1, message = "Duração deve ser maior que zero")
    @Max(value = 12, message = "Duração não pode ser maior que 12 meses")
    @Column(name = "duracao")
    private Integer duracao;

    @NotNull(message = "Tipo do plano é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_plano")
    private TipoPlano tipoPlano;

    @Size(max = 500, message = "Descrição não pode ter mais que 500 caracteres")
    @Column(name = "descricao")
    private String descricao;
} 