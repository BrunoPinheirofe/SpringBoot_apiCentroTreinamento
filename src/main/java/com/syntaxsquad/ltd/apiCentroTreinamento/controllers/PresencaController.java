package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Presenca;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Turma;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.PresencaRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.AlunoRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/presencas")
public class PresencaController {

    @Autowired
    private PresencaRepository presencaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    // Lista todas as presenças
    @GetMapping
    public List<Presenca> listarPresencas() {
        return presencaRepository.findAll();
    }

    // Registra presença
    @PostMapping("/turmas/{turmaId}/alunos/{alunoMatricula}")
    public ResponseEntity<?> registrarPresenca(
            @PathVariable Long turmaId,
            @PathVariable String alunoMatricula,
            @RequestBody Presenca presenca) {

        Turma turma = turmaRepository.findById(turmaId).orElse(null);
        Aluno aluno = alunoRepository.findByMatricula(alunoMatricula).orElse(null);

        if (turma == null || aluno == null) {
            return ResponseEntity.notFound().build();
        }

        // Verifica se o aluno pertence à turma
        if (!turma.getAlunos().contains(aluno)) {
            return ResponseEntity.badRequest()
                    .body("Aluno não pertence a esta turma");
        }

        presenca.setTurma(turma);
        presenca.setAluno(aluno);
        presenca.setDataPresenca(LocalDateTime.now());

        return ResponseEntity.ok(presencaRepository.save(presenca));
    }

    // Busca presenças por aluno
    @GetMapping("/alunos/{alunoMatricula}")
    public ResponseEntity<List<Presenca>> buscarPresencasPorAluno(@PathVariable String alunoMatricula) {
        Aluno aluno = alunoRepository.findByMatricula(alunoMatricula).orElse(null);
        if (aluno == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(presencaRepository.findByAluno(aluno));
    }

    // Busca presenças por turma
    @GetMapping("/turmas/{turmaId}")
    public ResponseEntity<List<Presenca>> buscarPresencasPorTurma(@PathVariable Long turmaId) {
        Turma turma = turmaRepository.findById(turmaId).orElse(null);
        if (turma == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(presencaRepository.findByTurma(turma));
    }

    // Busca presenças por período
    @GetMapping("/periodo")
    public List<Presenca> buscarPresencasPorPeriodo(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim) {
        return presencaRepository.findByDataPresencaBetween(inicio, fim);
    }

    // Conta presenças de um aluno
    @GetMapping("/alunos/{alunoMatricula}/contagem")
    public ResponseEntity<Long> contarPresencasAluno(@PathVariable String alunoMatricula) {
        Aluno aluno = alunoRepository.findByMatricula(alunoMatricula).orElse(null);
        if (aluno == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(presencaRepository.countPresencasByAluno(aluno));
    }

    // Lista faltas de um aluno
    @GetMapping("/alunos/{alunoMatricula}/faltas")
    public ResponseEntity<List<Presenca>> listarFaltasAluno(@PathVariable String alunoMatricula) {
        Aluno aluno = alunoRepository.findByMatricula(alunoMatricula).orElse(null);
        if (aluno == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(presencaRepository.findByAlunoAndPresenteFalse(aluno));
    }
} 