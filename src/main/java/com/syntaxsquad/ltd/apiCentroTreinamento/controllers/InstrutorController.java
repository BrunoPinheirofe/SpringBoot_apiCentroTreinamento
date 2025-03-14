package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.InstrutorDtoRequest;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.InstrutorDtoResponse;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.ErrorDto;
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

    // Função utilitária para gerar mensagens de erro
    private ResponseEntity<ErrorDto> gerarErro(String mensagem, HttpStatus status) {
        ErrorDto erro = new ErrorDto(mensagem);
        return ResponseEntity.status(status).body(erro);
    }


    // Função para verificar se o email já existe em outros usuários
    private boolean emailJaCadastrado(String email) {
        return alunoRepository.findByEmail(email).isPresent() ||
               instrutorRepository.findByEmail(email).isPresent() ||
               adminRepository.findByEmail(email).isPresent();
    }

    // Criando Instrutor (POST)
    @PostMapping
    public ResponseEntity<?> createInstrutor(@Valid @RequestBody InstrutorDtoRequest instrutorRequest) {
        if (emailJaCadastrado(instrutorRequest.getEmail())) {
            return gerarErro("Já existe um usuário com este email.", HttpStatus.BAD_REQUEST);
        }

        // Verifica se o usuário com o email existe no banco de Users
        Optional<User> user = userRepository.findByEmail(instrutorRequest.getEmail());
        if (user.isEmpty()) {
            return gerarErro("Usuário não encontrado com este email.", HttpStatus.BAD_REQUEST);
        }

        // Verifica se o usuário tem o role de INSTRUTOR
        if (user.get().getRole() != UserRole.TREINADOR) {
            return gerarErro("O usuário não tem permissão para criar instrutores.", HttpStatus.FORBIDDEN);
        }

        // Cria o objeto Instrutor
        Instrutor novoInstrutor = new Instrutor(instrutorRequest.getNome(), instrutorRequest.getEspecialidade(),
                instrutorRequest.getEmail(), user.get());

        // Salva o novo Instrutor no banco de dados
        instrutorRepository.save(novoInstrutor);

        // Prepara a resposta com os dados do novo instrutor
        InstrutorDtoResponse instrutorResponse = new InstrutorDtoResponse(
                novoInstrutor.getMatricula(), novoInstrutor.getNome(), novoInstrutor.getEspecialidade(),
                novoInstrutor.getEmail(), novoInstrutor.getTurmas());

        return ResponseEntity.status(HttpStatus.CREATED).body(instrutorResponse);
    }

    // Consultando todos os Instrutores (GET)
    @Cacheable(value = "instrutores_all")
    @GetMapping
    public ResponseEntity<List<InstrutorDtoResponse>> getAllInstrutores() {
        List<Instrutor> instrutores = instrutorRepository.findAll();
        if (instrutores.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<InstrutorDtoResponse> response = instrutores.stream()
                .map(instrutor -> new InstrutorDtoResponse(
                        instrutor.getMatricula(), instrutor.getNome(), instrutor.getEspecialidade(),
                        instrutor.getEmail(), instrutor.getTurmas()))
                .toList();

        return ResponseEntity.ok(response);
    }

    // Consultando um Instrutor por ID (GET)
    @GetMapping("/{matricula}")
    public ResponseEntity<?> getInstrutorById(@PathVariable String matricula) {
        Optional<Instrutor> instrutor = instrutorRepository.findById(matricula);
        if (instrutor.isEmpty()) {
            return gerarErro("Instrutor não encontrado.", HttpStatus.NOT_FOUND);
        }

        // Adicionando o campo "turmas" à resposta
        InstrutorDtoResponse response = new InstrutorDtoResponse(
                instrutor.get().getMatricula(), instrutor.get().getNome(), instrutor.get().getEspecialidade(),
                instrutor.get().getEmail(), instrutor.get().getTurmas());

        return ResponseEntity.ok(response);
    }

    // Atualizando um Instrutor (PUT)
    @CacheEvict(value = "instrutores_all", allEntries = true)
    @PutMapping("/{matricula}")
    public ResponseEntity<?> updateInstrutor(@PathVariable String matricula,
                                             @Valid @RequestBody InstrutorDtoRequest instrutorRequest) {
        Optional<Instrutor> existingInstrutor = instrutorRepository.findById(matricula);
        if (existingInstrutor.isEmpty()) {
            return gerarErro("Instrutor não encontrado.", HttpStatus.NOT_FOUND);
        }

        Instrutor instrutorToUpdate = existingInstrutor.get();
        instrutorToUpdate.setNome(instrutorRequest.getNome());
        instrutorToUpdate.setEspecialidade(instrutorRequest.getEspecialidade());
        instrutorToUpdate.setEmail(instrutorRequest.getEmail());

        instrutorRepository.save(instrutorToUpdate);

        InstrutorDtoResponse response = new InstrutorDtoResponse(
                instrutorToUpdate.getMatricula(), instrutorToUpdate.getNome(), instrutorToUpdate.getEspecialidade(),
                instrutorToUpdate.getEmail(), instrutorToUpdate.getTurmas());

        return ResponseEntity.ok(response);
    }

    // Deletando um Instrutor (DELETE)
    @CacheEvict(value = "instrutores_all", allEntries = true)
    @DeleteMapping("/{matricula}")
    public ResponseEntity<?> deleteInstrutor(@PathVariable String matricula) {
        Optional<Instrutor> instrutor = instrutorRepository.findById(matricula);
        if (instrutor.isEmpty()) {
            return gerarErro("Instrutor não encontrado.", HttpStatus.NOT_FOUND);
        }

        instrutorRepository.delete(instrutor.get());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
