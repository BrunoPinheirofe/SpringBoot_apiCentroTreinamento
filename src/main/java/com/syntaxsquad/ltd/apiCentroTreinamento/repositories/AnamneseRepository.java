package com.syntaxsquad.ltd.apiCentroTreinamento.repositories;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Anamnese;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Instrutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnamneseRepository extends JpaRepository<Anamnese, Long> {
    
    // Busca anamnese por aluno
    Optional<Anamnese> findByAluno(Aluno aluno);
    
    // Busca anamneses por instrutor
    List<Anamnese> findByInstrutor(Instrutor instrutor);
    
    // Busca por data de cadastro
    List<Anamnese> findByDataCadastro(LocalDate dataCadastro);
    
    // Busca anamneses de alunos que praticam atividade
    List<Anamnese> findByPraticaAtividadeTrue();
    
    // Busca por tipo de atividade praticada
    List<Anamnese> findByQualAtividadeContainingIgnoreCase(String atividade);
} 