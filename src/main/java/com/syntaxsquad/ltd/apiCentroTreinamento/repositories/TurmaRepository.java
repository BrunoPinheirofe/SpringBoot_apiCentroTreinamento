package com.syntaxsquad.ltd.apiCentroTreinamento.repositories;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Turma;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Instrutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {
    
    // Busca por nome da turma
    List<Turma> findByNomeContainingIgnoreCase(String nome);
    
    // Busca por dia da semana
    List<Turma> findByDiaSemana(String diaSemana);
    
    // Busca por horário
    List<Turma> findByHorario(String horario);
    
    // Busca turmas por instrutor
    List<Turma> findByInstrutor(Instrutor instrutor);
    
    // Busca turmas que contém determinado aluno
    List<Turma> findByAlunosContaining(Aluno aluno);
    
    // Busca turmas por dia e horário
    List<Turma> findByDiaSemanaAndHorario(String diaSemana, String horario);
    
    // Conta número de alunos em uma turma
    @Query("SELECT COUNT(a) FROM Turma t JOIN t.alunos a WHERE t.id = ?1")
    Long countAlunosByTurmaId(Long turmaId);
} 