package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;

import com.syntaxsquad.ltd.apiCentroTreinamento.dto.ExercicioRequestDto;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.ExercicioResponseDto;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Exercicio;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Treino;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.ExercicioRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.TreinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exercicios")
public class ExercicioController {

    private final ExercicioRepository exercicioRepository;
    private final TreinoRepository treinoRepository;

    @Autowired
    public ExercicioController(ExercicioRepository exercicioRepository, TreinoRepository treinoRepository) {
        this.exercicioRepository = exercicioRepository;
        this.treinoRepository = treinoRepository;
    }

    // Lista todos os exercícios
    @GetMapping
    public List<ExercicioResponseDto> listarExercicios() {
        return exercicioRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    // Busca exercício por ID
    @GetMapping("/{id}")
    public ResponseEntity<ExercicioResponseDto> buscarExercicioPorId(@PathVariable Long id) {
        return exercicioRepository.findById(id)
                .map(exercicio -> ResponseEntity.ok(convertToResponseDto(exercicio)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Cria novo exercício
    @PostMapping("/treinos/{treinoId}")
    public ResponseEntity<ExercicioResponseDto> criarExercicio(@PathVariable Long treinoId, @RequestBody ExercicioRequestDto exercicioDto) {
        return treinoRepository.findById(treinoId)
                .map(treino -> {
                    Exercicio exercicio = new Exercicio();
                    exercicio.setNome(exercicioDto.getNome());
                    exercicio.setRepeticoes(exercicioDto.getRepeticoes());
                    exercicio.setSeries(exercicioDto.getSeries());
                    exercicio.setTreino(treino);

                    Exercicio exercicioCriado = exercicioRepository.save(exercicio);
                    return ResponseEntity.ok(convertToResponseDto(exercicioCriado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Atualiza exercício
    @PutMapping("/{id}")
    public ResponseEntity<ExercicioResponseDto> atualizarExercicio(@PathVariable Long id, @RequestBody ExercicioRequestDto exercicioDto) {
        if (!exercicioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Exercicio exercicio = new Exercicio();
        exercicio.setId(id);
        exercicio.setNome(exercicioDto.getNome());
        exercicio.setRepeticoes(exercicioDto.getRepeticoes());
        exercicio.setSeries(exercicioDto.getSeries());
   


        Exercicio exercicioSalvo = exercicioRepository.save(exercicio);
        return ResponseEntity.ok(convertToResponseDto(exercicioSalvo));
    }

    // Busca exercícios por treino
    @GetMapping("/treinos/{treinoId}")
    public ResponseEntity<List<ExercicioResponseDto>> buscarExerciciosPorTreino(@PathVariable Long treinoId) {
        Treino treino = treinoRepository.findById(treinoId).orElse(null);
        if (treino == null) {
            return ResponseEntity.notFound().build();
        }

        List<ExercicioResponseDto> exercicios = exercicioRepository.findByTreino(treino).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(exercicios);
    }

    // Busca exercícios por nome
    @GetMapping("/busca")
    public ResponseEntity<List<ExercicioResponseDto>> buscarExerciciosPorNome(@RequestParam String nome) {
        List<ExercicioResponseDto> exercicios = exercicioRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(exercicios);
    }

    // Deleta exercício
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarExercicio(@PathVariable Long id) {
        if (!exercicioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        exercicioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar para converter Exercicio em ExercicioResponseDto
    private ExercicioResponseDto convertToResponseDto(Exercicio exercicio) {
        ExercicioResponseDto responseDto = new ExercicioResponseDto();
        responseDto.setId(exercicio.getId());
        responseDto.setNome(exercicio.getNome());
        responseDto.setRepeticoes(exercicio.getRepeticoes());
        responseDto.setSeries(exercicio.getSeries());
        return responseDto;
    }
}
