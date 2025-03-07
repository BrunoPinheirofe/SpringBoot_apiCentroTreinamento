package com.syntaxsquad.ltd.apiCentroTreinamento.repositories;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, String> {
    
    // Busca por email (útil para login/verificação de duplicidade)
    Optional<Administrador> findByEmail(String email);
    
    // Busca por nome ou sobrenome contendo o termo (case insensitive)
    List<Administrador> findByNomeContainingIgnoreCaseOrSobrenomeContainingIgnoreCase(String nome, String sobrenome);
    
    // Verifica se existe admin com este email
    boolean existsByEmail(String email);
} 