package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Exercicio;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Treino;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.ExercicioRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.TreinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercicios")
public class ExercicioController {

    @Autowired
    private ExercicioRepository exercicioRepository;

    @Autowired
    private TreinoRepository treinoRepository;

    // Lista todos os exercícios
    @GetMapping
    public List<Exercicio> listarExercicios() {
        return exercicioRepository.findAll();
    }

    // Busca exercício por ID
    @GetMapping("/{id}")
    public ResponseEntity<Exercicio> buscarExercicioPorId(@PathVariable Long id) {
        return exercicioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Cria novo exercício
    @PostMapping("/treinos/{treinoId}")
    public ResponseEntity<Exercicio> criarExercicio(
            @PathVariable Long treinoId,
            @RequestBody Exercicio exercicio) {

        return treinoRepository.findById(treinoId)
                .map(treino -> {
                    exercicio.setTreino(treino);
                    return ResponseEntity.ok(exercicioRepository.save(exercicio));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Atualiza exercício
    @PutMapping("/{id}")
    public ResponseEntity<Exercicio> atualizarExercicio(
            @PathVariable Long id,
            @RequestBody Exercicio exercicioAtualizado) {

        if (!exercicioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        exercicioAtualizado.setId(id);
        return ResponseEntity.ok(exercicioRepository.save(exercicioAtualizado));
    }

    // Busca exercícios por treino
    @GetMapping("/treinos/{treinoId}")
    public ResponseEntity<List<Exercicio>> buscarExerciciosPorTreino(@PathVariable Long treinoId) {
        Treino treino = treinoRepository.findById(treinoId).orElse(null);
        if (treino == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(exercicioRepository.findByTreino(treino));
    }

    // Busca exercícios por nome
    @GetMapping("/busca")
    public List<Exercicio> buscarExerciciosPorNome(@RequestParam String nome) {
        return exercicioRepository.findByNomeContainingIgnoreCase(nome);
    }

    // Deleta exercício
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarExercicio(@PathVariable Long id) {
        if (!exercicioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        exercicioRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
} 