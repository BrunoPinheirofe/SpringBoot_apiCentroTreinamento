package com.syntaxsquad.ltd.apiCentroTreinamento.repositories;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email); // Busca por email do usuário

    boolean existsByEmail(String email);
}