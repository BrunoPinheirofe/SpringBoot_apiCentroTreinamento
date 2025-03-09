package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.syntaxsquad.ltd.apiCentroTreinamento.dto.AlunoDtoRequest;
import com.syntaxsquad.ltd.apiCentroTreinamento.enums.UserRole;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.User;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.AlunoRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.UserRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.services.AlunoService;
import com.syntaxsquad.ltd.apiCentroTreinamento.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/alunos")
@Validated
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    // Criar aluno
    @PostMapping
    public ResponseEntity<?> criarAluno(@Valid @RequestBody AlunoDtoRequest alunoRequest) {
        // Verifica se já existe um usuário com o mesmo email
        Optional<User> existingUser = userRepository.findByEmail(alunoRequest.getEmail());

        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("Já existe um usuário com este email.");
        }

        // Verifica se o usuário é um aluno
        Optional<User> user = userRepository.findByEmail(alunoRequest.getEmail());
        if (user.isEmpty() || user.get().getRole() != UserRole.ALUNO) {
            return ResponseEntity.badRequest().body("Usuário não encontrado ou não tem permissão para ser aluno.");
        }

        Aluno aluno = new Aluno(alunoRequest.getNome(), alunoRequest.getSobrenome(), alunoRequest.getEmail(),
                alunoRequest.getTelefone(), alunoRequest.getDataNascimento(), alunoRequest.getIdade(),
                alunoRequest.getGenero(), alunoRequest.getObservacao(), user.get());

        Aluno alunoSalvo = alunoService.saveAluno(aluno);
        return ResponseEntity.ok(alunoSalvo);
    }

    // Atualizar aluno
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarAluno(@PathVariable String id, @Valid @RequestBody AlunoDtoRequest alunoRequest) {
        Optional<Aluno> alunoExistente = alunoRepository.findById(id);

        if (alunoExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Verifica se o email está sendo alterado para um email já existente
        Optional<Aluno> alunoComEmailExistente = alunoRepository.findByEmail(alunoRequest.getEmail());
        if (alunoComEmailExistente.isPresent() && !alunoComEmailExistente.get().getMatricula().equals(id)) {
            return ResponseEntity.badRequest().body("Email já está em uso.");
        }

        Aluno aluno = alunoExistente.get();
        aluno.setNome(alunoRequest.getNome());
        aluno.setSobrenome(alunoRequest.getSobrenome());
        aluno.setEmail(alunoRequest.getEmail());
        aluno.setTelefone(alunoRequest.getTelefone());
        aluno.setDataNascimento(alunoRequest.getDataNascimento());
        aluno.setGenero(alunoRequest.getGenero());
        aluno.setIdade(alunoRequest.getIdade());
        aluno.setObservacao(alunoRequest.getObservacao());

        Aluno alunoAtualizado = alunoService.saveAluno(aluno);
        return ResponseEntity.ok(alunoAtualizado);
    }

    // Remover aluno
    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void> removerAluno(@PathVariable String matricula) {
        Optional<Aluno> alunoExistente = alunoRepository.findByMatricula(matricula);
        if (alunoExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        alunoRepository.deleteById(matricula);
        return ResponseEntity.noContent().build();
    }

    // Buscar aluno por matricula
    @GetMapping("/{matricula}")
    public ResponseEntity<Aluno> buscarAlunoPorMatricula(@PathVariable String matricula) {
        Aluno aluno = alunoRepository.findByMatricula(matricula).orElse(null);
        if (aluno == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(aluno);
    }

    // Listar todos os alunos
    @GetMapping
    public ResponseEntity<Iterable<Aluno>> listarAlunos() {
        Iterable<Aluno> alunos = alunoRepository.findAll();
        return ResponseEntity.ok(alunos);
    }
}
