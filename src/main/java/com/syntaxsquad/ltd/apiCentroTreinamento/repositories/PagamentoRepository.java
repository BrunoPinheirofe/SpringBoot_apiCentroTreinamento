package com.syntaxsquad.ltd.apiCentroTreinamento.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Pagamento;
import com.syntaxsquad.ltd.apiCentroTreinamento.serializers.Matricula;

public interface PagamentoRepository extends JpaRepository <Pagamento, String> {

    Pagamento findByAluno(Aluno aluno);

    List<Pagamento> findByAlunoMatricula(Matricula matricula);

   

}
