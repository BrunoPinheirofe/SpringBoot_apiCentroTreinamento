package com.syntaxsquad.ltd.apiCentroTreinamento.repositories;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Instrutor;
import com.syntaxsquad.ltd.apiCentroTreinamento.serializers.Matricula;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InstrutorRepository extends JpaRepository<Instrutor, Matricula> {

    boolean existsByEmail(String email);

    Optional<Instrutor> findByEmail(String email);

    Optional<Instrutor> findByMatricula(Matricula Matricula);
}