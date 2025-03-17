package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import java.util.List;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Pagamento;

public class AlunoPagamentoResponse {
    private Aluno aluno;
    private List<Pagamento> pagamentos;

    // Construtores, getters e setters
    public AlunoPagamentoResponse(Aluno aluno, List<Pagamento> pagamentos) {
        this.aluno = aluno;
        this.pagamentos = pagamentos;
    }

    // Getters e setters
    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(List<Pagamento> pagamentos) {
        this.pagamentos = pagamentos;
    }
}

