package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.syntaxsquad.ltd.apiCentroTreinamento.dto.TreinoDtoRequest;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.TreinoDtoResponse;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Exercicio;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Treino;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.AlunoRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.ExercicioRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.TreinoRepository;

@RestController
@RequestMapping("/api/treinos")
public class TreinoController {

    @Autowired
    private TreinoRepository treinoRepository;

    @Autowired
    private ExercicioRepository exercicioRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    // Listar todos os treinos
    @Cacheable(value = "treinos_all")
    @GetMapping
    public List<TreinoDtoResponse> listarTreinos() {
        return treinoRepository.findAll()
                .stream()
                .map(TreinoDtoResponse::new)
                .collect(Collectors.toList());
    }

    // Buscar treino por ID
    @GetMapping("/{id}")
    public ResponseEntity<TreinoDtoResponse> buscarTreinoPorId(@PathVariable Long id) {
        return treinoRepository.findById(id)
                .map(treino -> ResponseEntity.ok(new TreinoDtoResponse(treino)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Criar um novo treino
    @CacheEvict(value = "treinos_all", allEntries = true)
    @PostMapping
    public ResponseEntity<TreinoDtoResponse> criarTreino(@RequestBody TreinoDtoRequest treinoDto) {
        // Validação dos IDs de exercícios e alunos
        if (treinoDto.getExerciciosIds() == null || treinoDto.getExerciciosIds().isEmpty() ||
                treinoDto.getAlunosIds() == null || treinoDto.getAlunosIds().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Buscar alunos e exercícios no banco de dados
        List<Aluno> alunos = alunoRepository.findByMatriculaIn(treinoDto.getAlunosIds());
        List<Exercicio> exercicios = exercicioRepository.findAllById(treinoDto.getExerciciosIds());

        // Verificar se todos os IDs foram encontrados
        if (alunos.size() != treinoDto.getAlunosIds().size()
                || exercicios.size() != treinoDto.getExerciciosIds().size()) {
            return ResponseEntity.badRequest().build();
        }

        // Criar e salvar o novo treino
        Treino novoTreino = new Treino(treinoDto);
        novoTreino.setAlunos(alunos);
        Treino treinoSalvo = treinoRepository.save(novoTreino);

        // Associar exercícios ao treino
        exercicios.forEach(exercicio -> exercicio.setTreino(treinoSalvo));
        exercicioRepository.saveAll(exercicios);
        treinoSalvo.setExercicios(exercicios);

        return ResponseEntity.ok(new TreinoDtoResponse(treinoSalvo));
    }

    // Adicionar um exercício a um treino existente
    @CacheEvict(value = "treinos_all", allEntries = true)
    @PostMapping("/{treinoId}/exercicios/{exercicioId}")
    public ResponseEntity<TreinoDtoResponse> adicionarExercicio(@PathVariable Long treinoId,
            @PathVariable Long exercicioId) {
        Optional<Treino> treinoOptional = treinoRepository.findById(treinoId);
        Optional<Exercicio> exercicioOptional = exercicioRepository.findById(exercicioId);

        if (treinoOptional.isPresent() && exercicioOptional.isPresent()) {
            Treino treino = treinoOptional.get();
            Exercicio exercicio = exercicioOptional.get();

            // Verificar se o exercício já está associado a outro treino
            if (exercicio.getTreino() != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Exercício já pertence a outro treino
            }

            // Associar o exercício ao treino
            exercicio.setTreino(treino);
            exercicioRepository.save(exercicio);

            // Atualizar a lista de exercícios do treino
            treino.getExercicios().add(exercicio);
            treinoRepository.save(treino);

            return ResponseEntity.ok(new TreinoDtoResponse(treino));
        }
        return ResponseEntity.notFound().build();
    }

    // Adicionar alunos a um treino existente
    @CacheEvict(value = "treinos_all", allEntries = true)
    @PostMapping("/{treinoId}/alunos/{alunosIds}")
    public ResponseEntity<TreinoDtoResponse> adicionarAlunos(
            @PathVariable Long treinoId, @PathVariable List<Long> alunosIds) {

        Optional<Treino> treinoOptional = treinoRepository.findById(treinoId);
        if (treinoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Treino treino = treinoOptional.get();

        // Buscar todos os alunos pelo ID
        List<Aluno> alunos = alunoRepository.findAllByMatriculaIn(alunosIds);

        if (alunos.isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Nenhum aluno encontrado
        }

        // Adiciona os alunos ao treino
        treino.getAlunos().addAll(alunos);
        treinoRepository.save(treino);

        return ResponseEntity.ok(new TreinoDtoResponse(treino));
    }

    // Buscar treinos por grupo muscular
    @GetMapping("/grupo/{grupoMuscular}")
    public List<TreinoDtoResponse> buscarTreinosPorGrupoMuscular(@PathVariable String grupoMuscular) {
        return treinoRepository.findByGrupoMuscularIgnoreCase(grupoMuscular)
                .stream()
                .map(TreinoDtoResponse::new)
                .collect(Collectors.toList());
    }

    // Atualizar um treino existente
    @CacheEvict(value = "treinos_all", allEntries = true)
    @PutMapping("/{id}")
    public ResponseEntity<TreinoDtoResponse> atualizarTreino(@PathVariable Long id,
            @RequestBody TreinoDtoRequest treinoDto) {
        return treinoRepository.findById(id)
                .map(treino -> {
                    treino.setNome(treinoDto.getNome());
                    treino.setGrupoMuscular(treinoDto.getGrupoMuscular());
                    treino.setCargaSugerida(treinoDto.getCargaSugerida());
                    treinoRepository.save(treino);
                    return ResponseEntity.ok(new TreinoDtoResponse(treino));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Remover um exercício de um treino
    @CacheEvict(value = "treinos_all", allEntries = true)
    @DeleteMapping("/{treinoId}/exercicios/{exercicioId}")
    public ResponseEntity<Void> removerExercicio(@PathVariable Long treinoId, @PathVariable Long exercicioId) {
        Optional<Treino> treinoOptional = treinoRepository.findById(treinoId);
        Optional<Exercicio> exercicioOptional = exercicioRepository.findById(exercicioId);

        if (treinoOptional.isPresent() && exercicioOptional.isPresent()) {
            Exercicio exercicio = exercicioOptional.get();
            exercicioRepository.delete(exercicio);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Deletar um treino
    @CacheEvict(value = "treinos_all", allEntries = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTreino(@PathVariable Long id) {
        if (!treinoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        treinoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}