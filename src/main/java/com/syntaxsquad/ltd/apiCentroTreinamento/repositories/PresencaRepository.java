package com.syntaxsquad.ltd.apiCentroTreinamento.repositories;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Presenca;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Turma;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Instrutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PresencaRepository extends JpaRepository<Presenca, Long> {
    
    // Busca presenças por aluno
    List<Presenca> findByAluno(Aluno aluno);
    
    // Busca presenças por turma
    List<Presenca> findByTurma(Turma turma);
    
    // Busca presenças registradas por instrutor
    List<Presenca> findByInstrutor(Instrutor instrutor);
    
    // Busca presenças por período
    List<Presenca> findByDataPresencaBetween(LocalDateTime inicio, LocalDateTime fim);
    
    // Busca presenças por aluno e período
    List<Presenca> findByAlunoAndDataPresencaBetween(Aluno aluno, LocalDateTime inicio, LocalDateTime fim);
    
    // Conta número de presenças de um aluno
    @Query("SELECT COUNT(p) FROM Presenca p WHERE p.aluno = ?1 AND p.presente = true")
    Long countPresencasByAluno(Aluno aluno);
    
    // Busca faltas por aluno
    List<Presenca> findByAlunoAndPresenteFalse(Aluno aluno);
} 