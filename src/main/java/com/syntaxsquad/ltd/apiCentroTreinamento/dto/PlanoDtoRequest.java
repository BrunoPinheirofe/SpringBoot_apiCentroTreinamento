package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import com.syntaxsquad.ltd.apiCentroTreinamento.enums.TipoPlano;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanoDtoRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser maior que zero")
    private BigDecimal valor;

    @Min(value = 1, message = "Duração deve ser maior que zero")
    @Max(value = 12, message = "Duração não pode ser maior que 12 meses")
    private Integer duracao;

    @NotNull(message = "Tipo do plano é obrigatório")
    private TipoPlano tipoPlano;

    @Size(max = 500, message = "Descrição não pode ter mais que 500 caracteres")
    private String descricao;

}
