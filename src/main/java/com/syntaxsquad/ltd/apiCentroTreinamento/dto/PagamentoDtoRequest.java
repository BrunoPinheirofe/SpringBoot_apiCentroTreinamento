package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagamentoDtoRequest {

    @NotNull(message = "A matricula do aluno é obrigatório.")
    private String matriculaAluno;

    @NotNull(message = "O ID do plano é obrigatório.")
    private Long planoId;

    @NotNull(message = "A data do pagamento é obrigatória.")
    private LocalDate dataPagamento;


    @NotNull(message = "A data de vencimento é obrigatória.")
    private LocalDate dataVencimento;

    @NotNull(message = "A forma de pagamento é obrigatória.")
    private String formaPagamento;

}
