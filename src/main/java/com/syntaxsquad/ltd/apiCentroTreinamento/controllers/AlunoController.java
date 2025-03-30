package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.syntaxsquad.ltd.apiCentroTreinamento.dto.AlunoDtoRequest;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.AlunoDtoResponse;
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
import com.syntaxsquad.ltd.apiCentroTreinamento.serializers.Matricula;
import com.syntaxsquad.ltd.apiCentroTreinamento.services.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/alunos")
@Validated
@Tag(name = "Alunos", description = "API para gerenciamento de alunos")
@SecurityRequirement(name = "jwt_auth")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private InstrutorRepository instrutorRepository;

    @Autowired
    private AdministradorRepository adminRepository;

    // Criar aluno
    @CacheEvict(value = "alunos_all", allEntries = true)
    @PostMapping
    @Operation(
        summary = "Criar um novo aluno",
        description = "Cria um novo aluno no sistema com base nos dados fornecidos"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Aluno criado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlunoDtoResponse.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Dados inválidos ou email já existente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))
        )
    })
    public ResponseEntity<?> criarAluno(
            @Valid @RequestBody 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados do aluno a ser criado", 
                required = true, 
                content = @Content(schema = @Schema(implementation = AlunoDtoRequest.class))
            ) AlunoDtoRequest alunoRequest) {

        // Verifica se já existe um usuário com o mesmo email nos repositórios de Aluno,
        // Instrutor ou Administrador
        Optional<Aluno> existingAluno = alunoRepository.findByEmail(alunoRequest.getEmail());
        Optional<Instrutor> existingInstrutor = instrutorRepository.findByEmail(alunoRequest.getEmail());
        Optional<Administrador> existingAdmin = adminRepository.findByEmail(alunoRequest.getEmail());

        if (existingAluno.isPresent() || existingInstrutor.isPresent() || existingAdmin.isPresent()) {
            // Erro de email já existente
            return ResponseEntity.badRequest().body(new ErrorDto("Erro: Já existe um usuário com este email"));
        }

        // Verifica se o usuário com o email existe no banco de Users
        Optional<User> user = userRepository.findByEmail(alunoRequest.getEmail());
        if (!user.isPresent()) {
            // Erro: Usuário não encontrado
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDto("Erro: Usuário não encontrado com este email"));
        }

        // Verifica se o usuário é um aluno
        if (user.get().getRole() != UserRole.ALUNO) {
            // Erro: O usuário não tem permissão para ser aluno
            return ResponseEntity.badRequest().body(new ErrorDto("Erro: Usuário não tem permissão para ser aluno"));
        }

        Aluno aluno = new Aluno(alunoRequest.getNome(), alunoRequest.getSobrenome(), alunoRequest.getEmail(),
                alunoRequest.getTelefone(), alunoRequest.getDataNascimento(), alunoRequest.getIdade(),
                alunoRequest.getGenero(), alunoRequest.getObservacao(), user.get());

        Aluno alunoSalvo = alunoService.saveAluno(aluno);

        AlunoDtoResponse alunoDto = new AlunoDtoResponse(
            alunoSalvo
        );
        return ResponseEntity.ok(alunoDto);
    }

    // Atualizar aluno
    @CacheEvict(value = "alunos_all", allEntries = true)
    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar um aluno existente",
        description = "Atualiza os dados de um aluno existente no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Aluno atualizado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlunoDtoResponse.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Dados inválidos",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Aluno não encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))
        )
    })
    public ResponseEntity<?> atualizarAluno(
            @Parameter(description = "ID do aluno a ser atualizado", required = true) 
            @PathVariable Matricula id, 
            @Valid @RequestBody 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Novos dados do aluno", 
                required = true, 
                content = @Content(schema = @Schema(implementation = AlunoDtoRequest.class))
            ) AlunoDtoRequest alunoRequest) {
        Optional<Aluno> alunoExistente = alunoRepository.findById(id);

        if (alunoExistente.isEmpty()) {
            // Erro: Aluno não encontrado
            return ResponseEntity.notFound().build();
        }

        // Verifica se o email está sendo alterado para um email já existente
        Optional<Aluno> alunoComEmailExistente = alunoRepository.findByEmail(alunoRequest.getEmail());
        if (alunoComEmailExistente.isPresent() && !alunoComEmailExistente.get().getMatricula().equals(id)) {
            // Erro: Email já está em uso
            return ResponseEntity.badRequest().body(new ErrorDto("Erro: Email já está em uso"));
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

        AlunoDtoResponse alunoDtoResponse = new AlunoDtoResponse(
                alunoAtualizado.getMatricula(), alunoAtualizado.getNome(),
                alunoAtualizado.getSobrenome(), alunoAtualizado.getEmail(),
                alunoAtualizado.getTelefone(), alunoAtualizado.getDataNascimento(),
                alunoAtualizado.getDataCadastro(), alunoAtualizado.getIdade(),
                alunoAtualizado.getGenero(), alunoAtualizado.getObservacao());

        return ResponseEntity.ok(alunoDtoResponse);
    }

    // Remover aluno
    @CacheEvict(value = "alunos_all", allEntries = true)
    @DeleteMapping("/{matricula}")
    public ResponseEntity<?> removerAluno(@PathVariable Matricula matricula) {
        Optional<Aluno> alunoExistente = alunoRepository.findByMatricula(matricula);
        if (alunoExistente.isEmpty()) {
            // Erro: Aluno não encontrado para remoção
            return ResponseEntity.notFound().build();
        }
        alunoRepository.deleteById(matricula);
        return ResponseEntity.noContent().build();
    }

    // Buscar aluno por matricula
    @GetMapping("/{matricula}")
    public ResponseEntity<?> buscarAlunoPorMatricula(@PathVariable Matricula matricula) {
        Aluno aluno = alunoRepository.findByMatricula(matricula).orElse(null);
        if (aluno == null) {
            // Erro: Aluno não encontrado
            return ResponseEntity.notFound().build();
        }
        AlunoDtoResponse alunoDtoResponse = new AlunoDtoResponse(
                aluno.getMatricula(), aluno.getNome(), aluno.getSobrenome(),
                aluno.getEmail(), aluno.getTelefone(), aluno.getDataNascimento(),
                aluno.getDataCadastro(), aluno.getIdade(), aluno.getGenero(), aluno.getObservacao()
        );
        return ResponseEntity.ok(alunoDtoResponse);
    }

    // Listar todos os alunos
    @GetMapping
    @Cacheable(value = "alunos_all")
    public ResponseEntity<Iterable<AlunoDtoResponse>> listarAlunos() {
        Iterable<Aluno> alunos = alunoRepository.findAll();
        Iterable<AlunoDtoResponse> alunosDtoResponse = StreamSupport
                .stream(alunos.spliterator(), false)
                .map(aluno -> new AlunoDtoResponse(
                        aluno.getMatricula(), aluno.getNome(),
                        aluno.getSobrenome(), aluno.getEmail(),
                        aluno.getTelefone(), aluno.getDataNascimento(),
                        aluno.getDataCadastro(), aluno.getIdade(),
                        aluno.getGenero(), aluno.getObservacao()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(alunosDtoResponse);
    }
}
