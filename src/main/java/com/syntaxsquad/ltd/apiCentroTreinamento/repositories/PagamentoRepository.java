package com.syntaxsquad.ltd.apiCentroTreinamento.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Pagamento;

public interface PagamentoRepository extends JpaRepository <Pagamento, Long> {

}
