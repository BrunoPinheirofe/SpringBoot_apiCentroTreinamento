package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Pagamento;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagamentoDtoResponse {

    public PagamentoDtoResponse(Pagamento p) {
        
        this.idPlano = p.getId();
        this.status = p.getStatus();
        this.email = p.getAluno().getEmail();
        this.metodoPagamento = p.getFormaPagamento();
        this.valor = p.getPlano().getValor();
        this.dataPagamento = p.getDataPagamento();
        this.dataVencimento = p.getVencimento();
    }
    private String idPlano;
    private String status;
    private String email;
    private String metodoPagamento;
    private BigDecimal  valor;
    private LocalDate dataPagamento;
    private LocalDate dataVencimento;
    private LocalDate dataCadastro;
    private String observacao;
    

}
