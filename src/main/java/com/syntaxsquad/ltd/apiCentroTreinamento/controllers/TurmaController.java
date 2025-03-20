package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Turma;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Instrutor;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.TurmaRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.serializers.Matricula;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.AlunoRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.InstrutorRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.TurmaResponseDTO;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.InstrutorDTO;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.TurmaDtoRequest;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.AlunoDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/turmas")
public class TurmaController {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private InstrutorRepository instrutorRepository;

    // Lista todas as turmas
    @Cacheable(value = "turmas_all")
    @GetMapping
    public ResponseEntity<List<TurmaResponseDTO>> listarTurmas() {
        try {
            List<Turma> turmas = turmaRepository.findAll();
            List<TurmaResponseDTO> response = turmas.stream()
                    .map(turma -> {
                        TurmaResponseDTO responseDTO = new TurmaResponseDTO();
                        responseDTO.setId(turma.getId());
                        responseDTO.setNome(turma.getNome());
                        responseDTO.setHorario(turma.getHorario());
                        responseDTO.setDiaSemana(turma.getDiaSemana());

                        // Setando o instrutor no DTO
                        InstrutorDTO instrutorDTO = new InstrutorDTO();
                        Instrutor instrutor = turma.getInstrutor();
                        instrutorDTO.setMatricula(instrutor.getMatricula());
                        instrutorDTO.setNome(instrutor.getNome());
                        responseDTO.setInstrutor(instrutorDTO);

                        // Setando os alunos no DTO
                        List<AlunoDTO> alunosDTO = turma.getAlunos().stream()
                                .map(aluno -> {
                                    AlunoDTO alunoDTO = new AlunoDTO();
                                    alunoDTO.setMatricula(aluno.getMatricula());
                                    alunoDTO.setNome(aluno.getNome());
                                    return alunoDTO;
                                })
                                .collect(Collectors.toList());
                        responseDTO.setAlunos(alunosDTO);

                        return responseDTO;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // Busca turma por ID
    @GetMapping("/{id}")
    public ResponseEntity<TurmaResponseDTO> buscarTurmaPorId(@PathVariable Long id) {
        try {
            return turmaRepository.findById(id)
                    .map(turma -> {
                        TurmaResponseDTO responseDTO = new TurmaResponseDTO();
                        responseDTO.setId(turma.getId());
                        responseDTO.setNome(turma.getNome());
                        responseDTO.setHorario(turma.getHorario());
                        responseDTO.setDiaSemana(turma.getDiaSemana());

                        // Setando o instrutor no DTO
                        InstrutorDTO instrutorDTO = new InstrutorDTO();
                        Instrutor instrutor = turma.getInstrutor();
                        instrutorDTO.setMatricula(instrutor.getMatricula());
                        instrutorDTO.setNome(instrutor.getNome());
                        responseDTO.setInstrutor(instrutorDTO);

                        // Setando os alunos no DTO
                        List<AlunoDTO> alunosDTO = turma.getAlunos().stream()
                                .map(aluno -> {
                                    AlunoDTO alunoDTO = new AlunoDTO();
                                    alunoDTO.setMatricula(aluno.getMatricula());
                                    alunoDTO.setNome(aluno.getNome());
                                    return alunoDTO;
                                })
                                .collect(Collectors.toList());
                        responseDTO.setAlunos(alunosDTO);

                        return ResponseEntity.ok(responseDTO);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // Cria nova turma
    @CacheEvict(value = "turmas_all", allEntries = true)
    @PostMapping
    public ResponseEntity<TurmaResponseDTO> criarTurma(@RequestBody TurmaDtoRequest turmaRequest) {
        try {
            // Verifica se já existe turma no mesmo horário
            if (!turmaRepository.findByDiaSemanaAndHorario(turmaRequest.getDiaSemana(), turmaRequest.getHorario()).isEmpty()) {
                return ResponseEntity.badRequest().body(null); // BadRequest se já existir turma no mesmo horário
            }
    
            // Verifica se o instrutor existe
            Optional<Instrutor> instrutorOpt = instrutorRepository.findByMatricula(turmaRequest.getInstrutorId());
            if (instrutorOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Retorna erro caso não encontre o instrutor
            }
    
            // Cria a turma
            Turma turma = new Turma();
            turma.setNome(turmaRequest.getNome());
            turma.setHorario(turmaRequest.getHorario());
            turma.setDiaSemana(turmaRequest.getDiaSemana());
            turma.setInstrutor(instrutorOpt.get()); // Associa o instrutor à turma
    
            // Adiciona os alunos à turma
            List<Aluno> alunos = alunoRepository.findByMatriculaIn(turmaRequest.getSIds()); // Buscar todos os alunos pelos CPFs
            turma.setAlunos(alunos);
    
            turma = turmaRepository.save(turma); // Salva a turma
    
            // Cria o DTO para a resposta
            TurmaResponseDTO responseDTO = new TurmaResponseDTO();
            responseDTO.setId(turma.getId());
            responseDTO.setNome(turma.getNome());
            responseDTO.setHorario(turma.getHorario());
            responseDTO.setDiaSemana(turma.getDiaSemana());
    
            // Setando o instrutor no DTO
            InstrutorDTO instrutorDTO = new InstrutorDTO();
            instrutorDTO.setMatricula(turma.getInstrutor().getMatricula());
            instrutorDTO.setNome(turma.getInstrutor().getNome());
            responseDTO.setInstrutor(instrutorDTO);
    
            // Setando os alunos no DTO
            List<AlunoDTO> alunosDTO = turma.getAlunos().stream()
                    .map(aluno -> {
                        AlunoDTO alunoDTO = new AlunoDTO();
                        alunoDTO.setMatricula(aluno.getMatricula());
                        alunoDTO.setNome(aluno.getNome());
                        return alunoDTO;
                    })
                    .collect(Collectors.toList());
            responseDTO.setAlunos(alunosDTO);
    
            return ResponseEntity.ok(responseDTO);
    
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    

    // Adiciona aluno à turma
    @CacheEvict(value = "turmas_all", allEntries = true)
    @PostMapping("/{turmaId}/alunos/{alunoMatricula}")
    public ResponseEntity<?> adicionarAluno(@PathVariable Long turmaId, @PathVariable Matricula alunoMatricula) {
        try {
            Turma turma = turmaRepository.findById(turmaId).orElse(null);
            Aluno aluno = alunoRepository.findByMatricula(alunoMatricula).orElse(null);

            if (turma == null || aluno == null) {
                return ResponseEntity.notFound().build();
            }

            if (turma.getAlunos().contains(aluno)) {
                return ResponseEntity.badRequest().body("Aluno já cadastrado na turma");
            }

            // Verifica limite de alunos (exemplo: máximo 20 por turma)
            if (turmaRepository.countAlunosByTurmaId(turmaId) >= 20) {
                return ResponseEntity.badRequest().body("Turma atingiu o limite máximo de alunos");
            }

            turma.getAlunos().add(aluno);
            turma = turmaRepository.save(turma); // Salva a turma novamente com o novo aluno

            // Cria o DTO para a resposta
            TurmaResponseDTO responseDTO = new TurmaResponseDTO();
            responseDTO.setId(turma.getId());
            responseDTO.setNome(turma.getNome());
            responseDTO.setHorario(turma.getHorario());
            responseDTO.setDiaSemana(turma.getDiaSemana());

            // Setando o instrutor no DTO
            InstrutorDTO instrutorDTO = new InstrutorDTO();
            instrutorDTO.setMatricula(turma.getInstrutor().getMatricula());
            instrutorDTO.setNome(turma.getInstrutor().getNome());
            responseDTO.setInstrutor(instrutorDTO);

            // Setando os alunos no DTO
            List<AlunoDTO> alunosDTO = turma.getAlunos().stream()
                    .map(a -> {
                        AlunoDTO alunoDTO = new AlunoDTO();
                        alunoDTO.setMatricula(a.getMatricula());
                        alunoDTO.setNome(a.getNome());
                        return alunoDTO;
                    })
                    .collect(Collectors.toList());
            responseDTO.setAlunos(alunosDTO);

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao adicionar aluno: " + e.getMessage());
        }
    }

    // Remove aluno da turma
    @CacheEvict(value = "turmas_all", allEntries = true)
    @DeleteMapping("/{turmaId}/alunos/{alunoMatricula}")
    public ResponseEntity<?> removerAluno(@PathVariable Long turmaId, @PathVariable Matricula alunoMatricula) {
        try {
            Turma turma = turmaRepository.findById(turmaId).orElse(null);
            Aluno aluno = alunoRepository.findByMatricula(alunoMatricula).orElse(null);

            if (turma == null || aluno == null) {
                return ResponseEntity.notFound().build();
            }

            turma.getAlunos().remove(aluno);
            turma = turmaRepository.save(turma); // Salva a turma novamente sem o aluno

            // Cria o DTO para a resposta
            TurmaResponseDTO responseDTO = new TurmaResponseDTO();
            responseDTO.setId(turma.getId());
            responseDTO.setNome(turma.getNome());
            responseDTO.setHorario(turma.getHorario());
            responseDTO.setDiaSemana(turma.getDiaSemana());

            // Setando o instrutor no DTO
            InstrutorDTO instrutorDTO = new InstrutorDTO();
            instrutorDTO.setMatricula(turma.getInstrutor().getMatricula());
            instrutorDTO.setNome(turma.getInstrutor().getNome());
            responseDTO.setInstrutor(instrutorDTO);

            // Setando os alunos no DTO
            List<AlunoDTO> alunosDTO = turma.getAlunos().stream()
                    .map(a -> {
                        AlunoDTO alunoDTO = new AlunoDTO();
                        alunoDTO.setMatricula(a.getMatricula());
                        alunoDTO.setNome(a.getNome());
                        return alunoDTO;
                    })
                    .collect(Collectors.toList());
            responseDTO.setAlunos(alunosDTO);

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao remover aluno: " + e.getMessage());
        }
    }
}
