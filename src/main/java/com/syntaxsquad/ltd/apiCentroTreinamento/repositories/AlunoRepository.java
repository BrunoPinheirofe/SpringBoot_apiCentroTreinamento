package com.syntaxsquad.ltd.apiCentroTreinamento.repositories;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;
import com.syntaxsquad.ltd.apiCentroTreinamento.enums.SexoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    
    // Busca por email
    Optional<Aluno> findByEmail(String email);
    
    // Busca por nome ou sobrenome
    List<Aluno> findByNomeContainingIgnoreCaseOrSobrenomeContainingIgnoreCase(String nome, String sobrenome);
    
    // Busca por gênero
    List<Aluno> findByGenero(SexoEnum genero);
    
    // Busca por data de cadastro
    List<Aluno> findByDataCadastro(LocalDate dataCadastro);
    
    // Busca alunos com idade maior que
    List<Aluno> findByIdadeGreaterThan(int idade);
    
    // Verifica se existe aluno com este email
    boolean existsByEmail(String email);
} 