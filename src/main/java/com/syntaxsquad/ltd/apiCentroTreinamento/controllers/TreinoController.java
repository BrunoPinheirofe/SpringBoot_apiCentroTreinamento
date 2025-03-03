package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Treino;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Exercicio;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.TreinoRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.ExercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treinos")
public class TreinoController {

    @Autowired
    private TreinoRepository treinoRepository;

    @Autowired
    private ExercicioRepository exercicioRepository;

    // Lista todos os treinos
    @GetMapping
    public List<Treino> listarTreinos() {
        return treinoRepository.findAll();
    }

    // Busca treino por ID
    @GetMapping("/{id}")
    public ResponseEntity<Treino> buscarTreinoPorId(@PathVariable Long id) {
        return treinoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Cria novo treino com exercícios
    @PostMapping
    public ResponseEntity<Treino> criarTreino(@RequestBody Treino treino) {
        // Validação básica
        if (treino.getExercicios() == null || treino.getExercicios().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(null);
        }

        // Salva o treino e seus exercícios
        Treino treinoSalvo = treinoRepository.save(treino);
        treino.getExercicios().forEach(exercicio -> {
            exercicio.setTreino(treinoSalvo);
            exercicioRepository.save(exercicio);
        });

        return ResponseEntity.ok(treinoSalvo);
    }

    // Adiciona exercício ao treino
    @PostMapping("/{treinoId}/exercicios")
    public ResponseEntity<Treino> adicionarExercicio(
            @PathVariable Long treinoId,
            @RequestBody Exercicio exercicio) {
        
        return treinoRepository.findById(treinoId)
                .map(treino -> {
                    exercicio.setTreino(treino);
                    exercicioRepository.save(exercicio);
                    treino.getExercicios().add(exercicio);
                    return ResponseEntity.ok(treinoRepository.save(treino));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Busca treinos por grupo muscular
    @GetMapping("/grupo/{grupoMuscular}")
    public List<Treino> buscarTreinosPorGrupoMuscular(@PathVariable String grupoMuscular) {
        return treinoRepository.findByGrupoMuscularIgnoreCase(grupoMuscular);
    }

    // Atualiza treino
    @PutMapping("/{id}")
    public ResponseEntity<Treino> atualizarTreino(
            @PathVariable Long id,
            @RequestBody Treino treinoAtualizado) {
        
        if (!treinoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        treinoAtualizado.setId(id);
        return ResponseEntity.ok(treinoRepository.save(treinoAtualizado));
    }

    // Remove exercício do treino
    @DeleteMapping("/{treinoId}/exercicios/{exercicioId}")
    public ResponseEntity<?> removerExercicio(
            @PathVariable Long treinoId,
            @PathVariable Long exercicioId) {
        
        return treinoRepository.findById(treinoId)
                .map(treino -> {
                    exercicioRepository.deleteById(exercicioId);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 