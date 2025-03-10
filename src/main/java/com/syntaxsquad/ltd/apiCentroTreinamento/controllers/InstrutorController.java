package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.InstrutorDtoRequest;
import com.syntaxsquad.ltd.apiCentroTreinamento.enums.UserRole;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Administrador;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Instrutor;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.User;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.AdministradorRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.AlunoRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.InstrutorRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.UserRepository;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/instrutores")
public class InstrutorController {

    @Autowired
    private AdministradorRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InstrutorRepository instrutorRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    // Criando Instrutor (POST)
    @PostMapping
    public ResponseEntity<?> createInstrutor(@Valid @RequestBody InstrutorDtoRequest instrutorRequest) {

        // Verifica se já existe um usuário com o mesmo email nos repositórios de Aluno,
        // Instrutor ou Administrador
        Optional<Aluno> existingAluno = alunoRepository.findByEmail(instrutorRequest.getEmail());
        Optional<Instrutor> existingInstrutor = instrutorRepository.findByEmail(instrutorRequest.getEmail());
        Optional<Administrador> existingAdmin = adminRepository.findByEmail(instrutorRequest.getEmail());

        if (existingAluno.isPresent() || existingInstrutor.isPresent() || existingAdmin.isPresent()) {
            return ResponseEntity.badRequest().body("Já existe um usuário com este email.");
        }

        // Verifica se o usuário com o email existe no banco de Users
        Optional<User> user = userRepository.findByEmail(instrutorRequest.getEmail());
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não encontrado com este email.");
        }

        // Verifica se o usuário tem o role de INSTRUTOR
        if (user.get().getRole() != UserRole.TREINADOR) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("O usuário não tem permissão para criar instrutores.");
        }

        // Cria o objeto Instrutor
        Instrutor novoInstrutor = new Instrutor(instrutorRequest.getNome(), instrutorRequest.getEspecialidade(),
                instrutorRequest.getEmail(), user.get());

        // Salva o novo Instrutor no banco de dados
        instrutorRepository.save(novoInstrutor);

        // Retorna o Instrutor criado como resposta
        return ResponseEntity.status(HttpStatus.CREATED).body(novoInstrutor);
    }

    // Consultando todos os Instrutores (GET)
    @GetMapping
    public ResponseEntity<List<Instrutor>> getAllInstrutores() {
        List<Instrutor> instrutores = instrutorRepository.findAll();
        if (instrutores.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(instrutores);
    }

    // Consultando um Instrutor por ID (GET)
    @GetMapping("/{matricula}")
    public ResponseEntity<?> getInstrutorById(@PathVariable String matricula) {
        Optional<Instrutor> instrutor = instrutorRepository.findById(matricula);
        if (!instrutor.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Instrutor não encontrado.");
        }
        return ResponseEntity.ok(instrutor.get());
    }

    // Atualizando um Instrutor (PUT)
    @PutMapping("/{matricula}")
    public ResponseEntity<?> updateInstrutor(@PathVariable String matricula,
            @Valid @RequestBody InstrutorDtoRequest instrutorRequest) {
        Optional<Instrutor> existingInstrutor = instrutorRepository.findById(matricula);
        if (!existingInstrutor.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Instrutor não encontrado.");
        }

        Instrutor instrutorToUpdate = existingInstrutor.get();
        instrutorToUpdate.setNome(instrutorRequest.getNome());
        instrutorToUpdate.setEspecialidade(instrutorRequest.getEspecialidade());
        instrutorToUpdate.setEmail(instrutorRequest.getEmail());

        instrutorRepository.save(instrutorToUpdate);
        return ResponseEntity.ok(instrutorToUpdate);
    }

    // Deletando um Instrutor (DELETE)
    @DeleteMapping("/{matricula}")
    public ResponseEntity<?> deleteInstrutor(@PathVariable String matricula) {
        Optional<Instrutor> instrutor = instrutorRepository.findById(matricula);
        if (!instrutor.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Instrutor não encontrado.");
        }

        instrutorRepository.delete(instrutor.get());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
