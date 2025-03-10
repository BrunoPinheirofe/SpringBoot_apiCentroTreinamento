package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Treino;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.ExercicioResponseDto;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.TreinoDtoRequest;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.TreinoDtoResponse;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Exercicio;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.TreinoRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.ExercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/treinos")
public class TreinoController {

    @Autowired
    private TreinoRepository treinoRepository;

    @Autowired
    private ExercicioRepository exercicioRepository;

    // Lista todos os treinos
    @GetMapping
    public List<TreinoDtoResponse> listarTreinos() {
        List<Treino> treinos = treinoRepository.findAll();

        return treinos.stream().map(treino -> {
            TreinoDtoResponse treinoDtoResponse = new TreinoDtoResponse();
            treinoDtoResponse.setId(treino.getId());
            treinoDtoResponse.setNome(treino.getNome());
            treinoDtoResponse.setGrupoMuscular(treino.getGrupoMuscular());
            treinoDtoResponse.setCargaSugerida(treino.getCargaSugerida());

            List<ExercicioResponseDto> exercicios = treino.getExercicios().stream().map(exercicio -> {
                ExercicioResponseDto exercicioDto = new ExercicioResponseDto();
                exercicioDto.setId(exercicio.getId());
                exercicioDto.setNome(exercicio.getNome());
                exercicioDto.setRepeticoes(exercicio.getRepeticoes());
                exercicioDto.setSeries(exercicio.getSeries());
                return exercicioDto;
            }).collect(Collectors.toList());

            treinoDtoResponse.setExercicios(exercicios);
            return treinoDtoResponse;
        }).collect(Collectors.toList());
    }

