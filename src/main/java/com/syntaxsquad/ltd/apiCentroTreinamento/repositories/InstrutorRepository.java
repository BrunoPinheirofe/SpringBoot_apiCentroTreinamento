package com.syntaxsquad.ltd.apiCentroTreinamento.repositories;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Instrutor;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InstrutorRepository extends JpaRepository<Instrutor, String> {

    boolean existsByEmail(String email);

    Optional<Instrutor> findByEmail(String email);

    Instrutor findByMatricula(String string);
}