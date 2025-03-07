package com.syntaxsquad.ltd.apiCentroTreinamento.enums;

public enum ClassificacaoIMC {
    ABAIXO_DO_PESO("Abaixo do peso", "< 18.5"),
    NORMAL("Normal", "18.5 - 24.9"),
    SOBREPESO("Sobrepeso", "25.0 - 29.9"),
    OBESO_GRAU_1("Obesidade Grau 1", "30.0 - 34.9"),
    OBESO_GRAU_2("Obesidade Grau 2", "35.0 - 39.9"),
    OBESO_GRAU_3("Obesidade Grau 3", ">= 40.0");

    private String descricao;
    private String faixa;

    ClassificacaoIMC(String descricao, String faixa) {
        this.descricao = descricao;
        this.faixa = faixa;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getFaixa() {
        return faixa;
    }

    public static ClassificacaoIMC getClassificacao(float imc) {
        if (imc < 18.5) return ABAIXO_DO_PESO;
        if (imc < 25.0) return NORMAL;
        if (imc < 30.0) return SOBREPESO;
        if (imc < 35.0) return OBESO_GRAU_1;
        if (imc < 40.0) return OBESO_GRAU_2;
        return OBESO_GRAU_3;
    }
} 