    // Busca treino por ID
    @GetMapping("/{id}")
    public ResponseEntity<TreinoDtoResponse> buscarTreinoPorId(@PathVariable Long id) {
        return treinoRepository.findById(id)
                .map(treino -> {
                    TreinoDtoResponse treinoDtoResponse = new TreinoDtoResponse();
                    treinoDtoResponse.setId(treino.getId());
                    treinoDtoResponse.setNome(treino.getNome());
                    treinoDtoResponse.setGrupoMuscular(treino.getGrupoMuscular());
                    treinoDtoResponse.setCargaSugerida(treino.getCargaSugerida());

                    List<ExercicioResponseDto> exercicios = treino.getExercicios().stream().map(exercicio -> {
                        ExercicioResponseDto exercicioDto = new ExercicioResponseDto();
                        exercicioDto.setId(exercicio.getId());
                        exercicioDto.setNome(exercicio.getNome());
                        exercicioDto.setRepeticoes(exercicio.getRepeticoes());
                        exercicioDto.setSeries(exercicio.getSeries());
                        return exercicioDto;
                    }).collect(Collectors.toList());

                    treinoDtoResponse.setExercicios(exercicios);
                    return ResponseEntity.ok(treinoDtoResponse);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Cria novo treino com exercícios
    @PostMapping
    public ResponseEntity<TreinoDtoResponse> criarTreino(@RequestBody TreinoDtoRequest treinoDto) {
        if (treinoDto.getExerciciosIds() == null || treinoDto.getExerciciosIds().isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Retorna erro caso não haja exercícios
        }

        // Criação do novo treino
        Treino novoTreino = new Treino();
        novoTreino.setNome(treinoDto.getNome());
        novoTreino.setGrupoMuscular(treinoDto.getGrupoMuscular());
        novoTreino.setCargaSugerida(treinoDto.getCargaSugerida());

        Treino treinoSalvo = treinoRepository.save(novoTreino);

        // Associando os exercícios ao treino
        treinoDto.getExerciciosIds().forEach(exercicioId -> {
            exercicioRepository.findById(exercicioId).ifPresent(exercicio -> {
                exercicio.setTreino(treinoSalvo); // Associa exercício ao treino
                exercicioRepository.save(exercicio); // Salva exercício
                treinoSalvo.getExercicios().add(exercicio); // Adiciona o exercício ao treino
            });
        });

        // Salvando o treino com os exercícios
        treinoRepository.save(treinoSalvo);

        // Retorna o treino criado como resposta
        TreinoDtoResponse responseDto = new TreinoDtoResponse();
        responseDto.setId(treinoSalvo.getId());
        responseDto.setNome(treinoSalvo.getNome());
        responseDto.setGrupoMuscular(treinoSalvo.getGrupoMuscular());
        responseDto.setCargaSugerida(treinoSalvo.getCargaSugerida());
        return ResponseEntity.ok(responseDto);
    }

    // Adiciona exercício ao treino
    @PostMapping("/{treinoId}/exercicios")
    public ResponseEntity<TreinoDtoResponse> adicionarExercicio(
            @PathVariable Long treinoId,
            @RequestBody Exercicio exercicio) {

        return treinoRepository.findById(treinoId)
                .map(treino -> {
                    exercicio.setTreino(treino); // Associa exercício ao treino
                    exercicioRepository.save(exercicio); // Salva exercício
                    treino.getExercicios().add(exercicio); // Adiciona exercício à lista do treino

                    TreinoDtoResponse responseDto = new TreinoDtoResponse();
                    responseDto.setId(treino.getId());
                    responseDto.setNome(treino.getNome());
                    responseDto.setGrupoMuscular(treino.getGrupoMuscular());
                    responseDto.setCargaSugerida(treino.getCargaSugerida());

                    // Retorna treino atualizado
                    return ResponseEntity.ok(responseDto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Busca treinos por grupo muscular
    @GetMapping("/grupo/{grupoMuscular}")
    public List<TreinoDtoResponse> buscarTreinosPorGrupoMuscular(@PathVariable String grupoMuscular) {
        List<Treino> treinos = treinoRepository.findByGrupoMuscularIgnoreCase(grupoMuscular);

        return treinos.stream().map(treino -> {
            TreinoDtoResponse treinoDtoResponse = new TreinoDtoResponse();
            treinoDtoResponse.setId(treino.getId());
            treinoDtoResponse.setNome(treino.getNome());
            treinoDtoResponse.setGrupoMuscular(treino.getGrupoMuscular());
            treinoDtoResponse.setCargaSugerida(treino.getCargaSugerida());
            return treinoDtoResponse;
        }).collect(Collectors.toList());
    }

    // Atualiza treino
    @PutMapping("/{id}")
    public ResponseEntity<TreinoDtoResponse> atualizarTreino(
            @PathVariable Long id,
            @RequestBody TreinoDtoResponse treinoDto) {

        return treinoRepository.findById(id)
                .map(treino -> {
                    treino.setNome(treinoDto.getNome());
                    treino.setGrupoMuscular(treinoDto.getGrupoMuscular());
                    treino.setCargaSugerida(treinoDto.getCargaSugerida());
                    treinoRepository.save(treino);

                    treinoDto.setId(treino.getId());
                    return ResponseEntity.ok(treinoDto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Remove exercício do treino
    @DeleteMapping("/{treinoId}/exercicios/{exercicioId}")
    public ResponseEntity<Void> removerExercicio(
            @PathVariable Long treinoId,
            @PathVariable Long exercicioId) {

        Optional<Treino> treinoOptional = treinoRepository.findById(treinoId);
        if (!treinoOptional.isPresent()) {
            return ResponseEntity.notFound().build(); // Retorna 404 caso o treino não seja encontrado
        }

        Treino treino = treinoOptional.get();

        // Agora tentamos excluir o exercício
        try {
            exercicioRepository.deleteById(exercicioId); // Remove o exercício
            return ResponseEntity.ok().build(); // Retorna 200 OK com corpo vazio
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build(); // Retorna 500 se ocorrer algum erro
        }
    }

}
