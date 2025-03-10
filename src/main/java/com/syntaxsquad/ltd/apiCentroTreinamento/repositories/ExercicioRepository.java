package com.syntaxsquad.ltd.apiCentroTreinamento.repositories;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Exercicio;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Treino;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExercicioRepository extends JpaRepository<Exercicio, Long> {

    // Busca por nome do exercício (sem paginação)
    List<Exercicio> findByNomeContainingIgnoreCase(String nome);

    // Busca exercícios por treino
    List<Exercicio> findByTreino(Treino treino);

    // Busca exercícios com carga maior que um valor específico
    List<Exercicio> findByCargaGreaterThan(Float carga);

    // Busca exercícios por número de séries
    List<Exercicio> findBySeries(Integer series);

    // Busca exercícios por número de repetições
    List<Exercicio> findByRepeticoes(Integer repeticoes);

    // Busca por nome do exercício (com paginação)
    Page<Exercicio> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
