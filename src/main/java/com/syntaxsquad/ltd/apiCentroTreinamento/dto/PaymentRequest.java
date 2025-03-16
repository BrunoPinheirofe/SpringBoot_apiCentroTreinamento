package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import java.math.BigDecimal;

public class PaymentRequest {

    private String emailAluno;
    private String nomeAluno;
    private String sobrenomeAluno;
    private BigDecimal valorPlano;
    private String formaPagamento;
    private String descricaoPlano;
    private String tipoPlano;  // Se necessário, adicione o tipo do plano

    // Getters e Setters
    public String getEmailAluno() {
        return emailAluno;
    }

    public void setEmailAluno(String emailAluno) {
        this.emailAluno = emailAluno;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getSobrenomeAluno() {
        return sobrenomeAluno;
    }

    public void setSobrenomeAluno(String sobrenomeAluno) {
        this.sobrenomeAluno = sobrenomeAluno;
    }

    public BigDecimal getValorPlano() {
        return valorPlano;
    }

    public void setValorPlano(BigDecimal valorPlano) {
        this.valorPlano = valorPlano;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getDescricaoPlano() {
        return descricaoPlano;
    }

    public void setDescricaoPlano(String descricaoPlano) {
        this.descricaoPlano = descricaoPlano;
    }

    public String getTipoPlano() {
        return tipoPlano;
    }

    public void setTipoPlano(String tipoPlano) {
        this.tipoPlano = tipoPlano;
    }
}
