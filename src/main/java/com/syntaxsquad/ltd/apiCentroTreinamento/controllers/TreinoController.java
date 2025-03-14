package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public List<TreinoDtoResponse> listarTreinos() {
        return treinoRepository.findAll()
                               .stream()
                               .map(TreinoDtoResponse::new)
                               .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreinoDtoResponse> buscarTreinoPorId(@PathVariable Long id) {
        return treinoRepository.findById(id)
                .map(treino -> ResponseEntity.ok(new TreinoDtoResponse(treino)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TreinoDtoResponse> criarTreino(@RequestBody TreinoDtoRequest treinoDto) {
        if (treinoDto.getExerciciosIds() == null || treinoDto.getExerciciosIds().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Treino novoTreino = new Treino(treinoDto);
        List<Aluno> alunos = alunoRepository.findByMatriculaIn(treinoDto.getAlunosIds());
        novoTreino.setAlunos(alunos);
        Treino treinoSalvo = treinoRepository.save(novoTreino);

        List<Exercicio> exercicios = exercicioRepository.findAllById(treinoDto.getExerciciosIds());
        exercicios.forEach(exercicio -> exercicio.setTreino(treinoSalvo));
        exercicioRepository.saveAll(exercicios);
        treinoSalvo.setExercicios(exercicios);

        return ResponseEntity.ok(new TreinoDtoResponse(treinoSalvo));
    }

    @PostMapping("/{treinoId}/exercicios")
    public ResponseEntity<TreinoDtoResponse> adicionarExercicio(@PathVariable Long treinoId, @RequestBody Long exercicioId) {
        Optional<Treino> treinoOptional = treinoRepository.findById(treinoId);
        Optional<Exercicio> exercicioOptional = exercicioRepository.findById(exercicioId);

        if (treinoOptional.isPresent() && exercicioOptional.isPresent()) {
            Treino treino = treinoOptional.get();
            Exercicio exercicio = exercicioOptional.get();
            exercicio.setTreino(treino);
            exercicioRepository.save(exercicio);
            treino.getExercicios().add(exercicio);
            return ResponseEntity.ok(new TreinoDtoResponse(treino));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{treinoId}/alunos")
    public ResponseEntity<TreinoDtoResponse> adicionarAlunos(@PathVariable Long treinoId, @RequestBody List<String> alunosIds) {
        Optional<Treino> treinoOptional = treinoRepository.findById(treinoId);
        if (treinoOptional.isPresent()) {
            Treino treino = treinoOptional.get();
            List<Aluno> alunos = alunoRepository.findByMatriculaIn(alunosIds);
            treino.setAlunos(alunos);
            treinoRepository.save(treino);
            return ResponseEntity.ok(new TreinoDtoResponse(treino));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/grupo/{grupoMuscular}")
    public List<TreinoDtoResponse> buscarTreinosPorGrupoMuscular(@PathVariable String grupoMuscular) {
        return treinoRepository.findByGrupoMuscularIgnoreCase(grupoMuscular)
                .stream()
                .map(TreinoDtoResponse::new)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreinoDtoResponse> atualizarTreino(@PathVariable Long id, @RequestBody TreinoDtoRequest treinoDto) {
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
}
