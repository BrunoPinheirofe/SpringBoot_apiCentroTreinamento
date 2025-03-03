package com.syntaxsquad.ltd.apiCentroTreinamento.repositories;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Plano;
import com.syntaxsquad.ltd.apiCentroTreinamento.enums.TipoPlano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PlanoRepository extends JpaRepository<Plano, Long> {
    
    // Busca por nome do plano
    List<Plano> findByNomeContainingIgnoreCase(String nome);
    
    // Busca por tipo de plano
    List<Plano> findByTipoPlano(TipoPlano tipoPlano);
    
    // Busca planos com valor menor que
    List<Plano> findByValorLessThan(BigDecimal valor);
    
    // Busca planos por duração
    List<Plano> findByDuracao(Integer duracao);
    
    // Busca planos ativos com valor entre
    List<Plano> findByValorBetween(BigDecimal valorMinimo, BigDecimal valorMaximo);
} 