package com.syntaxsquad.ltd.apiCentroTreinamento.repositories;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Treino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TreinoRepository extends JpaRepository<Treino, Long> {
    
    // Busca por nome do treino
    List<Treino> findByNomeContainingIgnoreCase(String nome);
    
    // Busca por grupo muscular
    List<Treino> findByGrupoMuscularIgnoreCase(String grupoMuscular);
    
    // Busca treinos com carga sugerida maior que
    List<Treino> findByCargaSugeridaGreaterThan(Float carga);
    
    // Busca treinos por nome e grupo muscular
    List<Treino> findByNomeContainingIgnoreCaseAndGrupoMuscularIgnoreCase(String nome, String grupoMuscular);
} 