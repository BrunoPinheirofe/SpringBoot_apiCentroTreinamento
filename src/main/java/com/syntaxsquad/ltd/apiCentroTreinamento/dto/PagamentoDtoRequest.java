package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.syntaxsquad.ltd.apiCentroTreinamento.serializers.Matricula;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PagamentoDtoRequest {

    @NotNull(message = "A matricula do aluno é obrigatório.")
    private Matricula matriculaAluno;

    @NotNull(message = "O ID do plano é obrigatório.")
    private Long planoId;

    @NotNull(message = "A data do pagamento é obrigatória.")
    private LocalDate dataPagamento;


    @NotNull(message = "A data de vencimento é obrigatória.")
    private LocalDate dataVencimento;

    @NotNull(message = "A forma de pagamento é obrigatória.")
    private String formaPagamento;

}
